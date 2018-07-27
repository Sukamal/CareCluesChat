package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 7/27/2018.
 */

public class ValidateOtpModel {

    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("expires_at")
    @Expose
    public String expiresAt;
    @SerializedName("scope")
    @Expose
    public String scope;
    @SerializedName("resource_owner_id")
    @Expose
    public String resourceOwnerId;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
}
