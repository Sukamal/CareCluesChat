package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthTopicResponseModel {
    @SerializedName("meta")
    @Expose
    public MetaModel meta;
    @SerializedName("data")
    @Expose
    public List<HealthTopicModel> data;
    @SerializedName("links")
    @Expose
    public List<LinkModel> pagelinks;
}
