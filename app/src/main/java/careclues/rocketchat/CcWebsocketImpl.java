package careclues.rocketchat;


import android.util.Log;

import com.google.gson.Gson;
import com.rocketchat.core.internal.middleware.CoreStreamMiddleware;

import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import careclues.rocketchat.callback.CcHistoryCallback;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.common.CcCoreMiddleware;
import careclues.rocketchat.common.CcCoreStreamMiddleware;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.listner.CcSocketFactory;
import careclues.rocketchat.listner.CcSocketListener;
import careclues.rocketchat.listner.CcSubscribeListener;
import careclues.rocketchat.listner.CcTypingListener;
import careclues.rocketchat.models.CcConnectedMessage;
import careclues.rocketchat.rpc.CcBasicRpc;
import careclues.rocketchat.rpc.CcChatHistoryRPC;
import careclues.rocketchat.rpc.CcCoreSubRPC;
import careclues.rocketchat.rpc.CcMessageRPC;
import careclues.rocketchat.rpc.CcRPC;
import careclues.rocketchat.rpc.CcTypingRPC;
import okhttp3.OkHttpClient;

public class CcWebsocketImpl implements CcSocketListener {

    private CcSocket socket;
    private OkHttpClient client;
    private CcSocketFactory factory;
    private String baseUrl;
    private AtomicInteger integer;
    private CcConnectivityManager connectivityManager;
    private String userId;
    private final CcCoreMiddleware coreMiddleware;
    private final CcCoreStreamMiddleware coreStreamMiddleware;

    private CcConnectListener connecTionListner;



    CcWebsocketImpl(OkHttpClient client, CcSocketFactory factory, String baseUrl) {
        this.client = client;
        this.factory = factory;
        this.baseUrl = baseUrl;
        this.socket = factory.create(client, baseUrl, this);

        connectivityManager = new CcConnectivityManager();
        coreMiddleware = new CcCoreMiddleware();
        coreStreamMiddleware = new CcCoreStreamMiddleware();


        integer = new AtomicInteger(1);
    }

    /*CcWebsocketImpl(OkHttpClient client, CcSocketFactory factory, String baseUrl,CcConnectivityManager connectivityManager) {
        this.client = client;
        this.factory = factory;
        this.baseUrl = baseUrl;
        this.socket = factory.create(client, baseUrl, this);
        this.connectivityManager = connectivityManager;
        integer = new AtomicInteger(1);

    }*/

    void connect(CcConnectListener listener) {
        connecTionListner = listener;
        connectivityManager.register(listener);
        socket.connect();
    }

    public String getMyUserId() {
        return userId;
    }

    public CcConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }

    void disconnect() {
        socket.disconnect();
    }

    public CcSocket getSocket() {
        return socket;
    }

    @Override
    public void onConnected() {
        Log.v("CcWebsocketImpl","RocketChatAPI Connected");
        integer.set(1);
        socket.sendData(CcBasicRpc.ConnectObject());
    }

    @Override
    public void onMessageReceived(JSONObject message) {

        switch (CcRPC.getMessageType(message.optString("msg"))) {
            case CONNECTED:
                processOnConnected(message);
                break;
            case RESULT:
                coreMiddleware.processCallback(Long.valueOf(message.optString("id")), message);
                break;
            case READY:
//                coreStreamMiddleware.processSubscriptionSuccess(message);
                break;
            case ADDED:
//                processCollectionsAdded(message);
                break;
            case CHANGED:
                processCollectionsChanged(message);
                break;
            case REMOVED:
                // TODO - collection REMOVED...
                //dbManager.update(message, RPC.MsgType.REMOVED);
                break;
            case NOSUB:
//                coreStreamMiddleware.processUnsubscriptionSuccess(message);
                break;
            case OTHER:
                break;
            default:

                break;
        }
    }

    private String sessionId;
    private void processOnConnected(JSONObject object) {
        sessionId = object.optString("session");
        connectivityManager.publishConnect(sessionId);
        /*sendData(BasicRPC.PING_MESSAGE);*/

    }

    private void processCollectionsAdded(JSONObject object) {
        if (userId == null) {
            userId = object.optString("id");
        }
        // TODO - collections added
        //dbManager.update(object, RPC.MsgType.ADDED);
    }


