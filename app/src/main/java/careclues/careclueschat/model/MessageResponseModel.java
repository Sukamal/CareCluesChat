package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class MessageResponseModel {

    @SerializedName("messages")
    public List<MessageModel> messages = null;
    @SerializedName("success")
    public Boolean success;
}
