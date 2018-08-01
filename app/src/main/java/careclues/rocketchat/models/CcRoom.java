package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CcRoom extends CcBaseRoom {

    @Override
    public String name() {
        return null;
    }


    @SerializedName("topic")
    public String topic;

    @SerializedName("muted")
    public List<String> mutedUsers;

    @SerializedName("_updatedAt")
    public CcDate updatedAt;

    @SerializedName("description")
    public String description;

    @SerializedName("ro")
    public Boolean readOnly;
}
