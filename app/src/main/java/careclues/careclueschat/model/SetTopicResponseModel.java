package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class SetTopicResponseModel {

    @SerializedName("topic")
    @Expose
    public String topic;
    @SerializedName("success")
    @Expose
    public Boolean success;

}
