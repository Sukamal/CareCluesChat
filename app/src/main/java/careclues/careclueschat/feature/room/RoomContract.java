package careclues.careclueschat.feature.room;

import com.rocketchat.core.model.Subscription;

import java.util.List;

import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.model.SubscriptionModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;

public interface RoomContract {

    interface view extends CommonViewInterface {
        public void onConnectionFaild(int errorType);
        public void onFetchBasicData();
        public void displyRoomListScreen(List<RoomAdapterModel> list);
        public void displyMoreRoomList(List<RoomAdapterModel> list);
        public void displyNewRoomList(List<RoomAdapterModel> list);
        public void updateRoomMessage(String roomId, MessageEntity msg);
        public void displyChatScreen(String roomId);
        public void onUserTyping(String roomId, String user, Boolean istyping);
    }

    interface presenter {
        public void doLogin(String userId, String password);
        public void doApiLogin(String userId, String password);
        public void reconnectToServer();
        public void disconnectToServer();
        public void getOpenRoom();
        public void getMoreRoom(int startCount, int threshold);
        public void getMessage(String roomId);
        public void createNewRoom();
        public void getTextConsultant(String roomId);



    }
}
