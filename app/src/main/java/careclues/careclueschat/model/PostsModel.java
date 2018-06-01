package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 27-12-2017.
 */

public class PostsModel {

    @SerializedName("userId")
    private int userId;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


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
