package careclues.careclueschat.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RoomAdapterModel implements Comparable<RoomAdapterModel>{

    @SerializedName("_id")
    public String Id;
    @SerializedName("description")
    public String description;
    @SerializedName("name")
    public String name;
    @SerializedName("userName")
    public String userName;
    @SerializedName("update")
    public Date updatedAt;


    @Override
    public int compareTo(@NonNull RoomAdapterModel roomAdapterModel) {
        if(updatedAt != null && roomAdapterModel.updatedAt!= null){
            if(updatedAt.equals(roomAdapterModel.updatedAt))
                return 0;
            else if(updatedAt.before(roomAdapterModel.updatedAt))
                return 1;
            else
                return -1;
        } else
            return -1;
    }
}
