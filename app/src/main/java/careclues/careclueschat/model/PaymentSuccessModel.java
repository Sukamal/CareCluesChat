package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentSuccessModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("reference_id")
    @Expose
    public String referenceId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("gratification_transaction")
    @Expose
    public Boolean gratificationTransaction;
    @SerializedName("wallet_transaction")
    @Expose
    public Boolean walletTransaction;
    @SerializedName("cash_transaction")
    @Expose
    public Boolean cashTransaction;
    @SerializedName("payment_transaction")
    @Expose
    public Boolean paymentTransaction;
    @SerializedName("purpose")
    @Expose
    public String purpose;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("mode_of_payment")
    @Expose
    public String modeOfPayment;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
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
