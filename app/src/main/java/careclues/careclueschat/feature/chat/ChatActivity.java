package careclues.careclueschat.feature.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppDialog;
import careclues.careclueschat.util.AppUtil;

public class ChatActivity extends BaseActivity implements ChatContract.view {

    private ChatPresenter presenter;
    private String roomId;
    private String userId;
    private LinearLayoutManager layoutManager;
    private ChatMessageAdapter messageAdapter;
    private AppDialog appDialog;


    @BindView(R.id.rvChatHistory)
    RecyclerView rvChatHistory;
    @BindView(R.id.et_message)
    EditText etMessage;

    @Override
    public int getContentLayout() {
        return R.layout.activity_chat;
    }

    @Override
    public void initComponents() {
        roomId = getIntent().getStringExtra("roomId");
        String randomId = AppUtil.generateUniquId();
        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
        initRecycleView();
        presenter = new ChatPresenter(this,roomId,getApplication());
        presenter.loadData(50);
    }

    @Override
    public void displyNextScreen() {

    }

    @Override
    public void displayChatList(List<ChatMessageModel> list) {
        displyChatList(list);
    }

    @Override
    public void displayMoreChatList(List<ChatMessageModel> list) {
        messageAdapter.addMessage(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etMessage.setText("");
                messageAdapter.notifyDataSetChanged();
                layoutManager.smoothScrollToPosition(rvChatHistory,null,messageAdapter.getItemCount());

            }
        });
    }

    @Override
    public void displyTypingStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setSubtitle(message);

            }
        });
    }

    @Override
    public void onConnectionFaild(final int errorType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(errorType == 1){
                    AppUtil.getSnackbarWithAction(findViewById(R.id.rlActivityLogin), R.string.connection_error)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    presenter.reconnectToServer();
                                }
                            })
                            .show();
                }else if(errorType == 2){
                    AppUtil.getSnackbarWithAction(findViewById(R.id.rlActivityLogin),  R.string.disconnected_from_server)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    presenter.reconnectToServer();
                                }
                            })
                            .show();
                }
            }
        });

    }

    @Override
    public void displayMessage(String message) {

    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deregisterSocket();
    }

    private void initRecycleView() {
//        rvRoom.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChatHistory.setLayoutManager(layoutManager);
//        rvRoom.setItemAnimator(new DefaultItemAnimator());
    }

    private void displyChatList(final List<ChatMessageModel> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter = new ChatMessageAdapter(ChatActivity.this, list,userId);
                rvChatHistory.setAdapter(messageAdapter);
//                layoutManager.smoothScrollToPosition(rvChatHistory,null,messageAdapter.getItemCount());
            }
        });
    }

    @OnClick(R.id.ib_submit)
    public void sendMessage(){
        presenter.sendMessage(etMessage.getText().toString());
    }

    @OnClick(R.id.ib_attachement)
    public void pickAttachment(){
        appDialog = new AppDialog();
        appDialog.showAlertPickImageDialog(ChatActivity.this, new AppDialog.PickImageListener() {
            @Override
            public void OnCameraPress() {
                if (AppUtil.checkPermission(ChatActivity.this, Manifest.permission.CAMERA)) {

                    if (AppUtil.checkPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        pickImageFromCamera();

                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_CAMERA_REQUEST);
//                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST);
                    }

                } else {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_CAMERA_REQUEST);
                }
            }

            @Override
            public void OnGalleryPress() {
                pickImageGallary();
            }
        });
    }


    public String mImagePath;
    public void pickImageFromCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = AppUtil.createLocalPath("/Document");
        mImagePath = uri.getPath();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(cameraIntent, AppConstant.RequestTag.PICK_CAMERA_REQUEST);
    }

    private void pickImageGallary(){
        List<Intent> targets = new ArrayList<Intent>();
        Uri target = Uri.parse("content://media/external/images/media");
        Intent intent = new Intent(Intent.ACTION_VIEW, target);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        List<ResolveInfo> candidates = getPackageManager().queryIntentActivities(intent, 0);

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
        startActivityForResult(chooser, AppConstant.RequestTag.PICK_GALARRY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.RequestTag.PICK_GALARRY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                String mImagePath = AppUtil.getAbsolutePathFromContentURI(ChatActivity.this, uri);
                // TODO ---------- Upload Image
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == AppConstant.RequestTag.PICK_CAMERA_REQUEST && resultCode == RESULT_OK) {
                // TODO ---------- Upload Image
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstant.RequestTag.PERMISSION_REQUEST_CODE_CAMERA_REQUEST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(AppUtil.checkPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        pickImageFromCamera();
                    }else{
                        ActivityCompat.requestPermissions(ChatActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST);
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "CAMERA Permission Denied, CAMERA permission allows us to access CAMERA. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                }
                break;
            case AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(ChatActivity.this, "WRITE_EXTERNAL_STORAGE Permission Denied.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
