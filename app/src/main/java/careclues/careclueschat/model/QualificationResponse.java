package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QualificationResponse extends ApiResponse {

    @SerializedName("data")
    @Expose
    private List<QualificationModel> data = null;

    public List<QualificationModel> getData() {
        return data;
    }

    public void setData(List<QualificationModel> data) {
        this.data = data;
    }

}


