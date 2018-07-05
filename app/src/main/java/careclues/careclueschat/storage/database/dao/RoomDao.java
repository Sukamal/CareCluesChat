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

//    @Query("SELECT * FROM room order by updatedAt desc LIMIT :startCount,:endCount")
//    List<RoomEntity> getLastUpdatedRoom(int startCount, int endCount);
//
//    @Query("select * from room where id in (Select roomId from subscription where open=1) order by updatedAt desc")
//    List<RoomEntity> getOpenRoomList();
//
//    @Query("select * from room where id in (Select roomId from subscription where open=0) order by updatedAt desc LIMIT :startCount,:endCount")
//    List<RoomEntity> getClosedRoomList(int startCount, int endCount);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RoomEntity> entityList);

    @Update
    void update(RoomEntity entity);

    @Delete
    void delete(RoomEntity entity);

    @Query("select roo.* from room roo inner join subscription sub on sub.roomId = roo.id and roo.readOnly=0 and sub.open=1 order by updatedAt desc")
    List<RoomEntity> getActiveRoomList();


    @Query("select ro.* from room ro where ro.id not in (select roo.id from room roo inner join subscription sub on sub.roomId = roo.id and roo.readOnly=0 and sub.open=1 ) order by updatedAt desc LIMIT :startCount,:endCount")
    List<RoomEntity> getNextRoomList(int startCount, int endCount);


//    @Query("select roo.*, (select updatedAt msgTime from message where rId = roo.id order by updatedAt desc limit 1)msgTime from room roo inner join subscription sub on sub.roomId = roo.id and roo.readOnly=0 and sub.open=1 order by updatedAt desc")
//    List<RoomListModel> getActiveRoomList1();
//
//
//    @Query("select ro.*, (select updatedAt msgTime from message where rId = ro.id order by updatedAt desc limit 1)msgTime from room ro where ro.id not in (select roo.id from room roo inner join subscription sub on sub.roomId = roo.id and roo.readOnly=0 and sub.open=1 ) order by updatedAt desc LIMIT :startCount,:endCount")
//    List<RoomListModel> getNextRoomList1(int startCount, int endCount);







//    SELECT column_name(s)
//    FROM table1
//    INNER JOIN table2 ON table1.column_name = table2.column_name;

}
