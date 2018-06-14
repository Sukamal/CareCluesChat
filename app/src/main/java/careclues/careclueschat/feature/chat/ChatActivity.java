package careclues.careclueschat.feature.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;

public class ChatActivity extends BaseActivity implements ChatContract.view{

    private ChatPresenter presenter;
    private String roomId;
    private LinearLayoutManager layoutManager;
    private ChatMessageAdapter messageAdapter;

    @BindView(R.id.rvChatHistory)
    RecyclerView rvChatHistory;

    @Override
    public int getContentLayout() {
        return R.layout.activity_chat;
    }

    @Override
    public void initComponents() {
        roomId = getIntent().getStringExtra("roomId");
        presenter = new ChatPresenter(this);
        executeServerTask();
    }

    @Override
    public void displyNextScreen() {

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

    private void initRecycleView(){
//        rvRoom.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChatHistory.setLayoutManager(layoutManager);
//        rvRoom.setItemAnimator(new DefaultItemAnimator());
    }

    private void executeServerTask(){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    initRecycleView();
        List<MessageEntity> messageEntities = ((CareCluesChatApplication) getApplication()).getChatDatabase().messageDao().getChatMessage(roomId, 50);
        List<ChatMessageModel> msgList = new ArrayList<>();
        for(MessageEntity entity : messageEntities){
            msgList.add(new ChatMessageModel(entity));
        }
        displyChatList(msgList);

//        presenter.loadData(new ServiceCallBack<PostsModel>(PostsModel.class) {
//            @Override
//            public void onSuccess(PostsModel response) {
//                if(response != null){
//                    System.out.println("------------onSuccess-----------");
//                    System.out.println("Body : "+ response.getBody());
//                    System.out.println("Title : "+ response.getTitle());
//                    System.out.println("UserId : "+ response.getUserId());
//                    System.out.println("Id : "+ response.getId());
//                }
//            }
//
//            @Override
//            public void onFailure(List<NetworkError> errorList) {
//
//            }
//        });

                } catch (Throwable e) {
                    System.out.println("Error111111111111111111111111111111111");
                }

            }
        });

    }

    public void displyChatList(final List<ChatMessageModel> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter = new ChatMessageAdapter(ChatActivity.this,list);
                rvChatHistory.setAdapter(messageAdapter);
            }
        });

    }
}
