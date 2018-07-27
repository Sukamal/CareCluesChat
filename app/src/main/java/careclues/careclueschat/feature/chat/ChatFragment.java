package careclues.careclueschat.feature.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.CategoryModel;
import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.PatientModel;
import careclues.careclueschat.feature.chat.chatmodel.ReplyMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.ServerMessageModel;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.feature.common.BaseFragment;
import careclues.careclueschat.feature.paytm.PaymentFragment;
import careclues.careclueschat.feature.room.RoomMainActivity;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.LanguageModel;
import careclues.careclueschat.model.SymptomModel;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppUtil;
import careclues.careclueschat.views.AnswerView;
import careclues.careclueschat.views.FamilyMemberView;
import careclues.careclueschat.views.MakePaymentView;
import careclues.careclueschat.views.MessageInputView;

import static android.app.Activity.RESULT_OK;
import static careclues.careclueschat.feature.chat.ChatFragment.InputType.TYPE_FAMILY_MEMBER;
import static careclues.careclueschat.feature.chat.ChatFragment.InputType.TYPE_PAY_FEES;
import static careclues.careclueschat.feature.chat.ChatFragment.InputType.TYPE_SELECT_ANSWERS;
import static careclues.careclueschat.feature.chat.ChatFragment.InputType.TYPE_TEXT;

