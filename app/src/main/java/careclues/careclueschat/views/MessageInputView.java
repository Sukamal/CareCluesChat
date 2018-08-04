package careclues.careclueschat.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;
//import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.chat.ChatFragment;
import careclues.careclueschat.feature.chat.ChatPresenter1;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppDialog;
import careclues.careclueschat.util.AppUtil;

public class MessageInputView extends RelativeLayout implements View.OnClickListener,TextWatcher{

    private ImageButton ivSubmit;
    private EditText etMessage;
    private CharSequence input;
    private ImageButton ibAttachement;
    private AnswerSelectionListner answerSelectionListner;
    private AppDialog appDialog;
    private Context context;
    private Fragment fragment;
    private ConstraintLayout clInput;
    Rect rectf = new Rect();

    public MessageInputView(Context context) {
        super(context);
        initView(context);
    }

    public MessageInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MessageInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setActivity(Fragment fragment){
        this.fragment = fragment;
    }

    public void setAnsSelectionListner(AnswerSelectionListner listner){
        this.answerSelectionListner = listner;
    }

    private void initView(Context context){

        this.context = context;
        inflate(context, R.layout.view_message_input_text,this);

        clInput = (ConstraintLayout) findViewById(R.id.cl_input);

        ivSubmit = (ImageButton) findViewById(R.id.ib_submit);
        ibAttachement = (ImageButton) findViewById(R.id.ib_attachement);

        etMessage = findViewById(R.id.et_message);
        ivSubmit.setEnabled(false);
        ivSubmit.setOnClickListener(this);
        ibAttachement.setOnClickListener(this);
        etMessage.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        input = s;
        ivSubmit.setEnabled(input.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_submit:
                sendMessage();
                break;
            case R.id.ib_attachement:
                sendAttachment();
                break;
        }
    }

    private void sendMessage(){
        if(answerSelectionListner != null){
            answerSelectionListner.onSimpleTextSelected(etMessage.getText().toString());
        }
    }

    private void sendAttachment(){
        pickAttachment();
    }

    public void pickAttachment(){
        appDialog = new AppDialog();

        int test1[] = new int[2];
        clInput.getLocationInWindow(test1);

//        int test2[] = new int[2];
//        clInput.getLocationOnScreen(test2);
//
//        System.out.println(test1[1] + " " + test2[1]);

        appDialog.showAlertPickImageDialog(context, this, new AppDialog.PickImageListener() {
            @Override
            public void OnCameraPress() {
                if (AppUtil.checkPermission(context, Manifest.permission.CAMERA)) {
                    if (AppUtil.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        pickImageFromCamera();

                    } else {
                        askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }

                } else {
                    askPermission(Manifest.permission.CAMERA);
                }
            }

            @Override
            public void OnGalleryPress() {
                pickImageGallary();
            }

            @Override
            public void OnDocumentPress() {
                pickDocumentFromGallery();
            }
        });
    }


    public void askPermission(String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST);
                break;
            case Manifest.permission.CAMERA:
                fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_CAMERA_REQUEST);
                break;

        }

    }

    public void pickImageFromCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri uri = AppUtil.createLocalPath("/Document");
//        mImagePath = uri.getPath();
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(cameraIntent, AppConstant.RequestTag.PICK_CAMERA_REQUEST);
    }

    public void pickDocumentFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("pdf");
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent, "Choose a file");
        fragment.startActivityForResult(intent, AppConstant.RequestTag.PICK_DOCUMENT_REQUEST);
    }

    public void pickImageGallary() {

        List<Intent> targets = new ArrayList<Intent>();
        Uri target = Uri.parse("content://media/external/images/media");
        Intent intent = new Intent(Intent.ACTION_VIEW, target);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        List<ResolveInfo> candidates = fragment.getActivity().getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.google.android.apps.photos")
                    && !packageName
                    .equals("com.google.android.apps.plus")
                    && !packageName.equals("com.android.documentsui")) {
                Intent iWantThis = new Intent();
                iWantThis.setType("image/*");
                iWantThis.setAction(Intent.ACTION_GET_CONTENT);
                iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
            }
        }

        Intent chooser = Intent.createChooser(targets.remove(0),
                "Select Picture");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                targets.toArray(new Parcelable[targets.size()]));
        fragment.startActivityForResult(chooser, AppConstant.RequestTag.PICK_GALARRY_REQUEST);
    }

}
