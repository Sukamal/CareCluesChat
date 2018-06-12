package careclues.rocketchat.listner;

public interface CcConnectListener {

    void onConnect(String sessionID);

    void onDisconnect(boolean closedByServer);

    void onConnectError(Throwable websocketException);
}
