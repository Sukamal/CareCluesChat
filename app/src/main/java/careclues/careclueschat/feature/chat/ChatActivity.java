package careclues.careclueschat.feature.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

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

public class ChatActivity extends BaseActivity implements ChatContract.view {

    private ChatPresenter presenter;
    private String roomId;
    private String userId;
    private LinearLayoutManager layoutManager;
    private ChatMessageAdapter messageAdapter;


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
                messageAdapter.notifyDataSetChanged();

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

    private void initRecycleView() {
//        rvRoom.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChatHistory.setLayoutManager(layoutManager);
//        rvRoom.setItemAnimator(new DefaultItemAnimator());
    }

    public void displyChatList(final List<ChatMessageModel> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter = new ChatMessageAdapter(ChatActivity.this, list,userId);
                rvChatHistory.setAdapter(messageAdapter);
                rvChatHistory.scrollToPosition(list.size()-1);
            }
        });
    }

    @OnClick(R.id.ib_submit)
    public void sendMessage(){
        presenter.sendMessage(etMessage.getText().toString());
    }
}
