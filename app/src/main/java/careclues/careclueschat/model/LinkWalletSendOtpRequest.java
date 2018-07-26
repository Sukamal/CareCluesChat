package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 7/27/2018.
 */

public class LinkWalletSendOtpRequest {

    @SerializedName("mobile_number")
    @Expose
    public int mobileNo;
}
