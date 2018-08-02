package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse extends ApiResponse {

    @SerializedName("data")
    @Expose
    private List<ReviewModel> data = null;

    public List<ReviewModel> getData() {
        return data;
    }

    public void setData(List<ReviewModel> data) {
        this.data = data;
    }
}
