package careclues.careclueschat.feature.chat;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.rocketchat.common.utils.Utils;
import com.rocketchat.core.model.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.ReplyMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.ServerMessageModel;
import careclues.careclueschat.model.AddLanguageResponseModel;
import careclues.careclueschat.model.BaseUserModel;
import careclues.careclueschat.model.CreateTextConsultantModel;
import careclues.careclueschat.model.HealthTopicResponseModel;
import careclues.careclueschat.model.LanguageModel;
import careclues.careclueschat.model.LanguageResponseModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.model.FamilyMemberResponseModel;
import careclues.careclueschat.model.SymptomResponseModel;
import careclues.careclueschat.model.TextConsultantResponseModel;
import careclues.careclueschat.model.UserProfileResponseModel;
import careclues.careclueschat.network.ApiClient;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.rocketchat.CcChatRoom;
import careclues.rocketchat.CcRocketChatClient;
import careclues.rocketchat.CcUtils;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.common.CcRocketChatException;
import careclues.rocketchat.models.CcBaseRoom;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcUser;

public class ChatPresenter1 implements ChatContract.presenter {

    private ChatContract.view view;
    private Application application;
    private String userId;
    private RoomMemberEntity userDetails;
    private String roomId;

    private AtomicInteger integer;

    private CcRocketChatClient api;
    private CcChatRoom chatRoom;
    private UserProfileResponseModel userProfileModel;
    private HealthTopicResponseModel healthTopicResponseModel;
    private SymptomResponseModel symptomResponseModel;
    private LanguageResponseModel languageResponseModel;
    private RestApiExecuter apiExecuter;
    private FamilyMemberResponseModel familyMemberResponseModel = null;

    private Timer timer;
    private List<String> addLanguageTasklist = new ArrayList<>();
    private String languageUpdated;



    public ChatPresenter1(ChatContract.view view, String roomId, Application application){
        this.view = view;
        this.application = application;
        this.roomId = roomId;
        integer = new AtomicInteger(1);

        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
        getLoginUserDetails(userId);
        if(CareCluesChatApplication.userProfile != null){
            userProfileModel = CareCluesChatApplication.userProfile;
        }else{
            userProfileModel =  getUserProfile(AppConstant.getUserId());
        }

    }

