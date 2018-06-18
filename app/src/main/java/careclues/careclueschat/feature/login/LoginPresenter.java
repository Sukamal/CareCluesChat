package careclues.careclueschat.feature.login;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.common.RoomDataPresenter;
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
import careclues.rocketchat.CcRocketChatClient;

public class LoginPresenter implements
        LoginContract.presenter,/*CcConnectListener*/
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
//    private CcRocketChatClient chatClient;
    private RestApiExecuter apiExecuter;
    private AppPreference appPreference;



    public LoginPresenter(final LoginContract.view view, final Application application){
        this.view = view;
        this.application = application;
        appPreference = ((CareCluesChatApplication) application).getAppPreference();

        apiExecuter = RestApiExecuter.getInstance();
//        chatClient = ((CareCluesChatApplication) application).getRocketChatClient();
//        chatClient.connect(this);
        chatClient = ((CareCluesChatApplication) application).getRocketChatAPI();
        chatClient.setReconnectionStrategy(null);
        chatClient.connect(this);

        roomDataPresenter = new RoomDataPresenter(application);
        roomDataPresenter.registerRoomListner(new RoomDataPresenter.FetchRoomListner() {
            @Override
            public void onFetchRoom(List<RoomEntity> entities) {
                Toast.makeText(application, "onFetchRoom", Toast.LENGTH_SHORT).show();
                roomDataPresenter.getSubscription(null,entities);
            }
        });

        roomDataPresenter.registerSubscriptionListner(new RoomDataPresenter.FetchSubscriptionListner() {
            @Override
            public void onFetchSubscription(List<SubscriptionEntity> entities) {
                Toast.makeText(application, "onFetchSubscription", Toast.LENGTH_SHORT).show();
                roomDataPresenter.getUpdatedRoomList(10);

            }
        });

        roomDataPresenter.registerUpdatedRoomListner(new RoomDataPresenter.FetchLastUpdatedRoomDbListner() {
            @Override
            public void onFetchDbUpdatedRoom(List<RoomEntity> entities) {
                roomDataPresenter.fetchMemberAndMessage(entities);
            }
        });

        roomDataPresenter.registerRoomMemberHistoryListner(new RoomDataPresenter.FetchRoomMemberHistoryListner() {
            @Override
            public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities) {
                Toast.makeText(application, "registerRoomMemberHistoryListner", Toast.LENGTH_SHORT).show();
                view.displyNextScreen();
            }
        });

        initHandler();
    }




    @Override
    public void doLogin(String userId,String password) {
        if (chatClient.getWebsocketImpl().getSocket().getState() == Socket.State.CONNECTED) {
            chatClient.login(userId, password, new LoginCallback() {
                @Override
                public void onLoginSuccess(Token token) {
                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
                    ((CareCluesChatApplication) application).setUserId(token.getUserId());
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

//    @Override
//    public void onConnect(String sessionID) {
//        view.displayMessage(application.getString(R.string.connected));
//    }
//
//    @Override
//    public void onDisconnect(boolean closedByServer) {
//        view.onConnectionFaild(2);
//    }
//
//    @Override
//    public void onConnectError(Throwable websocketException) {
//        view.onConnectionFaild(1);
//    }

//-------------------------------------------------LOAD BASIC DATA----------------------------------------------------------------------------------

    private List<String> roomIdList;
    private List<String> roomIdMember;
    private List<String> roomIdMessage;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;
    private List<RoomMemberEntity> roomMemberEntities;
    private List<MessageEntity> messageEntities;
    private List<RoomEntity> lastUpdatedRoomList;


    private Handler handler;
    private final int LOGG_IN_SUCCESS = 1;

    private RoomDataPresenter roomDataPresenter;




    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
                    case LOGG_IN_SUCCESS:
                        roomDataPresenter.getRoom(null);
                        break;
                }
            }
        };
    }

    @Override
    public void doApiLogin(String userId,String password){

//        careclues.rocketchat.CcRocketChatClient chatClient = new careclues.rocketchat.CcRocketChatClient();
//        chatClient.websocketImpl.login(userId,password);



        apiExecuter.doLogin(userId, password, new ServiceCallBack<LoginResponse>(LoginResponse.class) {
            @Override
            public void onSuccess(LoginResponse response) {
//                ((CareCluesChatApplication) application).setToken(response.getData().getAuthToken());
                RestApiExecuter.getInstance().getAuthToken().saveToken(response.getData().getUserId(),response.getData().getAuthToken());
//                System.out.println("Api Response : "+response.toString());
//                System.out.println("LOGIN-START--------------------------------------- : ");
//                getRoom();
//                getSubscription();
                handler.sendEmptyMessage(LOGG_IN_SUCCESS);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

}
