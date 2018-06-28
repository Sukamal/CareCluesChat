package careclues.rocketchat.callback;

import careclues.rocketchat.listner.CcListener;
import careclues.rocketchat.models.CcMessage;

public class CcMessageCallback {
    public interface SubscriptionCallback extends CcListener {
        void onMessage(String roomId, CcMessage message);
    }

    public interface MessageAckCallback extends CcCallback {
        void onMessageAck(CcMessage message);
    }
}