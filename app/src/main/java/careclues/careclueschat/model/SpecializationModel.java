package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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

//    private Integer id;
//    @SerializedName("subspecialty")
//    @Expose
//    private String subspecialty;
//    @SerializedName("slug")
//    @Expose
//    private String slug;
//    @SerializedName("links")
//    @Expose
//    private List<LinkModel> links = null;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getSubspecialty() {
//        return subspecialty;
//    }
//
//    public void setSubspecialty(String subspecialty) {
//        this.subspecialty = subspecialty;
//    }
//
//    public String getSlug() {
//        return slug;
//    }
//
//    public void setSlug(String slug) {
//        this.slug = slug;
//    }
//
//    public List<LinkModel> getLinks() {
//        return links;
//    }
//
//    public void setLinks(List<LinkModel> links) {
//        this.links = links;
//    }

}
