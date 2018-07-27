package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateOtpResponseModel extends ServerResponseBaseModel{

    @SerializedName("data")
    @Expose
    public ValidateOtpResponseModel data;
}
