package careclues.rocketchat;

import com.rocketchat.core.internal.rpc.BasicRPC;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import careclues.rocketchat.listner.ConnectListener;
import careclues.rocketchat.listner.SocketFactory;
import careclues.rocketchat.listner.SocketListener;
import okhttp3.OkHttpClient;

public class WebsocketImpl implements SocketListener {

    private Socket socket;
    private OkHttpClient client;
    private SocketFactory factory;
    private String baseUrl;
    private AtomicInteger integer;


    WebsocketImpl(OkHttpClient client, SocketFactory factory,String baseUrl){
        this.client = client;
        this.factory = factory;
        this.baseUrl = baseUrl;
        this.socket = factory.create(client, baseUrl,this);
        integer = new AtomicInteger(1);

    }

    void connect(ConnectListener connectListener) {
        socket.connect();
    }

    void disconnect() {
        socket.disconnect();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void onConnected() {
        System.out.println("RocketChatAPI Connected");
        integer.set(1);
        socket.sendData(BasicRPC.ConnectObject());
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
}
