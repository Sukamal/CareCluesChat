package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSuccessResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public PaymentSuccessModel data;


}
