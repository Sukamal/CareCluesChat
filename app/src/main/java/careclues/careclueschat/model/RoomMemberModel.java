package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class RoomMemberModel extends RoomUserModel{
    @SerializedName("status")
    public String status;
    @SerializedName("utcOffset")
    public Double utcOffset;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n******RoomMemeber*******\n");
        stringBuffer.append("\nid : "+id);
        stringBuffer.append("\nusername : "+userName);
        stringBuffer.append("\nname : "+name);
        stringBuffer.append("\nstatus : "+status);
        stringBuffer.append("\nutcOffset : "+utcOffset);
        stringBuffer.append("\n----RoomMemeber-----------\n");

        return stringBuffer.toString();
    }
}
