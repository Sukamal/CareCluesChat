package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageResponse {
    @SerializedName("data")
    @Expose
    private List<LanguageModel> data = null;

    public List<LanguageModel> getData() {
        return data;
    }

    public void setData(List<LanguageModel> data) {
        this.data = data;
    }

}
