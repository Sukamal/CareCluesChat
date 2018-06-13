package careclues.rocketchat;

import java.util.concurrent.ConcurrentLinkedQueue;

import careclues.rocketchat.listner.CcConnectListener;

public class CcConnectivityManager {

    private ConcurrentLinkedQueue<CcConnectListener> listeners;

    public CcConnectivityManager() {
        listeners = new ConcurrentLinkedQueue<>();
    }

    public void register(CcConnectListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void publishConnect(String sessionId) {
        for (CcConnectListener listener : listeners) {
            listener.onConnect(sessionId);
        }
    }

    public void publishDisconnect(boolean closedByServer) {
        for (CcConnectListener listener : listeners) {
            listener.onDisconnect(closedByServer);
        }
    }

    public void publishConnectError(Throwable websocketException) {
        for (CcConnectListener listener : listeners) {
            listener.onConnectError(websocketException);
        }
    }

    public Boolean unRegister(CcConnectListener listener) {
        return listeners.remove(listener);
    }
}
