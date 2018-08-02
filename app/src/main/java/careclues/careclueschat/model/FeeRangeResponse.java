package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeRangeResponse {

    @SerializedName("data")
    @Expose
    private List<FeeRangeModel> data = null;

    public List<FeeRangeModel> getData() {
        return data;
    }

    public void setData(List<FeeRangeModel> data) {
        this.data = data;
    }
}
