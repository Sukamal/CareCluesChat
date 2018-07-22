package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TextConsultantResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public TextConsultantModel data;
}
