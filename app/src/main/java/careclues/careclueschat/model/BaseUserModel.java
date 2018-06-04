package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class BaseUserModel {
    @SerializedName("_id")
    public String id;
    @SerializedName("username")
    public String userName;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n******User******\n");
        stringBuffer.append("\n id : "+id);
        stringBuffer.append("\n username : "+userName);
        stringBuffer.append("\n----User-----\n");
        return stringBuffer.toString();
    }

}
