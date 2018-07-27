package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 7/27/2018.
 */

public class UpdatePaymentModeRequest {

    @SerializedName("mode_of_payment")
    @Expose
    public String mode_of_payment;
}
