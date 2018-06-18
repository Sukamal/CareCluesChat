package careclues.careclueschat.feature.chat;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.model.BaseRoom;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.listener.TypingListener;
import com.rocketchat.core.ChatRoom;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.MessageCallback;
import com.rocketchat.core.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.User;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;

        public class ChatPresenter implements ChatContract.presenter,
        ConnectListener,
        MessageCallback.SubscriptionCallback,
        TypingListener {

    private ChatContract.view view;
    private Application application;
    private String userId;
    private RoomMemberEntity userDetails;
    private String roomId;
    private RocketChatClient api;
    private ChatRoom chatRoom;



    public ChatPresenter(ChatContract.view view,String roomId, Application application){
        this.view = view;
        this.application = application;
        this.roomId = roomId;

        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
        api = ((CareCluesChatApplication) application).getRocketChatAPI();
        getLoginUserDetails(userId);
        initWebSoket();
    }

    private void getLoginUserDetails(final String userId){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                userDetails = ((CareCluesChatApplication)application).getChatDatabase().roomMemberDao().findById(userId);
            }
        });
    }

    private void initWebSoket(){

        api.getWebsocketImpl().getConnectivityManager().register(this);
        List<BaseRoom> rooms = new ArrayList<>();
        BaseRoom  baseRoom;
        baseRoom  = new BaseRoom() {
            @Override
            public String roomId() {
                return roomId;
            }

            @Nullable
            @Override
            public RoomType type() {
                return RoomType.PRIVATE;
            }

            @Nullable
            @Override
            public com.rocketchat.common.data.model.User user() {
                return new com.rocketchat.common.data.model.User() {
                    @Nullable
                    @Override
                    public String id() {
//                        return "2eRbrjSZnsACYkRx4";
                        return userDetails.Id;
                    }

                    @Nullable
                    @Override
                    public String username() {
//                        return "sachu-985";
                        return userDetails.userName;
                    }

                    @Nullable
                    @Override
                    public List<String> roles() {
                        return null;
                    }
                };
            }

            @Nullable
            @Override
            public String name() {
                return null;
            }
        };
        rooms.add(baseRoom);
        api.getChatRoomFactory().createChatRooms(rooms);

        chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);
        chatRoom.subscribeRoomMessageEvent(null, this);
        chatRoom.subscribeRoomTypingEvent(null, this);

    }


    @Override
    public void loadData(int count) {
        getChatHistory(roomId,count);
    }

    @Override
    public void sendMessage(String msg) {
        chatRoom.sendMessage(msg.toString(), new MessageCallback.MessageAckCallback() {
            @Override
            public void onMessageAck(com.rocketchat.core.model.Message message) {

            }

            @Override
            public void onError(RocketChatException error) {
                Log.e("ERROR : ",error.getMessage());
            }
        });
    }

    private void getChatHistory(final String roomId, final int count){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<MessageEntity> messageEntities = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getChatMessage(roomId, count);
                    List<ChatMessageModel> msgList = new ArrayList<>();
                    for (MessageEntity entity : messageEntities) {
                        msgList.add(new ChatMessageModel(entity));
                    }
                    view.displayChatList(msgList);

                } catch (Throwable e) {
                    System.out.println("Error111111111111111111111111111111111");
                }

            }
        });
    }

    @Override
    public void onConnect(String sessionID) {

        //        String token = ((CareCluesChatApplication)getApplicationContext()).getToken();
        String token = RestApiExecuter.getInstance().getAuthToken().getToken();

    }

    @Override
    public void onDisconnect(boolean closedByServer) {

    }

    @Override
    public void onConnectError(Throwable websocketException) {

    }

    @Override
    public void onMessage(String roomId, Message message) {

        List<ChatMessageModel> list = new ArrayList<>();
        ChatMessageModel chatMessageModel = new ChatMessageModel(message.id(),message.message(),new Date(message.updatedAt()),message.sender().id());
        list.add(chatMessageModel);
        view.displayMoreChatList(list);

    }

    @Override
    public void onTyping(String roomId, String user, Boolean istyping) {

    }
}
