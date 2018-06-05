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

//testcommit
@Dao
public interface SubscriptionDao {

    @Query("SELECT * FROM subscription")
    List<SubscriptionEntity> getAll();

    @Query("SELECT * FROM subscription WHERE id = :Id")
    SubscriptionEntity findById(String Id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<SubscriptionEntity> entityList);

    @Query("SELECT * FROM subscription LIMIT :startCount,:endCount")
    List<SubscriptionEntity> getSubscripttion(int startCount, int endCount);

    @Update
    void update(SubscriptionEntity entity);

    @Delete
    void delete(SubscriptionEntity entity);

}
