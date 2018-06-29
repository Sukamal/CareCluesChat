package careclues.rocketchat.listner;

public interface CcTypingListener extends CcListener{
    void onTyping(String roomId, String user, Boolean istyping);
}
