package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseBaseModel {

    @SerializedName("meta")
    @Expose
    public MetaModel meta;

    @SerializedName("links")
    @Expose
    public List<LinkModel> pagelinks;
}
