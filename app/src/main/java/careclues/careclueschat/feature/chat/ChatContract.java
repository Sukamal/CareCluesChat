package careclues.careclueschat.feature.chat;


import java.io.File;
import java.util.List;

import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.network.ServiceCallBack;

public interface ChatContract {

    interface view extends CommonViewInterface {

        public void displyNextScreen();
        public void displayChatList(List<ChatMessageModel> list);
        public void displayMoreChatList(List<ChatMessageModel> list);
        public void displyTypingStatus(String message);
        public void onConnectionFaild(int errorType);
        public void displayFamilyMember(List<DataModel> data);
        public void displayHealthTopic(List<HealthTopicModel> data);
        public void displayTextInput();
    }

    interface presenter {
        public void loadData(int count);
        public void sendMessage(String msg);
        public void uploadFile(File file,String desc);
        public void reconnectToServer();
        public void disconnectToServer();
    }
}
