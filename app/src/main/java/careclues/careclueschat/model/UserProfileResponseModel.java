package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileResponseModel {
    @SerializedName("meta")
    @Expose
    public MetaModel meta;
    @SerializedName("data")
    @Expose
    public DataModel data;
}
