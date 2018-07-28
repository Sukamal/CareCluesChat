package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/22/2018.
 */

public class TextConsultantModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("internet_handling_fee")
    @Expose
    public String internetHandlingFee;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
    @SerializedName("duration")
    @Expose
    public Integer duration;
    @SerializedName("paid")
    @Expose
    public Boolean paid;
    @SerializedName("expires_at")
    @Expose
    public String expiresAt;
    @SerializedName("allows_general_physician")
    @Expose
    public Boolean allowsGeneralPhysician;
    @SerializedName("chat_conversation_id")
    @Expose
    public String chatConversationId;
    @SerializedName("later_payment")
    @Expose
    public Boolean laterPayment;
    @SerializedName("free_consultation")
    @Expose
    public Boolean freeConsultation;
    @SerializedName("tax_amount")
    @Expose
    public String taxAmount;
    @SerializedName("tax_rate")
    @Expose
    public String taxRate;
    @SerializedName("default_discount_amount")
    @Expose
    public String defaultDiscountAmount;
    @SerializedName("discount_amount_by_discount_code")
    @Expose
    public String discountAmountByDiscountCode;
    @SerializedName("discount_amount")
    @Expose
    public String discountAmount;

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
