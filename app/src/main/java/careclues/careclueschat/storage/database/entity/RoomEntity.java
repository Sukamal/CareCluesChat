package careclues.careclueschat.storage.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Entity(tableName = "room")
public class RoomEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    public String roomId;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "userId")
    public String userId;

    @ColumnInfo(name = "userName")
    public String userName;

    @ColumnInfo(name = "roomName")
    public  String roomName;

    @ColumnInfo(name = "roomFname")
    public  String roomFname;

    @ColumnInfo(name = "topic")
    public String topic;

    @ColumnInfo(name = "updatedAt")
    public String updatedAt;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "readOnly")
    public Boolean readOnly;





}
