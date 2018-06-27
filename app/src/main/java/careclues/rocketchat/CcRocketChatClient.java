package careclues.rocketchat;


import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.listner.CcSocketFactory;
import careclues.rocketchat.listner.CcSocketListener;
import careclues.rocketchat.listner.CcTokenProvider;
import okhttp3.OkHttpClient;

import static com.rocketchat.common.utils.Preconditions.checkNotNull;

public class CcRocketChatClient {

    public OkHttpClient client;
    public CcSocketFactory factory;
    public String socketUrl = "wss://ticklechat.careclues.com/websocket";
    public CcWebsocketImpl websocketImpl;
    public CcTokenProvider tokenProvider;



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








    public void login(String username, String password, CcLoginCallback loginCallback){
        websocketImpl.login(username,password,loginCallback);
    }

    public void loginUsingToken(String token) {
        websocketImpl.loginUsingToken(token);
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
