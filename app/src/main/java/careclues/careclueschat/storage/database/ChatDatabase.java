package careclues.careclueschat.storage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import careclues.careclueschat.storage.database.dao.MessageDao;
import careclues.careclueschat.storage.database.dao.RoomDao;
import careclues.careclueschat.storage.database.dao.RoomMemberDao;
import careclues.careclueschat.storage.database.dao.SubscriptionDao;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.RoomUserTypeConverter;

/**
 * Created by SukamalD on 6/3/2018.
 */

@TypeConverters({RoomUserTypeConverter.class})
@Database(version = AppConstant.DATABASE_VERSION,exportSchema = false,
        entities = {RoomEntity.class, SubscriptionEntity.class, RoomMemberEntity.class, MessageEntity.class})
public abstract class ChatDatabase extends RoomDatabase{

    public abstract RoomDao roomDao();
    public abstract SubscriptionDao subscriptionDao();
    public abstract RoomMemberDao roomMemberDao();
    public abstract MessageDao messageDao();

}
