package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("mobile_number")
    @Expose
    public String mobileNumber;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("date_of_birth")
    @Expose
    public String dateOfBirth;
    @SerializedName("chat_user_id")
    @Expose
    public String chatUserId;
    @SerializedName("chat_user_name")
    @Expose
    public String chatUserName;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("sms_enabled")
    @Expose
    public Boolean smsEnabled;
    @SerializedName("email_enabled")
    @Expose
    public Boolean emailEnabled;
    @SerializedName("alert_enabled")
    @Expose
    public Boolean alertEnabled;
    @SerializedName("email_verified")
    @Expose
    public Boolean emailVerified;
    @SerializedName("phone_verified")
    @Expose
    public Boolean phoneVerified;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
    @SerializedName("blood_group")
    @Expose
    public String bloodGroup;
    @SerializedName("relationship")
    @Expose
    public String relationship;





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
