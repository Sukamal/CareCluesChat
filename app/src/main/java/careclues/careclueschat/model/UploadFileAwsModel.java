package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadFileAwsModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("pending")
    @Expose
    public Boolean pending;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;

    public String getLink(String key){
        String retVal = null;
        if(links != null){
            for(LinkModel linkModel : links){
                if(linkModel.rel.equals(key)){
                    retVal = linkModel.href;
                    return retVal;
                }
            }
        }
        return retVal;
    }
}
