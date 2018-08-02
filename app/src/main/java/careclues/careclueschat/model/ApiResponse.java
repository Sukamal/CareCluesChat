package careclues.careclueschat.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("meta")
    @Expose
    private MetaModel meta;

    @SerializedName("links")
    @Expose
    private List<LinkModel> links = null;

    public MetaModel getMeta() {
        return meta;
    }

    public void setMeta(MetaModel meta) {
        this.meta = meta;
    }

    public List<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(List<LinkModel> links) {
        this.links = links;
    }

}
