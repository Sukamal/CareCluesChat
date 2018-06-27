package careclues.rocketchat.common;

import org.json.JSONObject;

/**
 * Created by SukamalD on 6/28/2018.
 */

public class CcRocketChatApiException extends CcRocketChatException {

    private String reason;
    private String errorType;
    private long error;
    private String message;

    public CcRocketChatApiException(long error, String message, String errorType) {
        super((message));
        this.error = error;
        this.message = this.reason = message;
        this.errorType = errorType;
        this.reason = String.valueOf(error) + ": " + message;
    }

    public CcRocketChatApiException(JSONObject object) {
        super(object.optString("message"));
        reason = object.optString("reason");
        errorType = object.optString("errorType");
        error = object.optLong("error");
        message = object.optString("message");
    }

    public String getReason() {
        return reason;
    }

    public String getErrorType() {
        return errorType;
    }

    public long getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ErrorObject{" +
                "reason='" + reason + '\'' +
                ", errorType='" + errorType + '\'' +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}

