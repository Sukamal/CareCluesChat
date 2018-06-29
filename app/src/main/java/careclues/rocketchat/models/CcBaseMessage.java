package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CcBaseMessage {

    @SerializedName("_id")
    public String id;

    @SerializedName("rid")
    public String rId;

    @SerializedName("msg")
    public String msg;


    @SerializedName("ts")
    public CcDate timeStamp;

    @SerializedName("u")
    public CcUser user;

    @SerializedName("_updatedAt")
    public CcDate updatedAt;

    @SerializedName("t")
    public String type;

    @SerializedName("alias")
    public String alias;



/*
    public CcBaseMessage(JSONObject object) {
        try {
            id = object.optString("_id");
            rId = object.optString("rid");
            msg = object.optString("msg");
            if (object.optJSONObject("ts") != null) {
                timeStamp = new Date(object.getJSONObject("ts").getLong("$date"));
            }
            sender = new UserObject(object.optJSONObject("u"));
            updatedAt = new Date(object.getJSONObject("_updatedAt").getLong("$date"));

            if (object.optJSONObject("editedAt") != null) {
                editedAt = new Date(object.getJSONObject("editedAt").getLong("$date"));
                editedBy = new UserObject(object.getJSONObject("editedBy"));
            }
            messagetype = object.optString("t");
            senderAlias = object.optString("alias");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/


}
