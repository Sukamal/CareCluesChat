package careclues.careclueschat.storage.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;

/**
 * Created by SukamalD on 6/3/2018.
 */

@Dao
public interface MessageDao {

    @Query("SELECT * FROM message")
    List<MessageEntity> getAll();

    @Query("SELECT * FROM message WHERE id = :Id")
    MessageEntity findById(String Id);

    @Query("SELECT * FROM message where rId = :Id order by updatedAt desc limit :count")
    List<MessageEntity> getChatMessage(String Id, int count);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<MessageEntity> entityList);

    @Update
    void update(MessageEntity entity);

    @Delete
    void delete(MessageEntity entity);

}
