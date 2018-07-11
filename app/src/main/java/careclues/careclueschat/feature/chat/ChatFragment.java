package careclues.careclueschat.feature.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.ServerMessageModel;
import careclues.careclueschat.feature.common.BaseFragment;
import careclues.careclueschat.feature.room.RoomMainActivity;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.views.MessageInputView;

public class ChatFragment extends BaseFragment implements ChatContract.view,RoomMainActivity.performChatFragmentAction,
        ChatMessageAdapter.InputTypeListner{

    private String roomId;
    private String userId;
    private ChatPresenter1 presenter;
    private LinearLayoutManager layoutManager;
    private ChatMessageAdapter messageAdapter;
    private LinearLayoutManager layoutManagerAns;
    private ChatAnsAdapter chatAnsAdapter;



    @BindView(R.id.rvChatHistory)
    RecyclerView rvChatHistory;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.mi_messageinput)
    MessageInputView inputView;
    @BindView(R.id.rvAnswers)
    RecyclerView rvAnswers;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomId = (String) getArguments().getString("roomId");
        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        ButterKnife.bind(this, view);
        ((RoomMainActivity)getActivity()).setChatFragmentAction(this);
        ((RoomMainActivity)getActivity()).dispalyFragment = ChatFragment.class.getName();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycleView();
        initView();
    }

    private void initView(){
        presenter = new ChatPresenter1(this,roomId,getActivity().getApplication());
        presenter.loadData(50);
    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChatHistory.setLayoutManager(layoutManager);
    }

    private void initAnsRecycleView() {
        layoutManagerAns = new LinearLayoutManager(getActivity());
        layoutManagerAns.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAnswers.setLayoutManager(layoutManagerAns);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etMessage.setText("");
                messageAdapter.notifyDataSetChanged();
                layoutManager.smoothScrollToPosition(rvChatHistory,null,messageAdapter.getItemCount());

            }
        });
    }

    @Override
    public void displyTypingStatus(String message) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(message);
    }

    @Override
    public void onConnectionFaild(int errorType) {

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
                messageAdapter = new ChatMessageAdapter(getActivity(), list,userId);
                rvChatHistory.setAdapter(messageAdapter);
                messageAdapter.setInputTypeListner(ChatFragment.this);
            }
        });
    }

    @OnClick(R.id.ib_submit)
    public void sendMessage(){
        presenter.sendMessage(etMessage.getText().toString());
    }

    @Override
    public void displyUserTyping(final String roomId, final String user, final Boolean istyping) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ChatFragment.this.roomId.equals(roomId) && istyping) {

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(user + "is typing..");

                }else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

                }

            }

        });
    }

    @Override
    public void updateChatMessage(MessageEntity message) {
        if(message.rId.equals(roomId)){
            List<ChatMessageModel> list = new ArrayList<>();
            ChatMessageModel chatMessageModel = new ChatMessageModel(message.Id,message.msg,message.updatedAt,message.user.id);
            list.add(chatMessageModel);
            messageAdapter.addMessage(list);
            etMessage.setText("");
            messageAdapter.notifyDataSetChanged();
            layoutManager.smoothScrollToPosition(rvChatHistory,null,messageAdapter.getItemCount());

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((RoomMainActivity)getActivity()).setChatFragmentAction(null);

    }

    private void loadAnswers(List<String> answers){
        initAnsRecycleView();
        if(answers != null){
            chatAnsAdapter = new ChatAnsAdapter(getActivity(),answers);
            rvAnswers.setAdapter(chatAnsAdapter);
        }
    }

    private List<String> populateTestAnsList(){
        List<String> answers = new ArrayList<>();

        answers.add("Yes");
        answers.add("No");
        answers.add("Answer 1");
        answers.add("Answer 222222222");
        answers.add("Answer 3");
        answers.add("Answer 4222222222222222222");
        answers.add("Ans 5");
        answers.add("Answer 6");
        answers.add("Answer 7345gdf dfgd");
        answers.add("Load More");


        return answers;
    }

    @Override
    public void onInputType(ServerMessageModel messageModel) {
        if(messageModel != null){
            switch (messageModel.control){
                case "text":
                    populateInput(true);
                    break;
                case "primarySymptomSelect":
                    populateInput(false);
                    break;
                default:
                    populateInput(false);
                    break;
            }
        }
    }

    private void populateInput(boolean istext){
        if(istext){
            inputView.setVisibility(View.VISIBLE);
            rvAnswers.setVisibility(View.GONE);
        }else {
            inputView.setVisibility(View.GONE);
            rvAnswers.setVisibility(View.VISIBLE);
            loadAnswers(populateTestAnsList());
        }
    }
}
