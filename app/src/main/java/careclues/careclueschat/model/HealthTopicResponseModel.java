package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthTopicResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public List<HealthTopicModel> data;
}
