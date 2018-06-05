package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;
import com.rocketchat.common.data.model.*;
import com.rocketchat.common.data.model.User;
import com.rocketchat.core.model.Url;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class BaseMessageModel {

    @SerializedName("_id")
    public String id;

    @SerializedName("rid")
    public String rId;

    @SerializedName("msg")
    public String msg;

    @SerializedName("ts")
    public String timeStamp;

    @SerializedName("u")
    public RoomUserModel user;

    @SerializedName("_updatedAt")
    public String updatedAt;

    @SerializedName("t")
    public String type;

    @SerializedName("alias")
    public String alias;
}
