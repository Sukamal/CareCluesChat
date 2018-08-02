package careclues.rocketchat.callback;

import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.RoomModel;
import careclues.rocketchat.listner.CcListener;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcRoom;
import careclues.rocketchat.models.CcSubscription;

public class CcMessageCallback {
    public interface SubscriptionCallback extends CcListener {
        void onMessage(String roomId, CcMessage message);
    }

    public interface MessageAckCallback extends CcCallback {
        void onMessageAck(CcMessage message);
    }

    public interface NewRoomCallback extends CcListener {
        void onNewRoom(String userId, CcRoom room,String type);
        void onNewSubscription(String userId, CcSubscription subscription, String type);

    }

}