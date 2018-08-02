package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MembershipModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("affiliation")
    @Expose
    private String affiliation;
    @SerializedName("links")
    @Expose
    private List<LinkModel> links = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public List<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(List<LinkModel> links) {
        this.links = links;


    }

}