package careclues.careclueschat.storage.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Entity(tableName = "roomMember",primaryKeys={"id","rId"})
public class RoomMemberEntity {

    @ColumnInfo(name = "id")
    @NonNull
    public String Id;

    @NonNull
    @ColumnInfo(name = "rId")
    public String rId;

    @ColumnInfo(name = "userName")
    public String userName;

    @ColumnInfo(name = "name")
    public  String name;

    @ColumnInfo(name = "status")
    public  String status;

    @ColumnInfo(name = "utcOffset")
    public Double utcOffset;


}
