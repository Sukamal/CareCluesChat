package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SymptomResponseModel {
    @SerializedName("meta")
    @Expose
    public MetaModel meta;
    @SerializedName("data")
    @Expose
    public List<SymptomModel> data;
    @SerializedName("links")
    @Expose
    public List<LinkModel> pagelinks;
}
