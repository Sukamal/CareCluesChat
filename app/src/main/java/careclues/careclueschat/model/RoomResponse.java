package careclues.careclueschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import careclues.careclueschat.model.RoomModel;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class RoomResponse {

    @SerializedName("update")
    private List<RoomModel> update = null;
    @SerializedName("remove")
    private List<Object> remove = null;
    @SerializedName("success")
    private Boolean success;

    public List<RoomModel> getUpdate() {
        return update;
    }

    public void setUpdate(List<RoomModel> update) {
        this.update = update;
    }

    public List<Object> getRemove() {
        return remove;
    }

    public void setRemove(List<Object> remove) {
        this.remove = remove;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        for(RoomModel model :update )
            stringBuffer.append(model.toString());

        return stringBuffer.toString();
    }
}
