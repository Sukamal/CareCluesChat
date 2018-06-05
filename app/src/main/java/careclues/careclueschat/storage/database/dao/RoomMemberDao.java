package careclues.careclueschat.storage.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Dao
public interface RoomMemberDao {

    @Query("SELECT * FROM roomMember")
    List<RoomMemberEntity> getAll();

    @Query("SELECT * FROM roomMember WHERE id = :Id")
    RoomMemberEntity findById(String Id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<RoomMemberEntity> entityList);

    @Update
    void update(RoomMemberEntity entity);

    @Delete
    void delete(RoomMemberEntity entity);

}
