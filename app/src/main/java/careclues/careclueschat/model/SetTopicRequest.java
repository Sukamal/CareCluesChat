package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/21/2018.
 */

public class SetTopicRequest {

    @SerializedName("roomId")
    public String roomId;

    @SerializedName("topic")
    public String topic;


   public SetTopicRequest(String roomId,String topic){
       this.roomId = roomId;
       this.topic = topic;
   }

}
