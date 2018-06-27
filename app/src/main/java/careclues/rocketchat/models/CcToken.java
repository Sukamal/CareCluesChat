package careclues.rocketchat.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CcToken {

    private String userId;
    private String authToken;
    private Date expiry;

    public CcToken(String userId, String authToken, Date expiry) {
        this.userId = userId;
        this.authToken = authToken;
        this.expiry = expiry;
    }

    public CcToken(JSONObject object) throws JSONException {
        userId = object.getString("id");
        authToken = object.getString("token");
        JSONObject expires = object.optJSONObject("tokenExpires");
        if (expires != null) {
            long date = expires.optLong("$date");
            expiry = new Date(date);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Date getExpiry() {
        return expiry;
    }

    @Override
    public String toString() {
        return "TokenObject{" +
                "userId='" + userId + '\'' +
                ", authToken='" + authToken + '\'' +
                ", expiry=" + expiry +
                '}';
    }
}
