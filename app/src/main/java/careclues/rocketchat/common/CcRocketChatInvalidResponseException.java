package careclues.rocketchat.common;

/**
 * Created by SukamalD on 6/28/2018.
 */

public class CcRocketChatInvalidResponseException extends CcRocketChatException {
    public CcRocketChatInvalidResponseException(String message) {
        super(message);
    }

    public CcRocketChatInvalidResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}