package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmBalanceResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public PaytmBalanceModel data;
}
