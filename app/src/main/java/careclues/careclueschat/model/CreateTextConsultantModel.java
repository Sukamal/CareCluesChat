package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 7/21/2018.
 */

public class CreateTextConsultantModel {
    @SerializedName("health_topic_id")
    @Expose
    public int health_topic_id ;
    @SerializedName("chat_conversation_id")
    @Expose
    public String chat_conversation_id ;
//    @SerializedName("minimum_payable_amount")
//    @Expose
//    public String minimum_payable_amount;
}
