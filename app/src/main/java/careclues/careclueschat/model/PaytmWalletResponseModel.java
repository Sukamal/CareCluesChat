package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmWalletResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public PaytmWalletModel data;
}
