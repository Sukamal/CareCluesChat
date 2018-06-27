package careclues.rocketchat.common;

/**
 * Created by SukamalD on 6/28/2018.
 */

public class CcRocketChatException extends RuntimeException {

    public CcRocketChatException(String message) {
        super(message);
    }

    public CcRocketChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
