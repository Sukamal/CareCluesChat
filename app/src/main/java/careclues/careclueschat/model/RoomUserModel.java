package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class RoomUserModel extends BaseUserModel {
    @SerializedName("name")
    public String name;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n********User********\n");
        stringBuffer.append("\nid : "+id);
        stringBuffer.append("\nusername : "+userName);
        stringBuffer.append("\nname : "+name);
        stringBuffer.append("\n--------User-------");
        return stringBuffer.toString();
    }
}
