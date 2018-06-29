package careclues.rocketchat;


import java.util.Date;

import careclues.rocketchat.callback.CcHistoryCallback;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.callback.CcMessageCallback;
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
    public String socketUrl = "wss://ticklechat.careclues.com/websocket";
    public CcWebsocketImpl websocketImpl;
    public CcTokenProvider tokenProvider;

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

    public void disconnect() {
        websocketImpl.disconnect();
    }

    public CcChatRoomFactory getChatRoomFactory() {
        return chatRoomFactory;
    }


    void getChatHistory(String roomID, int limit, Date oldestMessageTimestamp,
                        Date lasttimestamp, CcHistoryCallback callback) {
        websocketImpl.getChatHistory(roomID, limit, oldestMessageTimestamp, lasttimestamp, callback);
    }

    String subscribeRoomMessageEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        return websocketImpl.subscribeRoomMessageEvent(roomId, enable, subscribeListener, listener);
    }

    String subscribeRoomTypingEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcTypingListener listener) {
        return websocketImpl.subscribeRoomTypingEvent(roomId, enable, subscribeListener, listener);
    }




    public void login(String username, String password, CcLoginCallback loginCallback){
        websocketImpl.login(username,password,loginCallback);
    }

    public void loginUsingToken(String token,CcLoginCallback loginCallback) {
        websocketImpl.loginUsingToken(token,loginCallback);
    }

    //Tested
    void sendIsTyping(String roomId, String username, Boolean istyping) {
        websocketImpl.sendIsTyping(roomId, username, istyping);
    }

    //Tested
    void sendMessage(String msgId, String roomID, String message, CcMessageCallback.MessageAckCallback callback) {
        websocketImpl.sendMessage(msgId, roomID, message, callback);
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
