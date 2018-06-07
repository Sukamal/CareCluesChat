package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/8/2018.
 */

public class CreateRoomRequest {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("members")
    @Expose
    public String[] members;
    @SerializedName("readOnly")
    @Expose
    public boolean readOnly;

    public CreateRoomRequest( String name,String[] members){
        this.name = name;
        this.members = members;
    }
}
