package careclues.careclueschat.feature.common;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.model.GroupModel;
import careclues.careclueschat.model.GroupResponseModel;
import careclues.careclueschat.model.RoomResponse;
import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.MessageModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomMemberResponse;
import careclues.careclueschat.model.RoomModel;
import careclues.careclueschat.model.SetTopicResponseModel;
import careclues.careclueschat.model.SubscriptionModel;
import careclues.careclueschat.model.SubscriptionResponse;
import careclues.careclueschat.model.TextConsultantListResponseModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppUtil;
import careclues.careclueschat.util.DateFormatter;
import careclues.careclueschat.util.ModelEntityTypeConverter;
import careclues.rocketchat.CcUtils;

/**
 * Created by SukamalD on 6/16/2018.
 */

public class RoomDataPresenter {

    private Handler handler;
    private final int FETCH_ROOM_COMPLETED = 1;
    private final int FETCH_NO_ROOM_FOUND = 2;
    private final int FETCH_SUBSCRIPTION_COMPLETED = 3;
    private final int FETCH_UPDATED_ROOM = 4;
    private final int FETCH_MEMBER_MESSAGE_COMPLETED = 5;
    private final int FETCH_MESSAGE_COMPLETED = 6;

    private Timer timer;

    private Application application;
    private AppPreference appPreference;
    private RestApiExecuter apiExecuter;
    private List<RoomEntity> lastUpdatedRoomList;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;
    private String roomId;
    private GroupResponseModel groupResponseModel;


    private FetchRoomListner roomListner;
    private FetchSubscriptionListner subscriptionListner;
    private FetchRoomMemberHistoryListner roomMemberHistoryListner;
    private FetchLastUpdatedRoomDbListner lastUpdatedRoomDbListner;
    private FetchMessageListner fetchMessageListner;
    private CreateNewRoomListner createNewRoomListner;

    public RoomDataPresenter(Application application) {
        this.application = application;
        appPreference = ((CareCluesChatApplication) application).getAppPreference();
        apiExecuter = RestApiExecuter.getInstance();
        initHandler();

    }

    public interface FetchRoomListner {
        public void onFetchRoom(List<RoomEntity> entities);
    }

    public interface FetchLastUpdatedRoomDbListner {
        public void onFetchDbUpdatedRoom(List<RoomEntity> entities);
    }

    public interface FetchSubscriptionListner {
        public void onFetchSubscription(List<SubscriptionEntity> entities);
    }

    public interface FetchMessageListner {
        public void onFetchMessage(String roomId);
    }

    public interface FetchRoomMemberHistoryListner {
        public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities);
    }

    public interface CreateNewRoomListner{
        public void onNewRoomCreated(boolean reConsult,GroupModel groupModel);
    }


    public void registerRoomListner(FetchRoomListner roomListner) {
        this.roomListner = roomListner;
    }

    public void registerUpdatedRoomListner(FetchLastUpdatedRoomDbListner lastUpdatedRoomDbListner) {
        this.lastUpdatedRoomDbListner = lastUpdatedRoomDbListner;
    }

    public void registerSubscriptionListner(FetchSubscriptionListner subscriptionListner) {
        this.subscriptionListner = subscriptionListner;
    }

    public void registerRoomMemberHistoryListner(FetchRoomMemberHistoryListner roomMemberHistoryListner) {
        this.roomMemberHistoryListner = roomMemberHistoryListner;
    }

    public void registerMessageListner(FetchMessageListner fetchMessageListner) {
        this.fetchMessageListner = fetchMessageListner;
    }

    public void registerCreateNewRoom(CreateNewRoomListner createNewRoomListner){
        this.createNewRoomListner = createNewRoomListner;
    }

    public void unreGisterMessageListner(){
        fetchMessageListner = null;
    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case FETCH_ROOM_COMPLETED:
                        insertRoomRecordIntoDb(roomEntities);
                        if (roomListner != null) {
                            roomListner.onFetchRoom(roomEntities);
                        }
                        break;
                    case FETCH_NO_ROOM_FOUND:
                        if (roomListner != null) {
                            roomListner.onFetchRoom(null);
                        }
                        break;
                    case FETCH_SUBSCRIPTION_COMPLETED:
                        insertSubscriptionRecordIntoDb(subscriptionEntities);
                        if(subscriptionListner != null){
                            subscriptionListner.onFetchSubscription(subscriptionEntities);
                        }
                        break;
                    case FETCH_MEMBER_MESSAGE_COMPLETED:
                        insertRoomMemberRecordIntoDb(roomMemberEntities);
                        insertMessageRecordIntoDb(messageEntities, false);
                        if (roomMemberHistoryListner != null) {
                            roomMemberHistoryListner.onFetchRoomMemberMessage(roomMemberEntities, messageEntities);
                        }
                        break;
                    case FETCH_UPDATED_ROOM:
                        if (lastUpdatedRoomDbListner != null) {
                            lastUpdatedRoomDbListner.onFetchDbUpdatedRoom(lastUpdatedRoomList);
                        }
                        break;
                    case FETCH_MESSAGE_COMPLETED:
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DATE, -1);
                        appPreference.saveStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name(), DateFormatter.format(calendar.getTime(),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                        if (fetchMessageListner != null) {
                            fetchMessageListner.onFetchMessage(roomId);
                        }
                        break;

                }
            }
        };
    }

    public void getActiveRoomList(final int count) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
