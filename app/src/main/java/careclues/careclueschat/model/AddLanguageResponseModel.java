package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddLanguageResponseModel extends ServerResponseBaseModel{

    @SerializedName("data")
    @Expose
    public SymptomModel data;

}