//    @Override
//    public void onMessageReceived(JSONObject message) {
//        System.out.println("RocketChatAPI onMessageReceived");
//
//    }




    @Override
    public void onClosing() {
        Log.v("CcWebsocketImpl","RocketChatAPI onClosing");
        connect(connecTionListner);
    }

    @Override
    public void onClosed() {
        Log.v("CcWebsocketImpl","RocketChatAPI onClosed");
        connectivityManager.publishDisconnect(true);


    }

    @Override
    public void onFailure(Throwable throwable) {
        Log.v("CcWebsocketImpl","RocketChatAPI onFailure");
        connectivityManager.publishConnectError(throwable);

    }



    private void processCollectionsChanged(JSONObject object) {
        switch (CcDbManager.getCollectionType(object)) {
            case STREAM_COLLECTION:
                coreStreamMiddleware.processListeners(object);
                break;
            case COLLECTION:
                // TODO - Collections changed...
                //dbManager.update(object, RPC.MsgType.CHANGED);
                break;
        }
    }




    public void login(String username, String password,CcLoginCallback loginCallback) {
        int uniqueID = integer.getAndIncrement();
        coreMiddleware.createCallback(uniqueID, loginCallback, CcCoreMiddleware.CallbackType.LOGIN);
        socket.sendData(CcBasicRpc.login(uniqueID, username, password));
    }

    public void loginUsingToken(String token,CcLoginCallback loginCallback) {
        int uniqueID = integer.getAndIncrement();
        coreMiddleware.createCallback(uniqueID, loginCallback, CcCoreMiddleware.CallbackType.LOGIN);
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

    //Tested
    public void sendIsTyping(String roomId, String username, Boolean istyping) {
        int uniqueID = integer.getAndIncrement();
        socket.sendData(CcTypingRPC.sendTyping(uniqueID, roomId, username, istyping));
    }

    //Tested
    public void sendMessage(String msgId, String roomID, String message, CcMessageCallback.MessageAckCallback callback) {
        int uniqueID = integer.getAndIncrement();
        coreMiddleware.createCallback(uniqueID, callback, CcCoreMiddleware.CallbackType.SEND_MESSAGE);
        socket.sendData(CcMessageRPC.sendMessage(uniqueID, msgId, roomID, message));
    }

    public String subscribeRoomMessageEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        String uniqueID = CcUtils.shortUUID();
        coreStreamMiddleware.createSubscriptionListener(uniqueID, subscribeListener);
        coreStreamMiddleware.createSubscription(roomId, listener, CcCoreStreamMiddleware.SubscriptionType.SUBSCRIBE_ROOM_MESSAGE);
        socket.sendData(CcCoreSubRPC.subscribeRoomMessageEvent(uniqueID, roomId, enable));
        return uniqueID;
    }

    String subscribeRoomTypingEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcTypingListener listener) {
        String uniqueID = CcUtils.shortUUID();
        coreStreamMiddleware.createSubscriptionListener(uniqueID, subscribeListener);
        coreStreamMiddleware.createSubscription(roomId, listener, CcCoreStreamMiddleware.SubscriptionType.SUBSCRIBE_ROOM_TYPING);
        socket.sendData(CcCoreSubRPC.subscribeRoomTypingEvent(uniqueID, roomId, enable));
        return uniqueID;
    }

    //TODO  Needs to handle calback
    public String subscribeRoomChangeEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        String uniqueID = CcUtils.shortUUID();
        coreStreamMiddleware.createSubscriptionListener(uniqueID, subscribeListener);
        coreStreamMiddleware.createSubscription(roomId, listener, CcCoreStreamMiddleware.SubscriptionType.ROOM_SUBSCRIPTION_CHANGED);
        socket.sendData(CcCoreSubRPC.subscribeRoomChanged(uniqueID));
        return uniqueID;
    }

    //TODO  Needs to handle calback
    public String subscribeSubscriptionChangeEvent(String roomId, Boolean enable, CcSubscribeListener subscribeListener, CcMessageCallback.SubscriptionCallback listener) {
        String uniqueID = CcUtils.shortUUID();
        coreStreamMiddleware.createSubscriptionListener(uniqueID, subscribeListener);
        coreStreamMiddleware.createSubscription(roomId, listener, CcCoreStreamMiddleware.SubscriptionType.ROOM_SUBSCRIPTION_CHANGED);
        socket.sendData(CcCoreSubRPC.subscribeSubscriptionChanged(uniqueID));
        return uniqueID;
    }

    void unsubscribeRoom(String subId, CcSubscribeListener subscribeListener) {
        socket.sendData(CcCoreSubRPC.unsubscribeRoom(subId));
        coreStreamMiddleware.createSubscriptionListener(subId, subscribeListener);
    }

    void removeSubscription(String roomId, CcCoreStreamMiddleware.SubscriptionType type) {
        coreStreamMiddleware.removeSubscription(roomId, type);
    }

    void removeAllSubscriptions(String roomId) {
        coreStreamMiddleware.removeAllSubscriptions(roomId);
    }



    void getChatHistory(String roomID, int limit, Date oldestMessageTimestamp,
                        Date lasttimestamp, CcHistoryCallback callback) {
        int uniqueID = integer.getAndIncrement();
        coreMiddleware.createCallback(uniqueID, callback, CcCoreMiddleware.CallbackType.LOAD_HISTORY);
        socket.sendData(CcChatHistoryRPC.loadHistory(uniqueID, roomID, oldestMessageTimestamp, limit, lasttimestamp));
    }
}
