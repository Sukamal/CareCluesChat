package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class SubscriptionModel extends BaseRoomModel {

    @SerializedName("rid")
    public String rId;

    @SerializedName("roles")
    public List<String> roles;

    @SerializedName("ts")
//    public String timeStamp;
    public Date timeStamp;

    @SerializedName("ls")
    public Date lastSeen;

    @SerializedName("open")
    public Boolean open;

    @SerializedName("alert")
    public Boolean alert;

    @SerializedName("updatedAt")
//    public String updatedAt;
    public Date updatedAt;

    @SerializedName("unread")
    public Integer unread;

    @SerializedName("userMentions")
    public Integer userMentions;

    @SerializedName("groupMentions")
    public Integer groupMentions;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n\n---------------Subscription start-------------------\n\n");
        stringBuffer.append("\nId : "+Id);
        stringBuffer.append("\ntype : "+type);
        stringBuffer.append("\nuser : "+user);
        stringBuffer.append("\nname : "+name);
        stringBuffer.append("\nfname : "+fName);

        stringBuffer.append("\nrid : "+rId);
        stringBuffer.append("\nroles : "+roles);
        stringBuffer.append("\nupdatedAt : "+updatedAt);
        stringBuffer.append("\ntimestamp : "+timeStamp);
        stringBuffer.append("\nlastSeen : "+lastSeen);
        stringBuffer.append("\nopen : "+open);
        stringBuffer.append("\nupdatedAt : "+updatedAt);
        stringBuffer.append("\nalert : "+alert);
        stringBuffer.append("\nupdatedAt : "+updatedAt);
        stringBuffer.append("\nunread : "+unread);
        stringBuffer.append("\nuserMentions : "+userMentions);
        stringBuffer.append("\ngroupMentions : "+groupMentions);
        stringBuffer.append("\n\n---------------Subscription end-------------------\n\n");

        return stringBuffer.toString();
    }

}
