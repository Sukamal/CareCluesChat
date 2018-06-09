package careclues.careclueschat.feature.login;

import android.app.Application;
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
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.login.model.LoginResponse;
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
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppUtil;
import careclues.careclueschat.util.ModelEntityTypeConverter;

public class LoginPresenter implements
        LoginContract.presenter,
        ConnectListener,
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
    private AppPreference appPreference;

    public LoginPresenter(LoginContract.view view, Application application){
        this.view = view;
        this.application = application;
        appPreference = ((CareCluesChatApplication) application).getAppPreference();

        apiExecuter = RestApiExecuter.getInstance();

        chatClient = ((CareCluesChatApplication) application).getRocketChatAPI();
        chatClient.setReconnectionStrategy(null);
        chatClient.connect(this);
    }


    @Override
    public void doLogin(String userId,String password) {
        if (chatClient.getWebsocketImpl().getSocket().getState() == Socket.State.CONNECTED) {
            chatClient.login(userId, password, new LoginCallback() {
                @Override
                public void onLoginSuccess(Token token) {
                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
                    ((CareCluesChatApplication) application).setUserId(token.getUserId());
//                    view.displyNextScreen();

                }

                @Override
                public void onError(RocketChatException error) {

                }
            });
        }else{
//            view.onConnectionFaild(2);
        }

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

//-------------------------------------------------LOAD BASIC DATA----------------------------------------------------------------------------------

    private List<String> roomIdList;
    private List<String> roomIdMember;
    private List<String> roomIdMessage;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;
    private List<RoomMemberEntity> roomMemberEntities;
    private List<MessageEntity> messageEntities;


    @Override
    public void doApiLogin(String userId,String password){
        apiExecuter.doLogin(userId, password, new ServiceCallBack<LoginResponse>(LoginResponse.class) {
            @Override
            public void onSuccess(LoginResponse response) {
//                ((CareCluesChatApplication) application).setToken(response.getData().getAuthToken());
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

    private void getRoom(){

        if(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()) == null){
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
        }else{
            apiExecuter.getRooms(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()),
                    new ServiceCallBack<RoomResponse>(RoomResponse.class) {
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


    }

    private void getSubscription(){
        if(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()) == null){
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
        }else{
            apiExecuter.getSubscription(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()),
                    new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
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

    }

    private void getRoomMembers(final String roomId){
        apiExecuter.getRoomMembers(roomId, new ServiceCallBack<RoomMemberResponse>(RoomMemberResponse.class) {
            @Override
            public void onSuccess(RoomMemberResponse response) {
//                System.out.println("RoomMember Response: "+ response.members.toString());
                filterRoomMembersRecord(roomId,response.members);
                roomIdMember.remove(roomId);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                roomIdMember.remove(roomId);
            }
        });
    }

    private void getMessageHistory(final String roomId){
        apiExecuter.getChatMessage(roomId,0, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
            @Override
            public void onSuccess(MessageResponseModel response) {
//                System.out.println("RoomMember Response: "+ response.messages.toString());
                filterMessageRecord(response.messages);
                roomIdMessage.remove(roomId);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                roomIdMessage.remove(roomId);
            }
        });
    }


    private void getRoomMemberMessageHistory(){

        roomMemberEntities = new ArrayList<>();
        messageEntities = new ArrayList<>();
        for(RoomEntity roomEntity:roomEntities){
            getRoomMembers(roomEntity.roomId);
            getMessageHistory(roomEntity.roomId);
        }
    }

    private void filterRoomRecords(List<RoomModel> rooms){

        if(rooms != null && rooms.size() > 0){
            roomEntities = new ArrayList<>();
            roomIdList = new ArrayList<>();
            for(RoomModel roomModel : rooms){
//                if( roomModel.topic != null && roomModel.topic.equalsIgnoreCase("text-consultation") && roomModel.type == BaseRoomModel.RoomType.PRIVATE){

                    RoomEntity roomEntity;
                    roomEntity = ModelEntityTypeConverter.roomModelToEntity(roomModel);
                    roomEntities.add(roomEntity);
                    roomIdList.add(roomModel.Id);
//                }
            }

            roomIdMember = roomIdList ;
            roomIdMessage = roomIdList ;
            insertRoomRecordIntoDb(roomEntities);
            getRoomMemberMessageHistory();
        }else{
            view.displyNextScreen();
        }

    }

    private void filterSubscriptionRecord(List<SubscriptionModel> update){
        subscriptionEntities = new ArrayList<>();
        for(SubscriptionModel subscriptionModel : update){
            if( roomIdList.contains(subscriptionModel.rId)){

                SubscriptionEntity subscriptionEntity ;
                subscriptionEntity = ModelEntityTypeConverter.subscriptionModelToEntity(subscriptionModel);
                subscriptionEntities.add(subscriptionEntity);
            }
        }

        insertSubscriptionRecordIntoDb(subscriptionEntities);
    }


    private void filterRoomMembersRecord(String roomid,List<RoomMemberModel> memberModels){
        for(RoomMemberModel memberModel : memberModels){
            RoomMemberEntity memberEntity;
            memberEntity = ModelEntityTypeConverter.roomMemberToEntity(memberModel);
            memberEntity.rId = roomid;

            roomMemberEntities.add(memberEntity);
        }
        checkTaskComplete();
    }

    private void filterMessageRecord(List<MessageModel> messageModels){
        for(MessageModel messageModel : messageModels){
            MessageEntity messageEntity;
            messageEntity = ModelEntityTypeConverter.messageModelToEntity(messageModel);
            messageEntities.add(messageEntity);
        }

        checkTaskComplete();
//        insertMessageRecordIntoDb(messageEntities);
    }

    private void insertRoomRecordIntoDb(final List<RoomEntity> roomEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().roomDao().insertAll(roomEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.getMessage());
                }
            }
        });

    }

    private void insertSubscriptionRecordIntoDb(final List<SubscriptionEntity> subscriptionEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().subscriptionDao().insertAll(subscriptionEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.getMessage());
                }
            }
        });
    }

    private void insertRoomMemberRecordIntoDb(final List<RoomMemberEntity> roomMemberEntities){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    ((CareCluesChatApplication)application).getChatDatabase().roomMemberDao().insertAll(roomMemberEntities);

                }catch (Throwable e){
                    Log.e("DBERROR",e.getMessage());
                }
            }
        });
    }

    private void insertMessageRecordIntoDb(final List<MessageEntity> messageEntities) {
        System.out.println("insertMessageRecordIntoDb : " + messageEntities.size());

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().messageDao().insertAll(messageEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.toString());
                }

            }
        });

    }

    private void checkTaskComplete(){
        if(roomIdMember == null || roomIdMember.size() == 0 ){
            if(roomIdMessage == null || roomIdMessage.size() == 0){
                AppPreference appPreference = ((CareCluesChatApplication)application).getAppPreference();
                appPreference.saveStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name(), AppUtil.getCurrentUtcTime());
                insertRoomMemberRecordIntoDb(roomMemberEntities);
                insertMessageRecordIntoDb(messageEntities);
                view.displyNextScreen();
            }
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
