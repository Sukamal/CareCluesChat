
package careclues.careclueschat.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("reviewer")
    @Expose
    private ReviewerModel reviewer;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("links")
    @Expose
    private List<LinkModel> links = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ReviewerModel getReviewer() {
        return reviewer;
    }

    public void setReviewer(ReviewerModel reviewer) {
        this.reviewer = reviewer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(List<LinkModel> links) {
        this.links = links;
    }

}

