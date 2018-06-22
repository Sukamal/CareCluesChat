package careclues.careclueschat.storage.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Dao
public interface RoomDao {

    @Query("SELECT * FROM room")
    List<RoomEntity> getAll();

    @Query("SELECT * FROM room WHERE id = :Id")
    RoomEntity findById(String Id);

    @Query("SELECT * FROM room LIMIT :startCount,:endCount")
    List<RoomEntity> getRoom(int startCount, int endCount);

    @Query("SELECT * FROM room order by updatedAt desc LIMIT :startCount,:endCount")
    List<RoomEntity> getLastUpdatedRoom(int startCount, int endCount);

    @Query("select * from room where id in (Select roomId from subscription where open=1) order by updatedAt desc")
    List<RoomEntity> getOpenRoomList();

    @Query("select * from room where id in (Select roomId from subscription where open=0) order by updatedAt desc LIMIT :startCount,:endCount")
    List<RoomEntity> getClosedRoomList(int startCount, int endCount);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<RoomEntity> entityList);

    @Update
    void update(RoomEntity entity);

    @Delete
    void delete(RoomEntity entity);

}
