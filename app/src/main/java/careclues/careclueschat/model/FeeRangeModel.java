package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeRangeModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("minimum")
    @Expose
    private String minimum;
    @SerializedName("maximum")
    @Expose
    private String maximum;
    @SerializedName("no_maximum_limit")
    @Expose
    private Boolean noMaximumLimit;
    @SerializedName("links")
    @Expose
    private List<LinkModel> links = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public Boolean getNoMaximumLimit() {
        return noMaximumLimit;
    }

    public void setNoMaximumLimit(Boolean noMaximumLimit) {
        this.noMaximumLimit = noMaximumLimit;
    }

    public List<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(List<LinkModel> links) {
        this.links = links;
    }

}

