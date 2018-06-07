package careclues.careclueschat.feature.room;

import android.app.Application;

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

import java.util.List;
import java.util.Random;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.login.LoginContract;
import careclues.careclueschat.model.GroupResponseModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;

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

    public RoomPresenter(RoomContract.view view, Application application){
        this.view = view;
        this.application = application;
        apiExecuter = RestApiExecuter.getInstance();
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
//        chatClient.getSubscriptions(new SimpleListCallback<Subscription>() {
//            @Override
//            public void onSuccess(List<Subscription> list) {
//                chatClient.getChatRoomFactory().createChatRooms(list);
//                view.displyRoomList(list);
//
//            }
//
//            @Override
//            public void onError(RocketChatException error) {
//
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                list = ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().getSubscripttion(0,10);

                for(SubscriptionEntity entity : list){
                    view.displyRoomList(list);
                }
            }
        }).start();


    }

    @Override
    public void getMoreRoom(final int startCount, final int threshold) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().getSubscripttion(startCount,threshold);

                for(SubscriptionEntity entity : list){
                    view.displyMoreRoomList(list);
                }
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
        String[] members = {"api_admin","bot-la2zewmltd"};
        apiExecuter.createPrivateRoom(roomName, members, new ServiceCallBack<GroupResponseModel>(GroupResponseModel.class) {
            @Override
            public void onSuccess(GroupResponseModel response) {
                System.out.println("New Room : "+ response.toString());
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
}
