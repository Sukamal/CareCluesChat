package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePaymentModeResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public TextConsultantModel data;
}