//                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getLastUpdatedRoom(0,count);
                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getActiveRoomList();
                    if (lastUpdatedRoomList.size() == 0) {
                        lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getNextRoomList(0, 10);
                    }

                    handler.sendEmptyMessage(FETCH_UPDATED_ROOM);
                } catch (Throwable e) {
                    Log.e("DBERROR", "getActiveRoomList : " + e.toString());
                }

            }
        });
    }


    public void getRoom(String lastUpadate) {

        if (lastUpadate == null) {
            apiExecuter.getRooms(new ServiceCallBack<RoomResponse>(RoomResponse.class) {
                @Override
                public void onSuccess(RoomResponse response) {
//                System.out.println("Room Response: "+ response);
                    filterRoomRecords(response.getUpdate());
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
        } else {
            apiExecuter.getRooms(lastUpadate,
                    new ServiceCallBack<RoomResponse>(RoomResponse.class) {
                        @Override
                        public void onSuccess(RoomResponse response) {
//                System.out.println("Room Response: "+ response);
                            filterRoomRecords(response.getUpdate());
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
        }

    }

    private void filterRoomRecords(List<RoomModel> rooms) {
        if (rooms != null && rooms.size() > 0) {
            roomEntities = new ArrayList<>();
            for (RoomModel roomModel : rooms) {
                if (roomModel.topic != null && roomModel.topic.equalsIgnoreCase("text-consultation")
                        && roomModel.type == BaseRoomModel.RoomType.PRIVATE) {
                    RoomEntity roomEntity;
                    roomEntity = ModelEntityTypeConverter.roomModelToEntity(roomModel);
                    roomEntities.add(roomEntity);
                }
            }
            handler.sendEmptyMessage(FETCH_ROOM_COMPLETED);

        } else {
            handler.sendEmptyMessage(FETCH_NO_ROOM_FOUND);
        }

    }


    public void getSubscription(String lastUpadate, final List<RoomEntity> moreList) {
        if (lastUpadate == null) {
            apiExecuter.getSubscription(new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
                @Override
                public void onSuccess(SubscriptionResponse response) {
//                System.out.println("Subscription Response: "+ response);
                    filterSubscriptionRecord(response.update, moreList);
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
        } else {
            apiExecuter.getSubscription(lastUpadate,
                    new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
                        @Override
                        public void onSuccess(SubscriptionResponse response) {
//                System.out.println("Subscription Response: "+ response);
                            filterSubscriptionRecord(response.update, moreList);
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
        }

    }

    private void filterSubscriptionRecord(List<SubscriptionModel> update, final List<RoomEntity> moreList) {

        if (moreList != null) {
            subscriptionEntities = new ArrayList<>();
            List<String> roomIdList = new ArrayList<>();
            for (RoomEntity roomModel : moreList) {
                roomIdList.add(roomModel.roomId);
            }
            for (SubscriptionModel subscriptionModel : update) {
                // if( roomIdList.contains(subscriptionModel.rId) && (subscriptionModel.open)){
                if (roomIdList.contains(subscriptionModel.rId)) {
                    SubscriptionEntity subscriptionEntity;
                    subscriptionEntity = ModelEntityTypeConverter.subscriptionModelToEntity(subscriptionModel);
                    subscriptionEntities.add(subscriptionEntity);
                }
            }
        }

        handler.sendEmptyMessage(FETCH_SUBSCRIPTION_COMPLETED);
    }


    public void insertRoomRecordIntoDb(final List<RoomEntity> roomEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().roomDao().insertAll(roomEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR"," insertRoomRecordIntoDb : " + e.getMessage());
                }
            }
        });

    }

    public void insertRoomRecordIntoDb(final RoomEntity roomEntity) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<RoomEntity> roomEntityList = new ArrayList<>();
                    roomEntityList.add(roomEntity);
                    ((CareCluesChatApplication) application).getChatDatabase().roomDao().insertAll(roomEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", " insertRoomRecordIntoDb : " + e.getMessage());
                }
            }
        });

    }

    public void insertSubscriptionRecordIntoDb(final List<SubscriptionEntity> subscriptionEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().subscriptionDao().insertAll(subscriptionEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", " insertSubscriptionRecordIntoDb : " + e.getMessage());
                }
            }
        });
    }


    private List<String> roomIdMember;
    private List<String> roomIdMessage;
    private List<RoomMemberEntity> roomMemberEntities;
    private List<MessageEntity> messageEntities;

    public void fetchMemberAndMessage(List<RoomEntity> moreList) {
        roomIdMember = new ArrayList<>();
        roomIdMessage = new ArrayList<>();
        for (RoomEntity roomEntity : moreList) {
            roomIdMember.add(roomEntity.roomId);
            roomIdMessage.add(roomEntity.roomId);
        }
        checkTaskComplete();
        getRoomMemberMessageHistory(moreList);
    }

    private void getRoomMemberMessageHistory(List<RoomEntity> moreList) {
        roomMemberEntities = new ArrayList<>();
        messageEntities = new ArrayList<>();
        for (RoomEntity roomEntity : moreList) {
            checkMember(roomEntity.roomId);
            checkLatMessage(roomEntity.roomId);
//            getMessageHistory(roomEntity.roomId);

//            getRoomMembers(roomEntity.roomId);
//            getMessageHistory(roomEntity.roomId,0,null);


        }


    }

    private void checkMember(final String roomId){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                List<RoomMemberEntity> memberList;
                try {
                    memberList = ((CareCluesChatApplication) application).getChatDatabase().roomMemberDao().findByRoomId(roomId);
                    if(memberList == null){
                        getRoomMembers(roomId);
                    }else{
                        roomIdMember.remove(roomId);
                    }


                } catch (Throwable e) {
                    Log.e("DBERROR", " getOpenRoomDb : " + e.toString());
                }

            }
        });


    }

    private void checkLatMessage(final String roomId){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                MessageEntity lastMessage;
                String lastTime;
                try {
                    lastMessage = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getLastMessage(roomId);
                   if(lastMessage == null){
                       getMessageHistory(roomId,0,null);
                   }else{
                       lastTime = AppUtil.convertToServerPostDate(lastMessage.timeStamp);
                       Log.e("TTTTTTTTTTT",lastTime);
                       roomIdMessage.remove(roomId);

                   }


                } catch (Throwable e) {
                    Log.e("DBERROR", " getOpenRoomDb : " + e.toString());
                }

            }
        });
    }

