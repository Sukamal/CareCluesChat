package careclues.careclueschat.storage.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import careclues.careclueschat.model.BaseUserModel;
import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.model.UrlModel;
import careclues.careclueschat.util.RoomUserTypeConverter;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Entity(tableName = "message")
public class MessageEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    public String Id;

    @ColumnInfo(name = "rId")
    public String rId;

    @ColumnInfo(name = "msg")
    public String msg;

    @ColumnInfo(name = "ts")
    public String timeStamp;

    @ColumnInfo(name = "user")
    @TypeConverters(RoomUserTypeConverter.class)
    public RoomUserModel user;

    @ColumnInfo(name = "updatedAt")
    public String updatedAt;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "alias")
    public String alias;

    @ColumnInfo(name = "groupable")
    public Boolean groupable;

//    @ColumnInfo(name = "urls")
//    public List<UrlModel> urls = null;

//    @ColumnInfo(name = "mentions")
//    @TypeConverters(RoomUserTypeConverter.class)
//    public List<BaseUserModel> mentions;

//    @ColumnInfo(name = "attachments")
//    public List<Object> attachments = null;

    @ColumnInfo(name = "parseUrls")
    public Boolean parseUrls;

    @ColumnInfo(name = "meta")
    public String meta;
}
