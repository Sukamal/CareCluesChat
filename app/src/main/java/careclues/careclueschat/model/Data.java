package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/1/2018.
 */

public class Data {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("authToken")
    @Expose
    private String authToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nuserId: "+userId);
        stringBuffer.append("\nauthToken: "+authToken.toString());
        return stringBuffer.toString();
    }
}
