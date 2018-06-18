package careclues.careclueschat.feature.common;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.room.RoomPresenter;
import careclues.careclueschat.feature.room.RoomResponse;
import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.MessageModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomMemberResponse;
import careclues.careclueschat.model.RoomModel;
import careclues.careclueschat.model.SubscriptionModel;
import careclues.careclueschat.model.SubscriptionResponse;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.ModelEntityTypeConverter;

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

    private Timer timer;

    private Application application;
    private RestApiExecuter apiExecuter;
    private List<RoomEntity> lastUpdatedRoomList;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;


    private FetchRoomListner roomListner;
    private FetchSubscriptionListner subscriptionListner;
    private FetchRoomMemberHistoryListner roomMemberHistoryListner;
    private FetchLastUpdatedRoomDbListner lastUpdatedRoomDbListner;

    public RoomDataPresenter(Application application){
        this.application = application;
        apiExecuter = RestApiExecuter.getInstance();
        initHandler();

    }

    public interface FetchRoomListner{
        public void onFetchRoom(List<RoomEntity> entities);
    }

    public interface FetchLastUpdatedRoomDbListner{
        public void onFetchDbUpdatedRoom(List<RoomEntity> entities);
    }

    public interface FetchSubscriptionListner{
        public void onFetchSubscription(List<SubscriptionEntity> entities);
    }

    public interface FetchRoomMemberHistoryListner{
        public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities,List<MessageEntity> messageEntities);
    }


    public void registerRoomListner(FetchRoomListner roomListner){
        this.roomListner = roomListner;
    }

    public void registerUpdatedRoomListner(FetchLastUpdatedRoomDbListner lastUpdatedRoomDbListner){
        this.lastUpdatedRoomDbListner = lastUpdatedRoomDbListner;
    }

    public void registerSubscriptionListner(FetchSubscriptionListner subscriptionListner){
        this.subscriptionListner = subscriptionListner;
    }

    public void registerRoomMemberHistoryListner(FetchRoomMemberHistoryListner roomMemberHistoryListner){
        this.roomMemberHistoryListner = roomMemberHistoryListner;
    }

    private void initHandler(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
                    case FETCH_ROOM_COMPLETED:
                        insertRoomRecordIntoDb(roomEntities);
                        if(roomListner != null){
                            roomListner.onFetchRoom(roomEntities);
                        }
                        break;
                    case FETCH_NO_ROOM_FOUND:
                        if(roomListner != null){
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
                        insertMessageRecordIntoDb(messageEntities);
                        if(roomMemberHistoryListner != null){
                            roomMemberHistoryListner.onFetchRoomMemberMessage(roomMemberEntities,messageEntities);
                        }
                        break;
                    case FETCH_UPDATED_ROOM:
                        if(lastUpdatedRoomDbListner != null){
                            lastUpdatedRoomDbListner.onFetchDbUpdatedRoom(lastUpdatedRoomList);
                        }
                        break;

                }
            }
        };
    }

    public  void getUpdatedRoomList(final int count){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getLastUpdatedRoom(0,count);
                    handler.sendEmptyMessage(FETCH_UPDATED_ROOM);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.toString());
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

    private void filterRoomRecords(List<RoomModel> rooms){
        if(rooms != null && rooms.size() > 0){
            roomEntities = new ArrayList<>();
            for(RoomModel roomModel : rooms){
                if( roomModel.topic != null && roomModel.topic.equalsIgnoreCase("text-consultation") && roomModel.type == BaseRoomModel.RoomType.PRIVATE){
                    RoomEntity roomEntity;
                    roomEntity = ModelEntityTypeConverter.roomModelToEntity(roomModel);
                    roomEntities.add(roomEntity);
                }
            }
            handler.sendEmptyMessage(FETCH_ROOM_COMPLETED);

        }else{
            handler.sendEmptyMessage(FETCH_NO_ROOM_FOUND);
        }

    }


    public void getSubscription(String lastUpadate,final List<RoomEntity> moreList){
        if(lastUpadate == null){
            apiExecuter.getSubscription(new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
                @Override
                public void onSuccess(SubscriptionResponse response) {
//                System.out.println("Subscription Response: "+ response);
                    filterSubscriptionRecord(response.update,moreList);
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
        }else{
            apiExecuter.getSubscription(lastUpadate,
                    new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
                        @Override
                        public void onSuccess(SubscriptionResponse response) {
//                System.out.println("Subscription Response: "+ response);
                            filterSubscriptionRecord(response.update,moreList);
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
        }

    }

    private void filterSubscriptionRecord(List<SubscriptionModel> update, final List<RoomEntity> moreList){
        subscriptionEntities = new ArrayList<>();
        List<String> roomIdList = new ArrayList<>();
        for(RoomEntity roomModel : moreList){
            roomIdList.add(roomModel.roomId);
        }
        for(SubscriptionModel subscriptionModel : update){
            if( roomIdList.contains(subscriptionModel.rId)){
                SubscriptionEntity subscriptionEntity ;
                subscriptionEntity = ModelEntityTypeConverter.subscriptionModelToEntity(subscriptionModel);
                subscriptionEntities.add(subscriptionEntity);
            }
        }

        handler.sendEmptyMessage(FETCH_SUBSCRIPTION_COMPLETED);
    }


    private void insertRoomRecordIntoDb(final List<RoomEntity> roomEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().roomDao().insertAll(roomEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.getMessage());
                }
            }
        });

    }

    private void insertSubscriptionRecordIntoDb(final List<SubscriptionEntity> subscriptionEntities) {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().subscriptionDao().insertAll(subscriptionEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.getMessage());
                }
            }
        });
    }


    private List<String> roomIdMember;
    private List<String> roomIdMessage;
    private List<RoomMemberEntity> roomMemberEntities;
    private List<MessageEntity> messageEntities;

    public void fetchMemberAndMessage(List<RoomEntity> moreList){
        roomIdMember = new ArrayList<>();
        roomIdMessage = new ArrayList<>();
        for(RoomEntity roomEntity:moreList){
            roomIdMember.add(roomEntity.roomId);
            roomIdMessage.add(roomEntity.roomId);
        }
        checkTaskComplete();
        getRoomMemberMessageHistory(moreList);
    }

    private void getRoomMemberMessageHistory(List<RoomEntity> moreList){
        roomMemberEntities = new ArrayList<>();
        messageEntities = new ArrayList<>();
        for(RoomEntity roomEntity:moreList){
            getRoomMembers(roomEntity.roomId);
            getMessageHistory(roomEntity.roomId);
        }

    }

    private void getAllRoomMember(List<RoomEntity> moreList){
        roomMemberEntities = new ArrayList<>();
        messageEntities = new ArrayList<>();
        for(RoomEntity roomEntity:moreList){
            getRoomMembers(roomEntity.roomId);
            getMessageHistory(roomEntity.roomId);
        }

    }

    private  void getAllRoomMessage(List<RoomEntity> moreList){
        roomMemberEntities = new ArrayList<>();
        messageEntities = new ArrayList<>();
        for(RoomEntity roomEntity:moreList){
            getRoomMembers(roomEntity.roomId);
            getMessageHistory(roomEntity.roomId);
        }

    }

    private void getRoomMembers(final String roomId){
        apiExecuter.getRoomMembers(roomId, new ServiceCallBack<RoomMemberResponse>(RoomMemberResponse.class) {
            @Override
            public void onSuccess(RoomMemberResponse response) {
//                System.out.println("RoomMember Response: "+ response.members.toString());
                filterRoomMembersRecord(roomId,response.members);
                roomIdMember.remove(roomId);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                roomIdMember.remove(roomId);
            }
        });
    }

    private void getMessageHistory(final String roomId){
        apiExecuter.getChatMessage(roomId,0, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
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

    private void filterRoomMembersRecord(String roomid,List<RoomMemberModel> memberModels){
        for(RoomMemberModel memberModel : memberModels){
            RoomMemberEntity memberEntity;
            memberEntity = ModelEntityTypeConverter.roomMemberToEntity(memberModel);
            memberEntity.rId = roomid;
            roomMemberEntities.add(memberEntity);
        }
    }

    private void filterMessageRecord(List<MessageModel> messageModels){
        for(MessageModel messageModel : messageModels){
            MessageEntity messageEntity;
            messageEntity = ModelEntityTypeConverter.messageModelToEntity(messageModel);
            messageEntities.add(messageEntity);
        }
    }

    private void insertRoomMemberRecordIntoDb(final List<RoomMemberEntity> roomMemberEntities){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    ((CareCluesChatApplication)application).getChatDatabase().roomMemberDao().insertAll(roomMemberEntities);

                }catch (Throwable e){
                    Log.e("DBERROR",e.getMessage());
                }
            }
        });
    }

    private void insertMessageRecordIntoDb(final List<MessageEntity> messageEntities) {
        System.out.println("insertMessageRecordIntoDb : " + messageEntities.size());

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for(MessageEntity entity: messageEntities){
                        Log.e("MESSAGE : ", entity.toString());
                    }
                    ((CareCluesChatApplication) application).getChatDatabase().messageDao().insertAll(messageEntities);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.toString());
                }

            }
        });

    }



    private void checkTaskComplete(){
        timer = new Timer();
        timer.schedule(new RoomDataPresenter.RemindTask(),1000);
    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            if(roomIdMember.size() == 0 ){
                if(roomIdMessage.size() == 0){
                    timer.cancel();
                    handler.sendEmptyMessage(FETCH_MEMBER_MESSAGE_COMPLETED);
                }else{
                    timer.schedule(new RoomDataPresenter.RemindTask(),1000);
                }
            }else{
                timer.schedule(new RoomDataPresenter.RemindTask(),1000);
            }
        }
    }




}