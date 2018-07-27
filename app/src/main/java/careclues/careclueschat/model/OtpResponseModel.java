package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpResponseModel extends ServerResponseBaseModel{

    @SerializedName("data")
    @Expose
    public OtpModel data;
}
