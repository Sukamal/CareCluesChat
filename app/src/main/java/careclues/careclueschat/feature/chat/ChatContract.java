package careclues.careclueschat.feature.chat;


import java.io.File;
import java.util.List;

import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.ReplyMessageModel;
import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.LanguageModel;
import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.model.SymptomModel;
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
        public void displayPrimarySymptom(List<SymptomModel> data);
        public void displaySymptom(List<SymptomModel> data);
        public void displayLanguage(List<LanguageModel> data);
        public void displayOptions(List<String> data);
        public void displayNothing();
        public void displayTextInput();
        public void displayPayFee();
        public void onUpdateLanguageToServer(String languages);
        public void onUpdateImageToServer(String url);

    }

    interface presenter {
        public void loadData(int startcount,int endcount);
        public void sendMessage(String msg);
        public void sendMessageViaApi(ReplyMessageModel replyMessageModel, String controlType);
        public void uploadFile(File file,String desc);
        public void reconnectToServer();
        public void disconnectToServer();
        public void addLanguageApiCall(String languages);
        public void createTextConsultant(String url, String topicId, String roomId);
        public void getTextConsultant(String roomId);

    }
}
