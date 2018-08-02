package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CcSubscription extends CcBaseRoom {

    @Override
    public String name() {
        return null;
    }

    @SerializedName("ts")
    public CcDate ts;

    @SerializedName("ls")
    public CcDate ls;

    @SerializedName("rid")
    public String rid;

    @SerializedName("open")
    public boolean open;
    @SerializedName("alert")
    public boolean alert;
    @SerializedName("unread")
    public int unread;
    @SerializedName("userMentions")
    public int userMentions;
    @SerializedName("groupMentions")
    public int groupMentions;

    @SerializedName("_updatedAt")
    public CcDate _updatedAt;


    @SerializedName("muted")
    public List<String> mutedUsers;


    @SerializedName("description")
    public String description;

    @SerializedName("ro")
    public Boolean readOnly;
}
