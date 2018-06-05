package careclues.careclueschat.feature.login;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.rocketchat.common.RocketChatApiException;
import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.model.User;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.listener.TypingListener;
import com.rocketchat.common.network.Socket;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.AccountListener;
import com.rocketchat.core.callback.EmojiListener;
import com.rocketchat.core.callback.GetSubscriptionListener;
import com.rocketchat.core.callback.LoginCallback;
import com.rocketchat.core.callback.MessageCallback;
import com.rocketchat.core.callback.UserListener;
import com.rocketchat.core.model.Emoji;
import com.rocketchat.core.model.Message;
import com.rocketchat.core.model.Permission;
import com.rocketchat.core.model.PublicSetting;
import com.rocketchat.core.model.Subscription;
import com.rocketchat.core.model.Token;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.login.model.LoginResponse;
import careclues.careclueschat.feature.login.model.LoginRequest;
import careclues.careclueschat.feature.room.RoomResponse;
import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.MessageModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomMemberResponse;
import careclues.careclueschat.model.RoomModel;
import careclues.careclueschat.model.SubscriptionModel;
import careclues.careclueschat.model.SubscriptionResponse;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;

public class LoginPresenter implements LoginContract.presenter,ConnectListener,
        AccountListener.getPermissionsListener,
        AccountListener.getPublicSettingsListener,
        EmojiListener,
        GetSubscriptionListener,
        UserListener.getUserRoleListener,
        MessageCallback.SubscriptionCallback,
        TypingListener {

    private LoginContract.view view;
    private Application application;
    private RocketChatClient chatClient;
    private RestApiExecuter apiExecuter;

    public LoginPresenter(LoginContract.view view, Application application){
        this.view = view;
        this.application = application;

        apiExecuter = RestApiExecuter.getInstance();

        chatClient = ((CareCluesChatApplication) application).getRocketChatAPI();
        chatClient.setReconnectionStrategy(null);
        chatClient.connect(this);
    }


    @Override
    public void doLogin(String userId,String password) {
//        if (chatClient.getWebsocketImpl().getSocket().getState() == Socket.State.CONNECTED) {
//            chatClient.login(userId, password, new LoginCallback() {
//                @Override
//                public void onLoginSuccess(Token token) {
//                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
////                    view.displyNextScreen();
//
//                }
//
//                @Override
//                public void onError(RocketChatException error) {
//
//                }
//            });
//        }else{
////            view.onConnectionFaild(2);
//        }

        testGetMessageRecord();
        apiExecuter.doLogin(userId, password, new ServiceCallBack<LoginResponse>(LoginResponse.class) {
            @Override
            public void onSuccess(LoginResponse response) {
                RestApiExecuter.getInstance().getAuthToken().saveToken(response.getData().getUserId(),response.getData().getAuthToken());
//                System.out.println("Api Response : "+response.toString());
//                System.out.println("LOGIN-START--------------------------------------- : ");
                getRoom();
                getSubscription();
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });

    }



    @Override
    public void reconnectToServer() {
        chatClient.getWebsocketImpl().getSocket().reconnect();
    }

    @Override
    public void disconnectToServer() {
        chatClient.getWebsocketImpl().getConnectivityManager().unRegister(this);
    }

    @Override
    public void onConnect(String sessionID) {
        view.displayMessage(application.getString(R.string.connected));
    }

    @Override
    public void onDisconnect(boolean closedByServer) {
        view.onConnectionFaild(2);
    }

    @Override
    public void onConnectError(Throwable websocketException) {
        view.onConnectionFaild(1);
    }

    @Override
    public void onTyping(String roomId, String user, Boolean istyping) {

    }

    @Override
    public void onGetPermissions(List<Permission> permissions, RocketChatApiException error) {

    }

    @Override
    public void onGetPublicSettings(List<PublicSetting> settings, RocketChatApiException error) {

    }

    @Override
    public void onListCustomEmoji(List<Emoji> emojis, RocketChatApiException error) {

    }

    @Override
    public void onGetSubscriptions(List<Subscription> subscriptions, RocketChatApiException error) {

    }

    @Override
    public void onMessage(String roomId, Message message) {

    }

    @Override
    public void onUserRoles(List<User> users, RocketChatApiException error) {

    }


    private void getRoom(){
        apiExecuter.getRooms(new ServiceCallBack<RoomResponse>(RoomResponse.class) {
            @Override
            public void onSuccess(RoomResponse response) {
//                System.out.println("Room Response: "+ response);
                filterRoomRecords(response.getUpdate());
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    private void getSubscription(){
        apiExecuter.getSubscription(new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
            @Override
            public void onSuccess(SubscriptionResponse response) {
//                System.out.println("Subscription Response: "+ response);
                filterSubscriptionRecord(response.update);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    private List<String> roomIdList;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;

    private void filterRoomRecords(List<RoomModel> rooms){

        roomEntities = new ArrayList<>();
        roomIdList = new ArrayList<>();
        for(RoomModel roomModel : rooms){
            if( roomModel.topic != null && roomModel.topic.equalsIgnoreCase("text-consultation") && roomModel.type == BaseRoomModel.RoomType.PRIVATE){
                RoomEntity roomEntity = new RoomEntity();
                roomEntity.roomId = roomModel.Id;
                roomEntity.type = roomModel.type.name();
                roomEntity.userId = roomModel.user.id;
                roomEntity.userName = roomModel.user.userName;
                roomEntity.roomName = roomModel.name;
                roomEntity.roomFname = roomModel.fName;
                roomEntity.topic = roomModel.topic;
                roomEntity.updatedAt = roomModel.updatedAt;
                roomEntity.description = roomModel.description;
                roomEntity.readOnly = roomModel.readOnly;

                roomEntities.add(roomEntity);
                roomIdList.add(roomModel.Id);
            }
        }

        insertRoomRecordIntoDb(roomEntities);
        getRoomMemberMessageHistory();
    }

    private void filterSubscriptionRecord(List<SubscriptionModel> update){
        subscriptionEntities = new ArrayList<>();
        for(SubscriptionModel subscriptionModel : update){
            if( roomIdList.contains(subscriptionModel.rId)){

                SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
                subscriptionEntity.Id  = subscriptionModel.Id;
                subscriptionEntity.type  = subscriptionModel.type.name();
                subscriptionEntity.userId  = subscriptionModel.user.id;
                subscriptionEntity.userName  = subscriptionModel.user.userName;
                subscriptionEntity.name  = subscriptionModel.user.name;
                subscriptionEntity.rId  = subscriptionModel.rId;
                subscriptionEntity.timeStamp  = subscriptionModel.timeStamp;
                subscriptionEntity.lastSeen  = subscriptionModel.lastSeen;
                subscriptionEntity.open  = subscriptionModel.open;
                subscriptionEntity.alert  = subscriptionModel.alert;
                subscriptionEntity.updatedAt  = subscriptionModel.updatedAt;
                subscriptionEntity.unread  = subscriptionModel.unread;
                subscriptionEntity.userMentions  = subscriptionModel.userMentions;
                subscriptionEntity.groupMentions  = subscriptionModel.groupMentions;

                subscriptionEntities.add(subscriptionEntity);
            }
        }

        insertSubscriptionRecordIntoDb(subscriptionEntities);
    }


    private void getRoomMemberMessageHistory(){

//        for(int i = 0; i < 5; i++){
//            getMessageHistory(roomEntities.get(i).roomId);
//        }

        for(RoomEntity roomEntity:roomEntities){
            getRoomMembers(roomEntity.roomId);
            getMessageHistory(roomEntity.roomId);
        }
    }

    private void insertRoomRecordIntoDb(final List<RoomEntity> roomEntities){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((CareCluesChatApplication)application).getChatDatabase().roomDao().insertAll(roomEntities);
            }
        }).start();


    }

    private void insertSubscriptionRecordIntoDb(final List<SubscriptionEntity> subscriptionEntities){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().insertAll(subscriptionEntities);
            }
        }).start();
    }

    private void getRoomMembers(final String roomId){
        apiExecuter.getRoomMembers(roomId, new ServiceCallBack<RoomMemberResponse>(RoomMemberResponse.class) {
            @Override
            public void onSuccess(RoomMemberResponse response) {
//                System.out.println("RoomMember Response: "+ response.members.toString());
                filterRoomMembersRecord(roomId,response.members);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    private void filterRoomMembersRecord(String roomid,List<RoomMemberModel> memberModels){
        List<RoomMemberEntity> roomMemberEntities = new ArrayList<>();
        for(RoomMemberModel memberModel : memberModels){
            RoomMemberEntity memberEntity = new RoomMemberEntity();
            memberEntity.rId = roomid;
            memberEntity.Id = memberModel.id;
            memberEntity.userName = memberModel.userName;
            memberEntity.name = memberModel.name;
            memberEntity.status = memberModel.status;
            memberEntity.utcOffset = memberModel.utcOffset;

            roomMemberEntities.add(memberEntity);
        }

        insertRoomMemberRecordIntoDb(roomMemberEntities);
    }

    private void insertRoomMemberRecordIntoDb(final List<RoomMemberEntity> roomMemberEntities){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((CareCluesChatApplication)application).getChatDatabase().roomMemberDao().insertAll(roomMemberEntities);
            }
        }).start();
    }


    private void getMessageHistory(final String roomId){
        apiExecuter.getChatMessage(roomId,0, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
            @Override
            public void onSuccess(MessageResponseModel response) {
//                System.out.println("RoomMember Response: "+ response.messages.toString());
                filterMessageRecord(response.messages);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    private void filterMessageRecord(List<MessageModel> messageModels){
        List<MessageEntity> messageEntities = new ArrayList<>();
        for(MessageModel messageModel : messageModels){
            System.out.println("filterMessageRecord : " +messageModel.rId);
            MessageEntity messageEntity = new MessageEntity();

            messageEntity.Id = messageModel.id;
            messageEntity.rId = messageModel.rId;
            messageEntity.msg = messageModel.msg;
            messageEntity.timeStamp = messageModel.timeStamp;
//            messageEntity.user = messageModel.user;
            messageEntity.updatedAt = messageModel.updatedAt;
            messageEntity.type = messageModel.type;
            messageEntity.alias = messageModel.alias;
            messageEntity.groupable = messageModel.groupable;
//            messageEntity.mentions = messageModel.mentions;
            messageEntity.parseUrls = messageModel.parseUrls;
            messageEntity.meta = messageModel.meta;

            messageEntities.add(messageEntity);
        }

        insertMessageRecordIntoDb(messageEntities);
    }


    private void insertMessageRecordIntoDb(final List<MessageEntity> messageEntities){
        System.out.println("insertMessageRecordIntoDb : " +messageEntities.size());
        if(messageEntities != null ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        ((CareCluesChatApplication)application).getChatDatabase().messageDao().insertAll(messageEntities);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                view.displyNextScreen();
                            }
                        });

                    }catch(Exception e){
                        System.out.println("Errorrr !!!!!!! "+ e.toString());
                    }

                }
            }).start();
        }
    }

    private void testGetMessageRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MessageEntity> messageEntityList = ((CareCluesChatApplication)application).getChatDatabase().messageDao().getAll();

                for(MessageEntity messageEntity : messageEntityList){
                    System.out.println(" Message : " + messageEntity.toString());
                }
            }
        }).start();
    }


}
