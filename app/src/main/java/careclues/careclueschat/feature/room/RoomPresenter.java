package careclues.careclueschat.feature.room;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.rocketchat.common.RocketChatApiException;
import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.model.User;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.listener.TypingListener;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.AccountListener;
import com.rocketchat.core.callback.EmojiListener;
import com.rocketchat.core.callback.GetSubscriptionListener;
import com.rocketchat.core.callback.LoginCallback;
import com.rocketchat.core.callback.MessageCallback;
import com.rocketchat.core.callback.UserListener;
import com.rocketchat.core.model.Emoji;
import com.rocketchat.core.model.Message;
import com.rocketchat.core.model.Permission;
import com.rocketchat.core.model.PublicSetting;
import com.rocketchat.core.model.Subscription;
import com.rocketchat.core.model.Token;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.model.GroupResponseModel;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.model.RoomMemberModel;
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
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.ModelEntityTypeConverter;

public class RoomPresenter implements RoomContract.presenter {

    private RoomContract.view view;
    private Application application;
    private RocketChatClient chatClient;
    private List<RoomEntity> list;
    private List<RoomAdapterModel> modelList;
    private RestApiExecuter apiExecuter;
    private AppPreference appPreference;
    private Handler handler;
    private RoomAdapterModel adapterModel;


    public RoomPresenter(RoomContract.view view, Application application) {
        this.view = view;
        this.application = application;
        apiExecuter = RestApiExecuter.getInstance();
        appPreference = ((CareCluesChatApplication) application).getAppPreference();
        handleMessage();
        getRoom();
    }

    private void handleMessage(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.what == 1){
                    Toast.makeText(application, "handler", Toast.LENGTH_SHORT).show();
                    view.displyRoomList(modelList);
                }
            }
        };
    }

    @Override
    public void getRoom() {
        modelList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = new ArrayList<>();
                List<RoomEntity> moreList;
//                moreList = ((CareCluesChatApplication)application).getChatDatabase().subscriptionDao().getSubscripttion(0,10);
                moreList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getAll();

                if (moreList != null && moreList.size() > 0) {
                    list.addAll(moreList);
                }
                populateAdapterData(moreList);
            }
        }).start();

    }



    private void populateAdapterData(final List<RoomEntity> subscriptions) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    if (subscriptions != null) {
                        for (final RoomEntity entity : subscriptions) {
                            adapterModel = new RoomAdapterModel();
                            adapterModel.Id = entity.roomId;
                            adapterModel.description = entity.description;
                            List<RoomMemberEntity> memberEntities = ((CareCluesChatApplication) application).getChatDatabase().roomMemberDao().findByRoomId(entity.roomId);
                            List<MessageEntity> messageEntities = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getUpdatedMessage(entity.roomId, 1);

                            if (messageEntities != null && messageEntities.size() > 0) {
                                adapterModel.updatedAt = messageEntities.get(0).updatedAt;
                            }else{
                                adapterModel.updatedAt = entity.updatedAt;
                            }


                            for (RoomMemberEntity memberEntity : memberEntities) {
                                RoomMemberModel memberModel = ModelEntityTypeConverter.roomMemberEntityToModel(memberEntity);
                                if (memberEntity.userName.equalsIgnoreCase("api_admin") || memberEntity.userName.equalsIgnoreCase("bot-la2zewmltd") || memberEntity.userName.equalsIgnoreCase("sachu-985")) {
                                    adapterModel.name = "New-Consultant";
                                } else {
                                    adapterModel.name = memberEntity.name;
                                    break;
                                }
                            }
                            modelList.add(adapterModel);
                        }

                        android.os.Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                } catch (Throwable e) {
                    System.out.println("Error111111111111111111111111111111111");
                }

            }
        });

    }

    @Override
    public void getMoreRoom(final int startCount, final int threshold) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RoomEntity> moreList;
                moreList = ((CareCluesChatApplication)application).getChatDatabase().roomDao().getRoom(startCount,threshold);
                if(moreList != null && moreList.size() > 0){
                    list.addAll(moreList);
                }
                populateAdapterData(moreList);
                view.displyRoomList(modelList);
            }
        }).start();
    }

    @Override
    public void reconnectToServer() {
    }

    @Override
    public void disconnectToServer() {
    }

    @Override
    public void createNewRoom() {
        String roomName = "SUKU-TEST-" + (System.currentTimeMillis() / 1000);
        String[] members = {"api_admin", "bot-la2zewmltd", "sachu-985"};
        apiExecuter.createPrivateRoom(roomName, members, new ServiceCallBack<GroupResponseModel>(GroupResponseModel.class) {
            @Override
            public void onSuccess(GroupResponseModel response) {
                System.out.println("New Room : " + response.toString());
                getRoomData();
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    private void getRoomData() {

        if (appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()) == null) {
            apiExecuter.getRooms(new ServiceCallBack<RoomResponse>(RoomResponse.class) {
                @Override
                public void onSuccess(RoomResponse response) {
                    filterRoomRecords(response.getUpdate());
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
        } else {
            apiExecuter.getRooms(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()),
                    new ServiceCallBack<RoomResponse>(RoomResponse.class) {
                        @Override
                        public void onSuccess(RoomResponse response) {
                            filterRoomRecords(response.getUpdate());
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
        }


    }

    private void getSubscription() {
        if (appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()) == null) {
            apiExecuter.getSubscription(new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
                @Override
                public void onSuccess(SubscriptionResponse response) {
                    filterSubscriptionRecord(response.update);
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
        } else {
            apiExecuter.getSubscription(appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name()),
                    new ServiceCallBack<SubscriptionResponse>(SubscriptionResponse.class) {
                        @Override
                        public void onSuccess(SubscriptionResponse response) {
                            filterSubscriptionRecord(response.update);
                        }

                        @Override
                        public void onFailure(List<NetworkError> errorList) {

                        }
                    });
        }

    }

    private List<String> roomIdList;
    private List<RoomEntity> roomEntities;
    private List<SubscriptionEntity> subscriptionEntities;

    private void filterRoomRecords(List<RoomModel> rooms) {

        if (rooms != null && rooms.size() > 0) {
            roomEntities = new ArrayList<>();
            roomIdList = new ArrayList<>();
            for (RoomModel roomModel : rooms) {
//                if( roomModel.topic != null && roomModel.topic.equalsIgnoreCase("text-consultation") && roomModel.type == BaseRoomModel.RoomType.PRIVATE){

                RoomEntity roomEntity;
                roomEntity = ModelEntityTypeConverter.roomModelToEntity(roomModel);
                roomEntities.add(roomEntity);
                roomIdList.add(roomModel.Id);
//                }
            }
            insertRoomRecordIntoDb(roomEntities);
            getSubscription();

        } else {
            view.displyNextScreen();
        }

    }

    private void filterSubscriptionRecord(List<SubscriptionModel> update) {
        subscriptionEntities = new ArrayList<>();
        for (SubscriptionModel subscriptionModel : update) {
            if (roomIdList.contains(subscriptionModel.rId)) {

                SubscriptionEntity subscriptionEntity;
                subscriptionEntity = ModelEntityTypeConverter.subscriptionModelToEntity(subscriptionModel);
                subscriptionEntities.add(subscriptionEntity);
            }
        }
        insertSubscriptionRecordIntoDb(subscriptionEntities);

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
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getRoom();
                        }
                    }, 5000);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.getMessage());
                }
            }
        });
    }

}
