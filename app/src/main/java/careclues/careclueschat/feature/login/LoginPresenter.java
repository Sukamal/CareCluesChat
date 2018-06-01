package careclues.careclueschat.feature.login;

import android.app.Application;

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

    public LoginPresenter(LoginContract.view view, Application application){
        this.view = view;
        this.application = application;

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
                    view.displyNextScreen();

                }

                @Override
                public void onError(RocketChatException error) {
                    view.displayMessage("Error in login "+error.getMessage());
                }
            });
        }else{
            view.onConnectionFaild(2);
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
}
