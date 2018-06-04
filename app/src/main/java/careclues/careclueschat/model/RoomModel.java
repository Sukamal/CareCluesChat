package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class RoomModel extends BaseRoomModel {

    @SerializedName("topic")
    public String topic;

    @SerializedName("muted")
    public  List<String> mutedUsers;

    @SerializedName("_updatedAt")
    public String updatedAt;

    @SerializedName("description")
    public String description;

    @SerializedName("ro")
    public Boolean readOnly;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n\n---------------Room Start-------------------\n\n");
        stringBuffer.append("\nId : "+Id);
        stringBuffer.append("\ntype : "+type);
        stringBuffer.append("\nuser : "+user);
        stringBuffer.append("\nname : "+name);
        stringBuffer.append("\nfname : "+fName);

        stringBuffer.append("\ntopic : "+topic);
        stringBuffer.append("\nmutedUsers : "+mutedUsers);
        stringBuffer.append("\nupdatedAt : "+updatedAt);
        stringBuffer.append("\ndescription : "+description);
        stringBuffer.append("\nreadOnly : "+readOnly);

        stringBuffer.append("\n\n---------------Room End-------------------\n\n");


        return stringBuffer.toString();
    }
}
