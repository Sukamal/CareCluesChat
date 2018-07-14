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
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("for_male")
    @Expose
    private Boolean forMale;
    @SerializedName("for_female")
    @Expose
    private Boolean forFemale;
    @SerializedName("phone_consultation_availability")
    @Expose
    private Boolean phoneConsultationAvailability;
    @SerializedName("text_consultation_availability")
    @Expose
    private Boolean textConsultationAvailability;
    @SerializedName("home_consultation_availability")
    @Expose
    private Boolean homeConsultationAvailability;
    @SerializedName("common")
    @Expose
    private Boolean common;
    @SerializedName("alternate_medicine")
    @Expose
    private Boolean alternateMedicine;
    @SerializedName("links")
    @Expose
    private List<LinkModel> links = null;
}
