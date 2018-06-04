package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class SubscriptionResponse {

    @SerializedName("update")
    public List<SubscriptionModel> update = null;
    @SerializedName("remove")
    public List<Object> remove = null;
    @SerializedName("success")
    public Boolean success;

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        for(SubscriptionModel model :update )
            stringBuffer.append(model.toString());

        return stringBuffer.toString();
    }

}
