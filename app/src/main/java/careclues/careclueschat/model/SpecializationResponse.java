package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpecializationResponse {

    @SerializedName("data")
    @Expose
    private List<SpecializationModel> data = null;

    public List<SpecializationModel> getData() {
        return data;
    }

    public void setData(List<SpecializationModel> data) {
        this.data = data;
    }
}
