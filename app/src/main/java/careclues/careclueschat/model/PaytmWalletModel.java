package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/25/2018.
 */

public class PaytmWalletModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("links")
    @Expose
    private List<LinkModel> links = null;

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
