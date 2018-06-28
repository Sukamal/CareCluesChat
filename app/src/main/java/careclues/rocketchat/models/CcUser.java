package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

public class CcUser extends CcBaseUser {

    @SerializedName("name")
    public String name;
}
