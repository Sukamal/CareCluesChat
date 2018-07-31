package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TextConsultantListResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public List<TextConsultantModel> data;
}
