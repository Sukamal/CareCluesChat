package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class BaseRoomModel {

    @SerializedName("_id")
    public String Id;

    @SerializedName("t")
    public  RoomType type;

    @SerializedName("u")
    public  RoomUserModel user;

    @SerializedName("name")
    public  String name;

    @SerializedName("fname")
    public  String fName;

    public enum RoomType {
        @SerializedName("c") PUBLIC,
        @SerializedName("p") PRIVATE,
        @SerializedName("d") ONE_TO_ONE,
        @SerializedName("l") LIVECHAT
    }


    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nId : "+Id);
        stringBuffer.append("\ntype : "+type);
        stringBuffer.append("\nuser : "+user);
        stringBuffer.append("\nname : "+name);
        stringBuffer.append("\nfname : "+fName);

        return stringBuffer.toString();
    }
}