//    private void getAllRoomMember(List<RoomEntity> moreList) {
//        roomMemberEntities = new ArrayList<>();
//        messageEntities = new ArrayList<>();
//        for (RoomEntity roomEntity : moreList) {
//            getRoomMembers(roomEntity.roomId);
//            getMessageHistory(roomEntity.roomId);
//        }
//
//    }
//
//    private void getAllRoomMessage(List<RoomEntity> moreList) {
//        roomMemberEntities = new ArrayList<>();
//        messageEntities = new ArrayList<>();
//        for (RoomEntity roomEntity : moreList) {
//            getRoomMembers(roomEntity.roomId);
//            getMessageHistory(roomEntity.roomId);
//        }
//
//    }

    private void getRoomMembers(final String roomId) {
        apiExecuter.getRoomMembers(roomId, new ServiceCallBack<RoomMemberResponse>(RoomMemberResponse.class) {
            @Override
            public void onSuccess(RoomMemberResponse response) {
//                System.out.println("RoomMember Response: "+ response.members.toString());
                filterRoomMembersRecord(roomId, response.members);
                roomIdMember.remove(roomId);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                roomIdMember.remove(roomId);
            }
        });
    }

    private void getMessageHistory(final String roomId, int count, String lastTime) {
        apiExecuter.getChatMessage(roomId, 0, null, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
            @Override
            public void onSuccess(MessageResponseModel response) {
//                System.out.println("RoomMember Response: "+ response.messages.toString());
                filterMessageRecord(response.messages);
                roomIdMessage.remove(roomId);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                roomIdMessage.remove(roomId);
            }
        });
    }

    public void fetchMessages(final String roomId, final boolean isOnItemClick) {
        this.roomId = roomId;
        apiExecuter.getChatMessage(roomId, 0, null, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
            @Override
            public void onSuccess(MessageResponseModel response) {
                filterMessageRecord(response.messages);
                insertMessageRecordIntoDb(messageEntities, isOnItemClick);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                Log.e("Error", "Error!!!!");
            }
        });
    }

    private void filterRoomMembersRecord(String roomid, List<RoomMemberModel> memberModels) {
        for (RoomMemberModel memberModel : memberModels) {
            RoomMemberEntity memberEntity;
            memberEntity = ModelEntityTypeConverter.roomMemberToEntity(memberModel);
            memberEntity.rId = roomid;
            roomMemberEntities.add(memberEntity);
        }
    }

    private void filterMessageRecord(List<MessageModel> messageModels) {

        if (messageEntities == null) {
            messageEntities = new ArrayList<>();
        }
        for (MessageModel messageModel : messageModels) {
            MessageEntity messageEntity;
            //TODO check if message is hidden then don't store
            Log.d("MessageHideen", "filterMessageRecord: " + messageModel.isHiddenMessage());
            if (!messageModel.isHiddenMessage()) {
                messageEntity = ModelEntityTypeConverter.messageModelToEntity(messageModel);
                messageEntity.synced = true;
                messageEntities.add(messageEntity);
            }
        }
    }


    private void insertRoomMemberRecordIntoDb(final List<RoomMemberEntity> roomMemberEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().roomMemberDao().insertAll(roomMemberEntities);

                } catch (Throwable e) {
                    Log.e("DBERROR", " insertRoomMemberRecordIntoDb : " + e.getMessage());
                }
            }
        });
    }

    private void insertMessageRecordIntoDb(final List<MessageEntity> messageEntities, final boolean isOnItemClick) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
