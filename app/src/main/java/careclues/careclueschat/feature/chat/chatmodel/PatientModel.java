package careclues.careclueschat.feature.chat.chatmodel;

import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class PatientModel {
    @SerializedName("self")
    public Boolean self;
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("gender")
    public String gender;
    @SerializedName("link")
    public String lLink;
    @SerializedName("age")
    public int age;
    @SerializedName("languagePreferredExists")
    public Boolean languagePreferredExists;


//    @Ignore
    public String displayName;
}
