package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/24/2018.
 */

public class PhysicianModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("prefix")
    @Expose
    public String prefix;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("date_of_birth")
    @Expose
    public String dateOfBirth;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("years_of_experience")
    @Expose
    public Integer yearsOfExperience;
    @SerializedName("professional_statement")
    @Expose
    public String professionalStatement;
    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("reviews_count")
    @Expose
    public Integer reviewsCount;
    @SerializedName("qualifications")
    @Expose
    public List<QualificationModel> qualifications = null;
    @SerializedName("specializations")
    @Expose
    public List<SpecializationModel> specializations = null;
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
