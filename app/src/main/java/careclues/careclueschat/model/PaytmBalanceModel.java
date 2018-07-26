package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/26/2018.
 */

public class PaytmBalanceModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("wallet_balance")
    @Expose
    public Double walletBalance;
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
