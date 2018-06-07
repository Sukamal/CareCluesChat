package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/8/2018.
 */

public class GroupModel {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("t")
    @Expose
    public String t;
    @SerializedName("usernames")
    @Expose
    public List<String> usernames = null;
    @SerializedName("msgs")
    @Expose
    public Integer msgs;
    @SerializedName("u")
    @Expose
    public BaseUserModel user;
    @SerializedName("ts")
    @Expose
    public String ts;
    @SerializedName("ro")
    @Expose
    public Boolean ro;
    @SerializedName("sysMes")
    @Expose
    public Boolean sysMes;
    @SerializedName("_updatedAt")
    @Expose
    public String updatedAt;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nid "+id);
        stringBuffer.append("\nname "+name);
        stringBuffer.append("\nusernames "+usernames);
        stringBuffer.append("\nsysMes "+sysMes);
        return stringBuffer.toString();
    }
}
