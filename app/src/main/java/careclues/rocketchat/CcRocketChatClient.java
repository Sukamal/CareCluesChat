package careclues.rocketchat;


import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.WebsocketImpl;

import java.util.Date;

import careclues.careclueschat.util.AppConstant;
import careclues.rocketchat.callback.CcHistoryCallback;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.common.CcCoreStreamMiddleware;
import careclues.rocketchat.listner.CcChatRoomFactory;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.listner.CcSocketFactory;
import careclues.rocketchat.listner.CcSocketListener;
import careclues.rocketchat.listner.CcSubscribeListener;
import careclues.rocketchat.listner.CcTokenProvider;
import careclues.rocketchat.listner.CcTypingListener;
import okhttp3.OkHttpClient;

import static com.rocketchat.common.utils.Preconditions.checkNotNull;

public class CcRocketChatClient {

    public OkHttpClient client;
    public CcSocketFactory factory;
    public String socketUrl = AppConstant.SOCKET_URL;
    public CcWebsocketImpl websocketImpl;
    public CcTokenProvider tokenProvider;
    public WebsocketImpl websockImpl;


    public CcChatRoomFactory chatRoomFactory;


    private CcRocketChatClient(final Builder builder){

        if(builder.websocketUrl == null){
            throw new IllegalStateException("Must provide socketurl");
        }

        if(builder.client == null){
            client = new OkHttpClient();
        }else{
            client = builder.client;
        }

        if(builder.factory == null){
            factory = new CcSocketFactory() {
                @Override
                public CcSocket create(OkHttpClient client, String url, CcSocketListener socketListener) {
                    return new CcSocket(client,url,socketListener);
                }
            };
        }else{
            factory = builder.factory;
        }

        tokenProvider = builder.provider;
        chatRoomFactory = new CcChatRoomFactory(this);
        websocketImpl = new CcWebsocketImpl(client, factory,builder.websocketUrl);

    }

    public static final class Builder {
        private String websocketUrl;
        private OkHttpClient client;
        private CcSocketFactory factory;
        private CcTokenProvider provider;


        public Builder websocketUrl(String url) {
            this.websocketUrl = url;
            return this;
        }

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder socketFactory(CcSocketFactory factory) {
            this.factory = factory;
            return this;
        }

        public Builder tokenProvider(CcTokenProvider provider) {
            this.provider = checkNotNull(provider, "provider == null");
            return this;
        }


        public CcRocketChatClient build() {
            return new CcRocketChatClient(this);
        }

    }

    public CcWebsocketImpl getWebsocketImpl() {
        return websocketImpl;
    }

    public void connect(CcConnectListener listener) {
        websocketImpl.connect(listener);
    }

    public void setReconnectionStrategy(CcReconnectionStrategy strategy) {
        websocketImpl.setReconnectionStrategy(strategy);
    }

    public void disconnect() {
        websocketImpl.disconnect();
    }

    public void disablePing() {
        websocketImpl.disablePing();
    }

    public void enablePing() {
        websocketImpl.enablePing();
    }
    public CcChatRoomFactory getChatRoomFactory() {
        return chatRoomFactory;
    }


    void getChatHistory(String roomID, int limit, Date oldestMessageTimestamp,
                        Date lasttimestamp, CcHistoryCallback callback) {
        websocketImpl.getChatHistory(roomID, limit, oldestMessageTimestamp, lasttimestamp, callback);
    }

    public String subscribeRoomMessageEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        return websocketImpl.subscribeRoomMessageEvent(roomId, enable, subscribeListener, listener);
    }

    public String subscribeRoomTypingEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcTypingListener listener) {
        return websocketImpl.subscribeRoomTypingEvent(roomId, enable, subscribeListener, listener);
    }

    public String subscribeRoomChanged(String userId, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        return websocketImpl.subscribeRoomChangeEvent(userId, subscribeListener, listener);
    }

    public String subscribeSubscriptionChangeEvent(String userId, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        return websocketImpl.subscribeSubscriptionChangeEvent(userId, subscribeListener, listener);
    }

    public String subscribeMessageChangeEvent(String userId, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        return websocketImpl.subscribeMessageChangeEvent(userId, subscribeListener, listener);
    }

    public void removeSubscription(String roomId, CcCoreStreamMiddleware.SubscriptionType type) {
        websocketImpl.removeSubscription(roomId, CcCoreStreamMiddleware.SubscriptionType.SUBSCRIBE_ROOM_MESSAGE);
    }

    public void login(String username, String password, CcLoginCallback loginCallback){
        websocketImpl.login(username,password,loginCallback);
    }

    public void loginUsingToken(String token,CcLoginCallback loginCallback) {
        websocketImpl.loginUsingToken(token,loginCallback);
    }

    public void sendIsTyping(String roomId, String username, Boolean istyping) {
        websocketImpl.sendIsTyping(roomId, username, istyping);
    }

    public void sendMessage(String msgId, String roomID, String message, CcMessageCallback.MessageAckCallback callback) {
        websocketImpl.sendMessage(msgId, roomID, message, callback);
    }

    public void unsubscribeRoom(String subId, CcSubscribeListener subscribeListener) {
        websocketImpl.unsubscribeRoom(subId, subscribeListener);
    }

    public void removeAllSubscriptions(String roomId) {
        websocketImpl.removeAllSubscriptions(roomId);
    }


    public void logout() {
        websocketImpl.logout();
    }

    public void getSubscriptions() {
        websocketImpl.getSubscriptions();
    }

    public void getRooms() {
        websocketImpl.getRooms();
    }

}
