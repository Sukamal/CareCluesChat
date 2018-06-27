package careclues.careclueschat.feature.login;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
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

import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.common.RoomDataPresenter;
import careclues.careclueschat.feature.login.model.LoginResponse;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.rocketchat.CcRocketChatClient;
import careclues.rocketchat.CcSocket;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.common.CcRocketChatException;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.models.CcToken;

public class LoginPresenter implements
        LoginContract.presenter,CcConnectListener,
        /*ConnectListener,*/

        RoomDataPresenter.FetchRoomListner,
        RoomDataPresenter.FetchSubscriptionListner,
        RoomDataPresenter.FetchLastUpdatedRoomDbListner,
        RoomDataPresenter.FetchRoomMemberHistoryListner

{


    private LoginContract.view view;
    private Application application;
//    private RocketChatClient chatClient;
    private CcRocketChatClient chatClient;
    private RestApiExecuter apiExecuter;
    private AppPreference appPreference;

    private Handler mHandler;
    private final int LOG_IN_SUCCESS = 1;
    private final int LOG_IN_FAIL = 2;
    private RoomDataPresenter roomDataPresenter;



    public LoginPresenter(final LoginContract.view view, final Application application){
        this.view = view;
        this.application = application;
        appPreference = ((CareCluesChatApplication) application).getAppPreference();

        apiExecuter = RestApiExecuter.getInstance();

        chatClient = ((CareCluesChatApplication) application).getRocketChatClient();
        chatClient.connect(this);

//        chatClient = ((CareCluesChatApplication) application).getRocketChatAPI();
//        chatClient.setReconnectionStrategy(null);
//        chatClient.connect(this);


        roomDataPresenter = new RoomDataPresenter(application);
        roomDataPresenter.registerRoomListner(this);
        roomDataPresenter.registerSubscriptionListner(this);
        roomDataPresenter.registerUpdatedRoomListner(this);
        roomDataPresenter.registerRoomMemberHistoryListner(this);

        initHandler();
    }




    @Override
    public void doLogin(String userId,String password) {
//        if (chatClient.getWebsocketImpl().getSocket().getState() == Socket.State.CONNECTED) {
//            chatClient.login(userId, password, new LoginCallback() {
//                @Override
//                public void onLoginSuccess(Token token) {
//                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
//                    ((CareCluesChatApplication) application).setUserId(token.getUserId());
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

        if(chatClient.getWebsocketImpl().getSocket().getState() == CcSocket.State.CONNECTED){
            chatClient.login(userId, password, new CcLoginCallback() {
                @Override
                public void onLoginSuccess(CcToken token) {
                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
                    ((CareCluesChatApplication) application).setUserId(token.getUserId());
                    view.displayMessage(token.getUserId());


                }

                @Override
                public void onError(CcRocketChatException error) {

                }
            });
        }

    }



    @Override
    public void reconnectToServer() {
        chatClient.getWebsocketImpl().getSocket().reconnect();
    }

    @Override
    public void disconnectToServer() {
//        chatClient.getWebsocketImpl().getConnectivityManager().unRegister(this);
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


//-------------------------------------------------LOAD BASIC DATA----------------------------------------------------------------------------------


    @Override
    public void onFetchRoom(List<RoomEntity> entities) {
        Toast.makeText(application, "onFetchRoom", Toast.LENGTH_SHORT).show();
        roomDataPresenter.getSubscription(null,entities);
    }

    @Override
    public void onFetchDbUpdatedRoom(List<RoomEntity> entities) {
        roomDataPresenter.fetchMemberAndMessage(entities);
    }

    @Override
    public void onFetchSubscription(List<SubscriptionEntity> entities) {
        Toast.makeText(application, "onFetchSubscription", Toast.LENGTH_SHORT).show();
        roomDataPresenter.getUpdatedRoomList(10);
    }

    @Override
    public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities) {
        Toast.makeText(application, "registerRoomMemberHistoryListner", Toast.LENGTH_SHORT).show();
        view.displyNextScreen();
    }


    private void initHandler(){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
                    case LOG_IN_SUCCESS:
                        roomDataPresenter.getRoom(null);
                        break;
                    case LOG_IN_FAIL:

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
                mHandler.sendEmptyMessage(LOG_IN_SUCCESS);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                mHandler.sendEmptyMessage(LOG_IN_FAIL);
            }
        });
    }


}
