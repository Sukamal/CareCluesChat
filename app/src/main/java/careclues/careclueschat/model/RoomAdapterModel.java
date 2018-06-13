package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RoomAdapterModel {

    @SerializedName("_id")
    public String Id;
    @SerializedName("description")
    public String description;
    @SerializedName("name")
    public String name;

    @SerializedName("update")
    public Date updatedAt;


}
