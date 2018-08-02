package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MembershipResponse extends ApiResponse {

    @SerializedName("data")
    @Expose
    private List<MembershipModel> data = null;

    public List<MembershipModel> getData() {
        return data;
    }

    public void setData(List<MembershipModel> data) {
        this.data = data;
    }
}
