package careclues.rocketchat;

import com.google.gson.Gson;
import com.rocketchat.common.data.rpc.RPC;
import com.rocketchat.common.network.TaskHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import careclues.rocketchat.listner.CcSocketListener;
import careclues.rocketchat.models.CcSocketMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class CcSocket extends WebSocketListener {
    private CcSocketListener listener;
    private Request request;
    private OkHttpClient client;
    private String url;
    private WebSocket ws;


    private long pingInterval;
    private boolean pingEnable;
    private CcTaskHandler pingHandler;
    private CcTaskHandler timeoutHandler;
    private State currentState = State.DISCONNECTED;
    private CcReconnectionStrategy strategy;
    private Timer timer;
    private boolean selfDisconnect;


    public enum State {
        CREATED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
    }

    public CcSocket(OkHttpClient client, String url, CcSocketListener socketListener) {

        this.client = client;
        this.url = url;
        this.listener = socketListener;
        pingHandler = new CcTaskHandler();
        timeoutHandler = new CcTaskHandler();
        createSocket();
    }

    public CcSocket(String url, CcSocketListener listener) {
        this(new OkHttpClient(), url,listener);
    }

    public void createSocket() {
        // Create a WebSocket with a socket connection timeout value.
        request = new Request.Builder()
                .url(url)
                .addHeader("Accept-Encoding", "gzip, deflate, sdch")
                .addHeader("Accept-Language", "en-US,en;q=0.8")
                .addHeader("Pragma", "no-cache")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36")
                .build();
        setState(CcSocket.State.CREATED);
    }

    public void connect() {
        setState(CcSocket.State.CONNECTING);
        ws = client.newWebSocket(request, this);
        listener.onConnected();
    }

    protected void connectAsync() {
        connect();
    }

    public void reconnect() {
        connect();
    }

    public void disconnect() {
        if (currentState == State.DISCONNECTED) {
            return;
        } else if (currentState == State.CONNECTED) {
            ws.close(1001, "Close");
            setState(State.DISCONNECTING);
        } else {
            setState(State.DISCONNECTED);
        }

        pingHandler.removeLast();
        timeoutHandler.removeLast();
        selfDisconnect = true;
    }

    protected void sendDataInBackground(String message) {
        sendData(message);
    }

    public void setPingInterval(long pingInterval) {
        pingEnable = true;
        if (pingInterval != this.pingInterval) {
            this.pingInterval = pingInterval;
        }
    }

    public void disablePing() {
        if (pingEnable) {
            pingHandler.cancel();
            pingEnable = false;
        }
    }

    public void enablePing() {
        if (!pingEnable) {
            pingEnable = true;
            sendData(RPC.PING_MESSAGE);
        }
    }

    public boolean isPingEnabled() {
        return pingEnable;
    }

    private void setState(CcSocket.State state) {
        currentState = state;
    }

    public CcSocket.State getState() {
        return currentState;
    }


    public void sendData(String message) {
        if (getState() == CcSocket.State.CONNECTED) {
            ws.send(message);
        }
    }

    // OkHttp WebSocket callbacks
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        setState(State.CONNECTED);

        if (strategy != null) {
            strategy.setNumberOfAttempts(0);
        }
        listener.onConnected();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        onTextMessage(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        onTextMessage(bytes.toString());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        setState(State.DISCONNECTING);
        listener.onClosing();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        setState(State.DISCONNECTED);
        pingHandler.removeLast();
        timeoutHandler.removeLast();
        processReconnection();
        listener.onClosed();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
        setState(State.DISCONNECTED);
        pingHandler.removeLast();
        timeoutHandler.removeLast();
        processReconnection();
        listener.onFailure(throwable);
    }

    private void onTextMessage(String text) {
        JSONObject message = null;
        try {
            message = new JSONObject(text);
            System.out.println("------------------------------ "+message);
        } catch (JSONException e) {
            e.printStackTrace();
            return; // ignore non-json messages
        }

        CcSocketMessage socketMessage;
        Gson gson = new Gson();
        socketMessage = gson.fromJson(text,CcSocketMessage.class);

        // Valid message - reschedule next ping
        reschedulePing();

        // Proccess PING messages or send the message downstream
        RPC.MsgType messageType = RPC.getMessageType(message.optString("msg"));
        if (messageType == RPC.MsgType.PING) {
            sendData(RPC.PONG_MESSAGE);
        } else {
            listener.onMessageReceived(socketMessage.messageType,socketMessage.id,text);
        }
    }

    /* visible for testing */
    void processReconnection() {
        if (strategy != null && !selfDisconnect) {
            if (strategy.getNumberOfAttempts() < strategy.getMaxAttempts()) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        reconnect();
                        strategy.processAttempts();
                        timer.cancel();
                        timer.purge();
                    }
                }, strategy.getReconnectInterval());

            } else {
                pingHandler.cancel();
            }
        } else {
            pingHandler.cancel();
            selfDisconnect = false;
        }
    }

    // TODO: 15/8/17 solve problem of PONG RECEIVE FAILED by giving a fair chance
    protected void reschedulePing() {
        if (!pingEnable)
            return;

        pingHandler.removeLast();
        timeoutHandler.removeLast();
        pingHandler.postDelayed(new TimerTask() {
            @Override
            public void run() {
                sendData(RPC.PING_MESSAGE);
            }
        }, pingInterval);
        timeoutHandler.postDelayed(new TimerTask() {
            @Override
            public void run() {
                if (getState() != State.DISCONNECTING && getState() != State.DISCONNECTED) {
                    ws.cancel();
                    //onFailure(ws, new IOException("PING Timeout"), null);
                }
                timeoutHandler.removeLast();
            }
        }, 2 * pingInterval);
    }
}