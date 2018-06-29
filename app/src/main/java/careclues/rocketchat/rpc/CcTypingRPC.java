package careclues.rocketchat.rpc;

public class CcTypingRPC extends CcRPC {

    private static final String SEND_TYPING = "stream-notify-room";

    public static String sendTyping(int integer, String room_id, String username, Boolean istyping) {

        return getRemoteMethodObject(integer, SEND_TYPING, room_id + "/typing", username, istyping).toString();
    }
}