    private void getLoginUserDetails(final String userId){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                userDetails = ((CareCluesChatApplication)application).getChatDatabase().roomMemberDao().findById(userId);
                initWebSoket();

            }
        });
    }


    private void initWebSoket(){
        api = ((CareCluesChatApplication) application).getRocketChatClient();
        List<CcBaseRoom> rooms = new ArrayList<>();
        CcBaseRoom  baseRoom = new CcBaseRoom() {
            @Nullable
            @Override
            public String name() {
                return null;
            }
        };

        baseRoom.Id = roomId;
        baseRoom.type = CcBaseRoom.RoomType.PRIVATE;
        CcUser ccUser = new CcUser();
        ccUser.id = userDetails.Id;
        ccUser.name = userDetails.userName;
        baseRoom.user = ccUser;

        rooms.add(baseRoom);
        api.getChatRoomFactory().createChatRooms(rooms);

        chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);

    }

    public void deregisterSocket(){
        chatRoom.unSubscribeAllEvents();
    }


    @Override
    public void loadData(int count) {
        getChatHistory(roomId,count);
    }

    @Override
    public void sendMessage(String msg) {
        chatRoom.sendMessage(msg.toString(), new CcMessageCallback.MessageAckCallback() {
            @Override
            public void onMessageAck(CcMessage message) {
                Log.e("Message","Message Send : "+ message.id + " " +message.toString());
            }

            @Override
            public void onError(CcRocketChatException error) {
                Log.e("ERROR Sending Message: ",error.getMessage());
            }
        });

//        getUserProfile("985");
    }

    @Override
    public void sendMessageViaApi(final ReplyMessageModel replyMessageModel, final String controlType) {
        String msg = new Gson().toJson(replyMessageModel);
        Log.v("NEW_MESSAGE : ", msg);

        if(apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();
            apiExecuter.sendNewMessage(CcUtils.shortUUID(),roomId,msg, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
            @Override
            public void onSuccess(MessageResponseModel response) {
                if(controlType.equals(ControlType.CONTROL_HEALTH_TOPIC_SELECT.get())){

                    String url = replyMessageModel.patient.lLink;
                    String categoryLink = replyMessageModel.categoryModel.link;
                    String topicId = categoryLink.substring((categoryLink.lastIndexOf("/")) + 1);
                    createTextConsultant(url,topicId,roomId);
                }
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    @Override
    public void uploadFile(File file,String desc) {
//        chatRoom.uploadFile(file, "test_doc", desc, new FileListener() {
//            @Override
//            public void onUploadStarted(String roomId, String fileName, String description) {
//
//            }
//
//            @Override
//            public void onUploadProgress(int progress, String roomId, String fileName, String description) {
//
//            }
//
//            @Override
//            public void onUploadComplete(int statusCode, FileDescriptor file, String roomId, String fileName, String description) {
//
//            }
//
//            @Override
//            public void onUploadError(RocketChatException error, IOException e) {
//
//            }
//
//            @Override
//            public void onSendFile(Message message, RocketChatException error) {
//
//            }
//        });
    }

    @Override
    public void reconnectToServer() {
        api.getWebsocketImpl().getSocket().reconnect();
    }

    @Override
    public void disconnectToServer() {
        deregisterSocket();
    }



    @Override
    public void addLanguageApiCall(String languages) {
        String[] languageList = languages.split(";");
        String url = null;
        if(userProfileModel != null){
            url = userProfileModel.data.getLink("languages");
        
        if(languageList != null && languageList.length > 0){
            for(int i= 0; i < languageList.length;i++){
                LanguageModel languageModel = new LanguageModel();
                languageModel.name = languageList[i];
                addLanguageTasklist.add(languageList[i]);
                    if(apiExecuter == null)
                        apiExecuter = RestApiExecuter.getInstance();

                    apiExecuter.addUserLanguage(url, languageModel, new ServiceCallBack<AddLanguageResponseModel>(AddLanguageResponseModel.class) {
                        @Override
                        public void onSuccess(AddLanguageResponseModel response) {
                            addLanguageTasklist.remove(response.data.name);
                            if(languageUpdated == null){
                                languageUpdated = response.data.name;
                            }else{
                                languageUpdated = languageUpdated + "\n"+response.data.name;
                            }
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
                }
            checkTaskComplete();
            }
        }
        
    }

    @Override
    public void createTextConsultant(String url, String topicId, String roomId) {
        CreateTextConsultantModel healthTopicModel = new CreateTextConsultantModel();
        healthTopicModel.health_topic_id = topicId;
        healthTopicModel.chat_conversation_id = roomId;

        if(apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();
        apiExecuter.createTextConsultant(url, healthTopicModel, new ServiceCallBack<TextConsultantResponseModel>(TextConsultantResponseModel.class) {
            @Override
            public void onSuccess(TextConsultantResponseModel response) {

            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }


    private void getChatHistory(final String roomId, final int count){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<MessageEntity> messageEntities = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getChatMessage(roomId, count);
                    List<ChatMessageModel> msgList = new ArrayList<>();
                    for (MessageEntity entity : messageEntities) {
                        msgList.add(new ChatMessageModel(entity));
                    }
                    view.displayChatList(msgList);

                } catch (Throwable e) {
                    Log.e("ERROR", "Errorr!!!!"+e.toString());
                }

            }
        });
    }


////    @Override
//    public void onMessage(String roomId, CcMessage message) {
//
//        Log.e("Message"," On Message : "+ message.id + " " +message.toString());
//
////        insertIntoDB(message);
//        List<ChatMessageModel> list = new ArrayList<>();
//        ChatMessageModel chatMessageModel = new ChatMessageModel(message.id,message.msg,new Date(/*message.updatedAt*/),message.user.id);
//        list.add(chatMessageModel);
//        view.displayMoreChatList(list);
//
//    }

//    @Override
//    public void onTyping(String roomId, String user, Boolean istyping) {
//
//        if(istyping){
//            view.displyTypingStatus(user + " is typing...");
//        }else{
//            view.displyTypingStatus("");
//
//        }
//    }

    private void insertIntoDB(Message message){
        final MessageEntity messageEntity = new MessageEntity();
        messageEntity.Id = message.id();
        messageEntity.rId = message.roomId();
        messageEntity.msg = message.message();
        messageEntity.timeStamp = new Date(message.timestamp());
        RoomUserModel userModel = new RoomUserModel();
        userModel.id = message.sender().id();
        userModel.userName = message.sender().username();
        messageEntity.user = userModel;
        messageEntity.updatedAt = new Date(message.updatedAt());
        messageEntity.type = message.type();
        messageEntity.alias = message.senderAlias();
        messageEntity.groupable = message.groupable();
        List<BaseUserModel> mentions = new ArrayList<>();
        if(message.mentions() != null){
            for(int i=0; i <message.mentions().size(); i++){
                BaseUserModel baseUserModel = new BaseUserModel();
                baseUserModel.id = message.mentions().get(i).id();
                baseUserModel.userName = message.mentions().get(i).username();
                mentions.add(baseUserModel);
            }
        }
        messageEntity.mentions = mentions;
        messageEntity.parseUrls = message.parseUrls();

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                ((CareCluesChatApplication)application).getChatDatabase().messageDao().addMessage(messageEntity);

            }
        });

    }

    private MessageEntity insertMessageIntoDB(String message){
        final MessageEntity messageEntity = new MessageEntity();
//        messageEntity.Id = AppUtil.generateUniquId();
        messageEntity.Id = Utils.shortUUID();
        messageEntity.rId = roomId;
        messageEntity.msg = message;
        messageEntity.timeStamp = new Date();
        RoomUserModel userModel = new RoomUserModel();
        userModel.id = userDetails.Id;
        userModel.userName = userDetails.userName;
        messageEntity.user = userModel;
        messageEntity.updatedAt = new Date();
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                ((CareCluesChatApplication)application).getChatDatabase().messageDao().addMessage(messageEntity);
            }
        });
        return  messageEntity;

    }


    public void enableInputControlOptions(ServerMessageModel messageModel){
        if(messageModel != null && messageModel.control != null){
            if(messageModel.control.equals(ChatPresenter1.ControlType.CONTROL_FAMILY_MEMBER_SELECT.get())){
                getFamilyMember();
            }else if(messageModel.control.equals(ChatPresenter1.ControlType.CONTROL_HEALTH_TOPIC_SELECT.get())){
                getHealthTopic();
            }else if(messageModel.control.equals(ChatPresenter1.ControlType.CONTROL_PRIMARY_SYMPTOM_SELECT.get())){
                getPrimarySymptom(messageModel.categoryModel.link);
            }else if(messageModel.control.equals(ChatPresenter1.ControlType.CONTROL_SYMPTOM_SELECT.get())){
                getSymptoms(messageModel.categoryModel.link,messageModel.symptomModel.id);
            }else if(messageModel.control.equals(ControlType.CONTROL_SELECT.get())){
                view.displayOptions(messageModel.options);
            }else if(messageModel.control.equals(ControlType.CONTROL_SELECT_LANGUAGE.get())){
                getLanguage();
            } else if(messageModel.control.equals(ControlType.CONTROL_TEXT.get())){
                displayTextInput();
            }else{
                displayTextInput();
            }
        }else{
            displayBlank();
        }
    }

    private void displayBlank(){
        view.displayNothing();
    }

    private void displayTextInput(){
        view.displayTextInput();
    }

    private void getFamilyMember(){
        if(userProfileModel != null){
            String url = userProfileModel.data.getLink("dependants");
            if(apiExecuter == null)
                apiExecuter = RestApiExecuter.getInstance();

            apiExecuter.getServerResponse(url, new ServiceCallBack<FamilyMemberResponseModel>(FamilyMemberResponseModel.class) {
                @Override
                public void onSuccess(FamilyMemberResponseModel response) {
                    familyMemberResponseModel = response;
                    view.displayFamilyMember(familyMemberResponseModel.data);
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {
                    familyMemberResponseModel = null;
                }
            });
        }
    }


    private void getHealthTopic(){
        if(apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.getHealthTopics(new ServiceCallBack<HealthTopicResponseModel>(HealthTopicResponseModel.class) {
            @Override
            public void onSuccess(HealthTopicResponseModel response) {
                healthTopicResponseModel = response;
                view.displayHealthTopic(healthTopicResponseModel.data);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                familyMemberResponseModel = null;
            }
        });



    }

    private void getPrimarySymptom(String urlLink){

        if(apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        String url = urlLink + "/symptoms";
        apiExecuter.getServerResponse(url, new ServiceCallBack<SymptomResponseModel>(SymptomResponseModel.class) {
            @Override
            public void onSuccess(SymptomResponseModel response) {
                symptomResponseModel = response;
                view.displayPrimarySymptom(symptomResponseModel.data);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                symptomResponseModel = null;
            }
        });
    }

    private void getSymptoms(String urlLink,int id){
        if(apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        String url = urlLink;
        if(id != 0){
            url = url + "/symptoms?exclude[id]="+id;

        }

        apiExecuter.getServerResponse(url, new ServiceCallBack<SymptomResponseModel>(SymptomResponseModel.class) {
            @Override
            public void onSuccess(SymptomResponseModel response) {
                symptomResponseModel = response;
                view.displaySymptom(symptomResponseModel.data);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                symptomResponseModel = null;
            }
        });
    }

    private void getLanguage(){
        String urlLink = ApiClient.API_BASE_URL + "languages";
        if(apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();


        apiExecuter.getServerResponse(urlLink, new ServiceCallBack<LanguageResponseModel>(LanguageResponseModel.class) {
            @Override
            public void onSuccess(LanguageResponseModel response) {
                languageResponseModel = response;
                view.displayLanguage(languageResponseModel.languages);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                symptomResponseModel = null;
            }
        });
    }




    public UserProfileResponseModel getUserProfile(String userId){
        apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.getUserProfile(userId, new ServiceCallBack<UserProfileResponseModel>(UserProfileResponseModel.class) {
            @Override
            public void onSuccess(UserProfileResponseModel response) {
                userProfileModel = response;
                CareCluesChatApplication.userProfile = userProfileModel;
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                userProfileModel = null;
            }
        });
        return userProfileModel;
    }



    public enum ControlType {
        CONTROL_FAMILY_MEMBER_SELECT("familyMemberSelect"),
        CONTROL_TEXT("text"),
        CONTROL_HEALTH_TOPIC_SELECT("healthTopicSelect"),
        CONTROL_PRIMARY_SYMPTOM_SELECT("primarySymptomSelect"),
        CONTROL_SYMPTOM_SELECT("symptomSelect"),
        CONTROL_SELECT("select"),
        CONTROL_SELECT_LANGUAGE("languageSelect");

        private String control;

        ControlType(String template) {
            this.control = template;
        }

        public String get() {
            return control;
        }
    }



    class RemindTask extends TimerTask {
        @Override
        public void run() {
            if(addLanguageTasklist.size() == 0 ){
                timer.cancel();
                view.onUpdateLanguageToServer(languageUpdated);
            }else{
                timer.schedule(new RemindTask(),100);
            }
        }
    }

    private void checkTaskComplete(){
        timer = new Timer();
        timer.schedule(new RemindTask(),100);
    }


}
