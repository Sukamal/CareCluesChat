package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/27/2018.
 */

public class OtpModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("response_code")
    @Expose
    public String responseCode;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
}
