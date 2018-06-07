package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class GroupResponseModel {

    @SerializedName("group")
    @Expose
    private GroupModel group;
    @SerializedName("success")
    @Expose
    private Boolean success;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n\n\n---------------CREATEGROUP----------------");
        stringBuffer.append(group.toString());
        return stringBuffer.toString();
    }
}
