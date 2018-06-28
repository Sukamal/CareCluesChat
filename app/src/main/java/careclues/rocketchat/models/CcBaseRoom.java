package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

import careclues.careclueschat.model.RoomUserModel;

/**
 * Created by SukamalD on 6/2/2018.
 */

public abstract class CcBaseRoom {

    @SerializedName("_id")
    public String Id;

    @SerializedName("t")
    public  RoomType type;

    @SerializedName("u")
    public  CcUser user;

    public abstract String name();

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
        return stringBuffer.toString();
    }
}
