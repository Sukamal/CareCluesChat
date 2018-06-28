package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

public class CcBaseUser {

    @SerializedName("_id")
    public String id;
    @SerializedName("username")
    public String userName;
}
