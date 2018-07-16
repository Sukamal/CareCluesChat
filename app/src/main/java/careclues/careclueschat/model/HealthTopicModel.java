package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/14/2018.
 */

public class HealthTopicModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("for_male")
    @Expose
    public Boolean forMale;
    @SerializedName("for_female")
    @Expose
    public Boolean forFemale;
    @SerializedName("phone_consultation_availability")
    @Expose
    public Boolean phoneConsultationAvailability;
    @SerializedName("text_consultation_availability")
    @Expose
    public Boolean textConsultationAvailability;
    @SerializedName("home_consultation_availability")
    @Expose
    public Boolean homeConsultationAvailability;
    @SerializedName("common")
    @Expose
    public Boolean common;
    @SerializedName("alternate_medicine")
    @Expose
    public Boolean alternateMedicine;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
}
