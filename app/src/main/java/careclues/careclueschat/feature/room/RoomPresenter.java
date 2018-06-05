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

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.login.LoginContract;

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

    public RoomPresenter(RoomContract.view view, Application application){
        this.view = view;
        this.application = application;
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
