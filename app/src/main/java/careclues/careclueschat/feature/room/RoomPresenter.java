package careclues.careclueschat.feature.room;

import android.app.Application;
import android.util.Log;

import com.rocketchat.common.RocketChatApiException;
import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.model.User;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.listener.SimpleListCallback;
import com.rocketchat.common.listener.TypingListener;
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
import java.util.Random;
import java.util.logging.Handler;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.login.LoginContract;
import careclues.careclueschat.model.GroupResponseModel;
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
import careclues.careclueschat.util.ModelEntityTypeConverter;

public class RoomPresenter implements RoomContract.presenter,ConnectListener,
        AccountListener.getPermissionsListener,
        AccountListener.getPublicSettingsListener,
        EmojiListener,
        GetSubscriptionListener,
        UserListener.getUserRoleListener,
        MessageCallback.SubscriptionCallback,
        TypingListener {

    private RoomContract.view view;
    private Application application;
    private RocketChatClient chatClient;
    private List<SubscriptionEntity> list;
    private RestApiExecuter apiExecuter;
    private AppPreference appPreference;


    public RoomPresenter(RoomContract.view view, Application application){
        this.view = view;
        this.application = application;
        apiExecuter = RestApiExecuter.getInstance();
        appPreference = ((CareCluesChatApplication) application).getAppPreference();
        registerConnectivity();
    }

    @Override
    public void registerConnectivity() {
        chatClient = ((CareCluesChatApplication) application).getRocketChatAPI();
        chatClient.getWebsocketImpl().getConnectivityManager().register(this);
        chatClient.subscribeActiveUsers(null);
        chatClient.subscribeUserData(null);
        getRoom();
    }

    @Override
    public void getRoom() {
        /*chatClient.getSubscriptions(new SimpleListCallback<Subscription>() {
            @Override
            public void onSuccess(List<Subscription> list) {
                chatClient.getChatRoomFactory().createChatRooms(list);
                view.displyRoomList(list);

            }

            @Override
            public void onError(RocketChatException error) {

            }
        });
*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = new ArrayList<>();
                List<SubscriptionEntity> moreList;
//                moreList = ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().getSubscripttion(0,10);
                moreList = ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().getAll();

                if(moreList != null && moreList.size() > 0){
                    list.addAll(moreList);
                }
                view.displyRoomList(list);
            }
        }).start();


    }

    @Override
    public void getMoreRoom(final int startCount, final int threshold) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SubscriptionEntity> moreList;
                moreList = ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().getSubscripttion(startCount,threshold);
                if(moreList != null && moreList.size() > 0){
                    list.addAll(moreList);
                }
                view.displyRoomList(list);
            }
        }).start();
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
    public void createNewRoom() {
        String roomName = "SUKU-TEST-"+(System.currentTimeMillis()/1000);
        String[] members = {"api_admin","bot-la2zewmltd","sachu-985"};
        apiExecuter.createPrivateRoom(roomName, members, new ServiceCallBack<GroupResponseModel>(GroupResponseModel.class) {
            @Override
            public void onSuccess(GroupResponseModel response) {
                System.out.println("New Room : "+ response.toString());
                getRoomData();
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    @Override
    public void onConnect(String sessionID) {

        String token = ((CareCluesChatApplication)application).getToken();
        chatClient.loginUsingToken(token, new LoginCallback() {
            @Override
            public void onLoginSuccess(Token token) {
                chatClient.subscribeActiveUsers(null);
                chatClient.subscribeUserData(null);
            }

            @Override
            public void onError(RocketChatException error) {

            }
        });

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


    private void getRoomData(){

        if(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()) == null){
            apiExecuter.getRooms(new ServiceCallBack<RoomResponse>(RoomResponse.class) {
                @Override
                public void onSuccess(RoomResponse response) {
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
                            filterSubscriptionRecord(response.update);
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
        }

    }

    private List<String> roomIdList;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;

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
            insertRoomRecordIntoDb(roomEntities);
            getSubscription();

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
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getRoom();
                        }
                    },5000);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.getMessage());
                }
            }
        });
    }

}
