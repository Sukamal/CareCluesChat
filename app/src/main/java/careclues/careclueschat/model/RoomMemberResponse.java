package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class RoomMemberResponse {

    @SerializedName("members")
    public List<RoomMemberModel> members = null;
    @SerializedName("count")
    public Integer count;
    @SerializedName("offset")
    public Integer offset;
    @SerializedName("total")
    public Integer total;
    @SerializedName("success")
    public Boolean success;


}
