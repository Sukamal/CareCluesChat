package careclues.rocketchat;


import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.listner.CcSocketFactory;
import careclues.rocketchat.listner.CcSocketListener;
import careclues.rocketchat.rpc.CcBasicRpc;
import okhttp3.OkHttpClient;

public class CcWebsocketImpl implements CcSocketListener {

    private CcSocket socket;
    private OkHttpClient client;
    private CcSocketFactory factory;
    private String baseUrl;
    private AtomicInteger integer;


    CcWebsocketImpl(OkHttpClient client, CcSocketFactory factory, String baseUrl) {
        this.client = client;
        this.factory = factory;
        this.baseUrl = baseUrl;
        this.socket = factory.create(client, baseUrl, this);
        integer = new AtomicInteger(1);

    }

    void connect() {
        socket.connect();
    }

    void disconnect() {
        socket.disconnect();
    }

    public CcSocket getSocket() {
        return socket;
    }

    @Override
    public void onConnected() {
        System.out.println("RocketChatAPI Connected");
        integer.set(1);
        socket.sendData(CcBasicRpc.ConnectObject());
    }

    @Override
    public void onMessageReceived(JSONObject message) {
        System.out.println("RocketChatAPI onMessageReceived");

    }

    @Override
    public void onClosing() {
        System.out.println("RocketChatAPI onClosing");

    }

    @Override
    public void onClosed() {
        System.out.println("RocketChatAPI onClosed");

    }

    @Override
    public void onFailure(Throwable throwable) {
        System.out.println("RocketChatAPI onFailure");

    }

    public void login(String username, String password) {
        int uniqueID = integer.getAndIncrement();
        socket.sendData(CcBasicRpc.login(uniqueID, username, password));
    }

    public void loginUsingToken(String token) {
        int uniqueID = integer.getAndIncrement();
        socket.sendData(CcBasicRpc.loginUsingToken(uniqueID, token));
    }

    public void logout() {
        int uniqueID = integer.getAndIncrement();
        socket.sendData(CcBasicRpc.logout(uniqueID));
    }

    public void getSubscriptions() {
        int uniqueID = integer.getAndIncrement();
        socket.sendData(CcBasicRpc.getSubscriptions(uniqueID));
    }

    public void getRooms() {
        int uniqueID = integer.getAndIncrement();
        socket.sendData(CcBasicRpc.getRooms(uniqueID));
    }
}
