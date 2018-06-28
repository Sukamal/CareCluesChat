package careclues.rocketchat.listner;

public interface CcSubscribeListener extends CcListener {
    void onSubscribe(Boolean isSubscribed, String subId);
}
