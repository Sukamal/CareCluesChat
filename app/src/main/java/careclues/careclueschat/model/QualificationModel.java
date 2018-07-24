package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/24/2018.
 */

public class QualificationModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("degree")
    @Expose
    public String degree;
    @SerializedName("specialty")
    @Expose
    public String specialty;
    @SerializedName("college")
    @Expose
    public String college;
    @SerializedName("year_of_passing")
    @Expose
    public Integer yearOfPassing;
    @SerializedName("additional_information")
    @Expose
    public String additionalInformation;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
}
