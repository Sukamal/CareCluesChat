package careclues.rocketchat.listner;

import org.json.JSONObject;

import careclues.rocketchat.CcMessageType;

public interface CcSocketListener {

    void onConnected();

    void onMessageReceived(JSONObject message);

    void onClosing();

    void onClosed();

    void onFailure(Throwable throwable);
}
