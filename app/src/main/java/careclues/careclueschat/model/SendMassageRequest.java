package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/21/2018.
 */

public class SendMassageRequest {

    @SerializedName("message")
    @Expose
    public MassageRequest message;

    public SendMassageRequest (String id, String rId,String msg){
        message = new MassageRequest(id,rId,msg);
    }
}