public class ChatFragment extends BaseFragment implements ChatContract.view, RoomMainActivity.performChatFragmentAction,
        ChatMessageAdapter.InputTypeListner,
        AnswerSelectionListner {

    private String roomId;
    private String userId;
    private ChatPresenter1 presenter;
    private LinearLayoutManager layoutManager;
    private ChatMessageAdapter messageAdapter;

    @BindView(R.id.rvChatHistory)
    RecyclerView rvChatHistory;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.view_messageinput)
    MessageInputView inputView;
    @BindView(R.id.rvAnswers)
    RecyclerView rvAnswers;
    @BindView(R.id.ib_submit_ans)
    ImageButton ibSubmitAns;
    @BindView(R.id.ll_input_layout)
    public LinearLayout llInputLayout;
    @BindView(R.id.view_familymember)
    public FamilyMemberView viewFamilymember;
    @BindView(R.id.view_answer)
    public AnswerView view_answer;
    @BindView(R.id.view_pay_fee)
    public MakePaymentView view_PayFee;
    //    private ChatMessageModel lastMessage;
    private ServerMessageModel lastMessage;
    @BindView(R.id.iv_test)
    ImageView iv_test;


    private View view;


    public enum InputType {
        TYPE_TEXT,
        TYPE_FAMILY_MEMBER,
        TYPE_SELECT_ANSWERS,
        TYPE_PAY_FEES
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomId = (String) getArguments().getString("roomId");
        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chat, container, false);
        ButterKnife.bind(this, view);
        ((RoomMainActivity) getActivity()).setChatFragmentAction(this);
        ((RoomMainActivity) getActivity()).dispalyFragment = ChatFragment.class.getName();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycleView();
        initView();
    }

    private void initView() {
        presenter = new ChatPresenter1(this, roomId, getActivity().getApplication());
        presenter.loadData(50);
    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChatHistory.setLayoutManager(layoutManager);
    }

    @Override
    public void displyNextScreen() {

    }

    @Override
    public void displayChatList(List<ChatMessageModel> list) {
        testmsglist = list;
        displyChatList(list);
    }

    @Override
    public void displayMoreChatList(List<ChatMessageModel> list) {
        messageAdapter.addMessage(list);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etMessage.setText("");
                messageAdapter.notifyDataSetChanged();
                layoutManager.smoothScrollToPosition(rvChatHistory, null, messageAdapter.getItemCount());

            }
        });
    }

    @Override
    public void displyTypingStatus(String message) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(message);
    }

    @Override
    public void onConnectionFaild(int errorType) {

    }

    @Override
    public void displayFamilyMember(List<DataModel> data) {
        viewFamilymember.addMembers(data);
        viewFamilymember.setAnsSelectionListner(this);
        viewFamilymember.setInitData(getActivity(), roomId);
        dispayTemplet(TYPE_FAMILY_MEMBER);
    }

    @Override
    public void displayHealthTopic(List<HealthTopicModel> data) {
        List<ChatAnsModel> ansList = new ArrayList<>();
        for (HealthTopicModel topicModel : data) {
            ChatAnsModel ansModel = new ChatAnsModel(topicModel.name, false);
            ansModel.ansObject = topicModel;
            ansList.add(ansModel);
        }
        view_answer.setAnswerList(ansList, ChatPresenter1.ControlType.CONTROL_HEALTH_TOPIC_SELECT, true);
        view_answer.removeAllListner();
        view_answer.setAnsSelectionListner(this);
        dispayTemplet(TYPE_SELECT_ANSWERS);
    }

    @Override
    public void displayPrimarySymptom(List<SymptomModel> data) {
        List<ChatAnsModel> ansList = new ArrayList<>();
        for (SymptomModel symptom : data) {
            ChatAnsModel ansModel = new ChatAnsModel(symptom.name, false);
            ansModel.ansObject = symptom;
            ansList.add(ansModel);
        }
        view_answer.setAnswerList(ansList, ChatPresenter1.ControlType.CONTROL_PRIMARY_SYMPTOM_SELECT, true);
        view_answer.removeAllListner();
        view_answer.setAnsSelectionListner(this);
        dispayTemplet(TYPE_SELECT_ANSWERS);
    }

    @Override
    public void displaySymptom(List<SymptomModel> data) {
        List<ChatAnsModel> ansList = new ArrayList<>();
        for (SymptomModel symptom : data) {
            ChatAnsModel ansModel = new ChatAnsModel(symptom.name, false);
            ansModel.ansObject = symptom;
            ansList.add(ansModel);
        }
        view_answer.setAnswerList(ansList, ChatPresenter1.ControlType.CONTROL_SYMPTOM_SELECT, true);
        view_answer.removeAllListner();
        view_answer.setAnsSelectionListner(this);
        dispayTemplet(TYPE_SELECT_ANSWERS);
    }

    @Override
    public void displayLanguage(List<LanguageModel> data) {
        List<ChatAnsModel> ansList = new ArrayList<>();
        for (LanguageModel model : data) {
            ChatAnsModel ansModel = new ChatAnsModel(model.name, false);
            ansModel.ansObject = model;
            ansList.add(ansModel);
        }
        view_answer.setAnswerList(ansList, ChatPresenter1.ControlType.CONTROL_SELECT_LANGUAGE, true);
        view_answer.removeAllListner();
        view_answer.setAnsSelectionListner(this);
        dispayTemplet(TYPE_SELECT_ANSWERS);
    }

    @Override
    public void displayOptions(List<String> data) {
        List<ChatAnsModel> ansList = new ArrayList<>();
        for (String option : data) {
            ChatAnsModel ansModel = new ChatAnsModel(option, false);
            ansModel.ansObject = option;
            ansList.add(ansModel);
        }
        view_answer.setAnswerList(ansList, ChatPresenter1.ControlType.CONTROL_SELECT, true);
        view_answer.removeAllListner();
        view_answer.setAnsSelectionListner(this);
        dispayTemplet(TYPE_SELECT_ANSWERS);
    }

    @Override
    public void displayTextInput() {
        inputView.setAnsSelectionListner(this);
        inputView.setActivity(this);
        dispayTemplet(TYPE_TEXT);
    }

    @Override
    public void displayPayFee() {
        view_PayFee.setAnsSelectionListner(this);
        dispayTemplet(TYPE_PAY_FEES);

    }


    @Override
    public void displayNothing() {
        dispayTemplet(null);
    }

    @Override
    public void displayToastMessage(String message) {

    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    private void displyChatList(final List<ChatMessageModel> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter = new ChatMessageAdapter(getActivity(), list, userId);
                rvChatHistory.setAdapter(messageAdapter);
                messageAdapter.setInputTypeListner(ChatFragment.this);
            }
        });
    }


    @Override
    public void displyUserTyping(final String roomId, final String user, final Boolean istyping) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ChatFragment.this.roomId.equals(roomId) && istyping) {

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(user + "is typing..");

                } else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

                }

            }

        });
    }


    // Will fire when soket listen any new message
    @Override
    public void updateChatMessage(MessageEntity message) {
        if (message.rId.equals(roomId)) {
            List<ChatMessageModel> list = new ArrayList<>();
            ChatMessageModel chatMessageModel = new ChatMessageModel(message.Id, message.msg, message.updatedAt, message.user.id);
            list.add(chatMessageModel);
            messageAdapter.addMessage(list);
            etMessage.setText("");
            messageAdapter.notifyDataSetChanged();
            layoutManager.smoothScrollToPosition(rvChatHistory, null, messageAdapter.getItemCount());

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((RoomMainActivity) getActivity()).setChatFragmentAction(null);

    }

    @Override
    public void onInputType(ServerMessageModel messageModel) {
        if (messageModel != null) {
            lastMessage = messageModel;
            presenter.enableInputControlOptions(messageModel);

        }
    }


    private void dispayTemplet(InputType type) {
        for (int i = 0; i < llInputLayout.getChildCount(); i++) {
            llInputLayout.getChildAt(i).setVisibility(View.GONE);
        }
        if (type == TYPE_TEXT) {
            inputView.setVisibility(View.VISIBLE);
        } else if (type == TYPE_FAMILY_MEMBER) {
            viewFamilymember.setVisibility(View.VISIBLE);
        } else if (type == TYPE_SELECT_ANSWERS) {
            view_answer.setVisibility(View.VISIBLE);
        }else if (type == TYPE_PAY_FEES) {
            view_PayFee.setVisibility(View.VISIBLE);
        }
    }


    List<ChatMessageModel> testmsglist;
    int count = 0;

    @OnClick(R.id.nxt)
    public void nextMessage() {
        Log.v("NEXT MSG : ", testmsglist.get(count).text);
        lastMessage = testmsglist.get(count).messageModel;
        onInputType(lastMessage);
        count++;
    }

    @OnClick(R.id.prev)
    public void prevMessage() {
        count--;
        Log.v("NEXT MSG : ", testmsglist.get(count).text);
        lastMessage = testmsglist.get(count).messageModel;
        onInputType(lastMessage);

    }


    @Override
    public void onPatientSelected(PatientModel patientModel) {
        String replyMsgId = lastMessage.id;
        String content = (patientModel.self) ? "I am consulting for myself" : "I am consulting for my " + patientModel.displayName;
        patientModel.displayName = null;
        patientModel.age = 23;
        populetSendMessage(lastMessage.control,replyMsgId, content, patientModel, lastMessage.categoryModel, lastMessage.symptomModel);
    }

    @Override
    public void onHealthTopicSelected(HealthTopicModel healthTopicModel) {
        String replyMsgId = lastMessage.id;
        String content = "My problem is " + healthTopicModel.name;

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.name = healthTopicModel.name;
        categoryModel.alternate_medicine = healthTopicModel.alternateMedicine;
        categoryModel.link = healthTopicModel.getLink("self");

        populetSendMessage(lastMessage.control,replyMsgId, content, lastMessage.patientModel, categoryModel, lastMessage.symptomModel);
    }

    @Override
    public void onSymptomSelected(SymptomModel symptomModel) {
        String replyMsgId = lastMessage.id;
        String content = symptomModel.name;
        populetSendMessage(lastMessage.control,replyMsgId, content, lastMessage.patientModel, lastMessage.categoryModel, symptomModel);
    }

    @Override
    public void onOptionSelected(String option) {
        String replyMsgId = lastMessage.id;
        String content = option;
        populetSendMessage(lastMessage.control,replyMsgId, content, lastMessage.patientModel, lastMessage.categoryModel, lastMessage.symptomModel);
    }

    @Override
    public void onSimpleTextSelected(String msg) {
        String replyMsgId = lastMessage.id;
        String content = msg;
        populetSendMessage(lastMessage.control,replyMsgId, content, lastMessage.patientModel, lastMessage.categoryModel, lastMessage.symptomModel);
    }

    @Override
    public void onLanguageSelected(String option) {
        //TODO add server call to post language
        presenter.addLanguageApiCall(option);
    }

    @Override
    public void onPayButtonClick() {
        Toast.makeText(getActivity(), "Click On Pay Button ChatFragment", Toast.LENGTH_SHORT).show();
        Fragment fragment = new PaymentFragment();
        addFragment(fragment,true,null);
    }

    @Override
    public void onUpdateLanguageToServer(String languages) {
        String replyMsgId = lastMessage.id;
        String content = languages;
        lastMessage.patientModel.languagePreferredExists = true;
        populetSendMessage(lastMessage.control,replyMsgId, content, lastMessage.patientModel, lastMessage.categoryModel, lastMessage.symptomModel);
    }


    private void populetSendMessage(String messageType,  String replyMsgId, String content, PatientModel patientModel, CategoryModel categoryModel, SymptomModel symptomModel) {
        ReplyMessageModel replyMessageModel = new ReplyMessageModel();

        replyMessageModel.type = "reply";
        if (replyMsgId != null)
            replyMessageModel.replyToQuestionId = replyMsgId;
        if (content != null)
            replyMessageModel.content = content;
        if (patientModel != null)
            replyMessageModel.patient = patientModel;
        if (categoryModel != null)
            replyMessageModel.categoryModel = categoryModel;
        if (symptomModel != null) {
            replyMessageModel.symptomModel = symptomModel;
            replyMessageModel.symptomModel.links = null;
        }
        presenter.sendMessageViaApi(replyMessageModel,messageType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.RequestTag.PICK_GALARRY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                String mImagePath = AppUtil.getAbsolutePathFromContentURI(getActivity(), uri);
                File file = new File(mImagePath);

                Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
                iv_test.setImageBitmap(bitmap);

                presenter.copy(file,getActivity(),roomId,".jpg");

                // TODO ---------- Upload Image
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == AppConstant.RequestTag.PICK_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            presenter.saveToInternalStorage(getActivity(),roomId,photo);

            iv_test.setImageBitmap(photo);
//            String mImagePath = AppUtil.getAbsolutePathFromContentURI(ChatActivity.this, uri);
        } else if (requestCode == AppConstant.RequestTag.PICK_DOCUMENT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                String mImagePath = AppUtil.getAbsolutePathFromContentURI(getActivity(), uri);
                File file = new File(mImagePath);
//                presenter.uploadFile(file,"Test Upload File");
                presenter.copy(file,getActivity(),roomId,".pdf");
                // TODO ---------- Upload Image
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstant.RequestTag.PERMISSION_REQUEST_CODE_CAMERA_REQUEST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (AppUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        inputView.pickImageFromCamera();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST);
                    }
                } else {
                    Toast.makeText(getActivity(), "CAMERA Permission Denied, CAMERA permission allows us to access CAMERA. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                }
                break;
            case AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getActivity(), "WRITE_EXTERNAL_STORAGE Permission Denied.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



}
