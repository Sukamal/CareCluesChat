package careclues.careclueschat.feature.room;

import com.rocketchat.core.model.Subscription;

import java.util.List;

import careclues.careclueschat.feature.common.CommonViewInterface;

public interface RoomContract {

    interface view extends CommonViewInterface {

        public void onConnectionFaild(int errorType);
        public void displyRoomList(List<Subscription> list);
        public void displyNextScreen();
    }

    interface presenter {
        public void registerConnectivity();
        public void getRoom();
        public void reconnectToServer();
        public void disconnectToServer();
    }
}