//                    for(MessageEntity entity: messageEntities){
//                        Log.e("MESSAGE : ", entity.toString());
//                    }
                    ((CareCluesChatApplication) application).getChatDatabase().messageDao().insertAll(messageEntities);

                    if (isOnItemClick) {
                        handler.sendEmptyMessage(FETCH_MESSAGE_COMPLETED);

                    }
                } catch (Throwable e) {
                    Log.e("DBERROR", " insertMessageRecordIntoDb : " + e.toString());
                }

            }
        });

    }


    private void checkTaskComplete() {
        timer = new Timer();
        timer.schedule(new RoomDataPresenter.RemindTask(), 1000);
    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            if (roomIdMember.size() == 0) {
                if (roomIdMessage.size() == 0) {
                    timer.cancel();
                    handler.sendEmptyMessage(FETCH_MEMBER_MESSAGE_COMPLETED);
                } else {
                    timer.schedule(new RoomDataPresenter.RemindTask(), 1000);
                }
            } else {
                timer.schedule(new RoomDataPresenter.RemindTask(), 1000);
            }
        }
    }

    public void createNewRoom(final boolean reConsult) {
        String roomName = "TC-" + (System.currentTimeMillis() / 1000);
        String[] members = {"api_admin", "bot-la2zewmltd"};
        apiExecuter.createPrivateRoom(roomName, members, new ServiceCallBack<GroupResponseModel>(GroupResponseModel.class) {
            @Override
            public void onSuccess(GroupResponseModel response) {
                Log.e("NEWROOM", "New Room : " + response.toString());
                if (response.success) {
                    groupResponseModel = response;
                    setRoomTopic(response.group, "text-consultation",reConsult);
                }
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });

    }

    private void setRoomTopic(final GroupModel groupModel, String topic, final boolean reConsult) {
        apiExecuter.setRoomTopicw(groupModel.id, topic, new ServiceCallBack<SetTopicResponseModel>(SetTopicResponseModel.class) {
            @Override
            public void onSuccess(SetTopicResponseModel response) {
                Log.e("NEWROOM", "SetTopic : " + response.toString());
                initiateBotChat(groupModel.id);
                if(createNewRoomListner != null){
                    createNewRoomListner.onNewRoomCreated(reConsult,groupModel);
                }
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }


    private void initiateBotChat(String roomId){
        String msg = "@bot-la2zewmltd introduce " + AppConstant.userProfile.data.firstName + " to text consultation";
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();
        apiExecuter.sendNewMessage(CcUtils.shortUUID(), roomId, msg, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
            @Override
            public void onSuccess(MessageResponseModel response) {

            }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
    }

    public void getTextConsultant(String roomId) {
        String urlLink = AppConstant.API_BASE_URL + "text_consultations?chat_conversation_id="+roomId+"&expand=physician,patient,health_topic,qualifications,reviews_count,specializations";
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.getServerResponse(urlLink, new ServiceCallBack<TextConsultantListResponseModel>(TextConsultantListResponseModel.class) {
            @Override
            public void onSuccess(TextConsultantListResponseModel response) {
                if(response != null && response.data != null && response.data.size() > 0){
                    AppConstant.textConsultantModel = response.data.get(0);
                }

            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

}
