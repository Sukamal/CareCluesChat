package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadFileResponseModel extends ServerResponseBaseModel{
    @SerializedName("data")
    @Expose
    public UploadFileAwsModel data;
}
