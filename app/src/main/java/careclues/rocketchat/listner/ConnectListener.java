package careclues.rocketchat.listner;

public interface ConnectListener {

    void onConnect(String sessionID);

    void onDisconnect(boolean closedByServer);

    void onConnectError(Throwable websocketException);
}
