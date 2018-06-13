package careclues.careclueschat.feature.room;

import com.rocketchat.core.model.Subscription;

import java.util.List;

import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.model.SubscriptionModel;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;

public interface RoomContract {

    interface view extends CommonViewInterface {

        public void onConnectionFaild(int errorType);
        public void displyRoomList(List<RoomAdapterModel> list);
        public void displyMoreRoomList(List<RoomAdapterModel> list);
        public void displyNextScreen();
    }

    interface presenter {
        public void registerConnectivity();
        public void getRoom();
        public void getMoreRoom(int startCount, int threshold);
        public void reconnectToServer();
        public void disconnectToServer();
        public void createNewRoom();
    }
}
