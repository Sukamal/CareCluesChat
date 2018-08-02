package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacilityResponse extends ApiResponse {

    @SerializedName("data")
    @Expose
    private List<FacilityModel> data = null;

    public List<FacilityModel> getData() {
        return data;
    }

    public void setData(List<FacilityModel> data) {
        this.data = data;
    }

}
