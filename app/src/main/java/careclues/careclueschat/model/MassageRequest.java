package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/21/2018.
 */

public class MassageRequest {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("rid")
    @Expose
    public String rid;
    @SerializedName("msg")
    @Expose
    public String msg;

    public MassageRequest(String id, String rId,String msg){
        this.id = id;
        this.rid = rId;
        this.msg = msg;
    }
}
