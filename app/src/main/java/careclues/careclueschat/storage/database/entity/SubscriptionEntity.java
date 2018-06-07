package careclues.careclueschat.storage.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;


import java.util.Date;

import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.util.RoomUserTypeConverter;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Entity(tableName = "subscription")
public class SubscriptionEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    public String Id;

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
//
//    @ColumnInfo(name = "name")
//    public  String name;

    @ColumnInfo(name = "fName")
    public  String fName;

    @ColumnInfo(name = "roomId")
    public String rId;

    @ColumnInfo(name = "timeStamp")
//    public String timeStamp;
    public Date timeStamp;

    @ColumnInfo(name = "lastSeen")
    public String lastSeen;

    @ColumnInfo(name = "open")
    public Boolean open;

    @ColumnInfo(name = "alert")
    public Boolean alert;

    @ColumnInfo(name = "updatedAt")
//    public String updatedAt;
    public Date updatedAt;

    @ColumnInfo(name = "unread")
    public Integer unread;

    @ColumnInfo(name = "userMentions")
    public Integer userMentions;

    @ColumnInfo(name = "groupMentions")
    public Integer groupMentions;
}
