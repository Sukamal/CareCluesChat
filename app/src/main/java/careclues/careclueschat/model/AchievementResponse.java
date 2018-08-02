package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AchievementResponse extends ApiResponse {

    @SerializedName("data")
    @Expose
    private List<AchievementModel> data = null;

    public List<AchievementModel> getData() {
        return data;
    }

    public void setData(List<AchievementModel> data) {
        this.data = data;
    }
}
