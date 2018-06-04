package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 27-12-2017.
 */

public class PostsModel {

    @SerializedName("userId")
    public int userId;
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("body")
    public String body;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n userId : " +userId);
        stringBuffer.append("\n id : " +id);
        stringBuffer.append("\n title : " +title);
        stringBuffer.append("\n userId : " +userId);
        stringBuffer.append("\n body : " +body);

        return stringBuffer.toString();
    }
}
