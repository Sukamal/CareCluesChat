package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/24/2018.
 */

public class SpecializationModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("subspecialty")
    @Expose
    public String subspecialty;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
}
