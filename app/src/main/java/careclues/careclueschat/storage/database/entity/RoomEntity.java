package careclues.careclueschat.storage.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


import java.util.Date;

import careclues.careclueschat.model.BaseUserModel;
import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.util.RoomUserTypeConverter;

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

    @TypeConverters(RoomUserTypeConverter.class)
    @ColumnInfo(name = "user")
    public RoomUserModel user;

//    @ColumnInfo(name = "userId")
//    public String userId;
//
//    @ColumnInfo(name = "userName")
//    public String userName;

    @ColumnInfo(name = "roomName")
    public  String roomName;

    @ColumnInfo(name = "roomFname")
    public  String roomFname;

    @ColumnInfo(name = "topic")
    public String topic;

    @ColumnInfo(name = "updatedAt")
//    public String updatedAt;
    public Date updatedAt;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "readOnly")
    public Boolean readOnly;





}
