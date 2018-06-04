package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class MessageModel extends BaseMessageModel{

    @SerializedName("groupable")
    public Boolean groupable;

    @SerializedName("urls")
    public List<UrlModel> urls = null;

    @SerializedName("mentions")
    public List<BaseUserModel> mentions;

//    @SerializedName("attachments")
//    public List<Object> attachments = null;

    @SerializedName("parseUrls")
    public Boolean parseUrls;

    @SerializedName("meta")
    public String meta;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n\n---------------Message Start-------------------\n\n");
        stringBuffer.append("\nid : "+id);
        stringBuffer.append("\nroomId : "+rId);
        stringBuffer.append("\nmsg : "+msg);
        stringBuffer.append("\ntimeStamp : "+timeStamp);
        stringBuffer.append("\nuser : "+user.toString());
        stringBuffer.append("\nupdatedAt : "+updatedAt);
        stringBuffer.append("\ntype : "+type);
        stringBuffer.append("\nalias : "+alias);
        stringBuffer.append("\ngroupable : "+groupable);
        stringBuffer.append("\nurls : "+urls);
        stringBuffer.append("\nmentions : "+mentions);
        stringBuffer.append("\nparseUrls : "+parseUrls);
        stringBuffer.append("\nmeta : "+meta);
        stringBuffer.append("\n\n---------------Message End-------------------\n\n");
        return stringBuffer.toString();
    }

}
