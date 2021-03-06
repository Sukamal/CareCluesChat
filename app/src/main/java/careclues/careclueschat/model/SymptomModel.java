package careclues.careclueschat.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/14/2018.
 */

public class SymptomModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;

}
