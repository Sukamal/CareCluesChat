package careclues.careclueschat.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceResponse extends ApiResponse{

    @SerializedName("data")
    @Expose
    private List<ServiceModel> data = null;

    public List<ServiceModel> getData() {
        return data;
    }

    public void setData(List<ServiceModel> data) {
        this.data = data;
    }

}