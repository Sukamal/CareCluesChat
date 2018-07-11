package careclues.careclueschat.feature.chat.chatmodel;

import com.google.gson.annotations.SerializedName;

public class PatientModel {
    @SerializedName("self")
    public Boolean self;
    @SerializedName("firstName")
    public String first_name;
    @SerializedName("lastName")
    public String last_name;
    @SerializedName("gender")
    public String gender;
    @SerializedName("link")
    public String lLink;
}
