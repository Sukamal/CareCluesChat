package careclues.careclueschat.feature.room;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.ChatPresenter1;
import careclues.careclueschat.feature.common.RoomDataPresenter;
import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.GroupModel;
import careclues.careclueschat.model.LoginResponse;
import careclues.careclueschat.model.GroupResponseModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomModel;
import careclues.careclueschat.model.SetTopicResponseModel;
import careclues.careclueschat.model.UserProfileResponseModel;
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
import careclues.careclueschat.util.ModelEntityTypeConverter;
import careclues.rocketchat.CcRocketChatClient;
import careclues.rocketchat.CcSocket;
import careclues.rocketchat.CcUtils;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.callback.CcRoomCallback;
import careclues.rocketchat.common.CcRocketChatException;
import careclues.rocketchat.listner.CcChatRoomFactory;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.listner.CcTypingListener;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcRoom;
import careclues.rocketchat.models.CcSubscription;
import careclues.rocketchat.models.CcToken;

public class RoomPresenter implements RoomContract.presenter,
        RoomDataPresenter.FetchRoomMemberHistoryListner,
        RoomDataPresenter.FetchLastUpdatedRoomDbListner,
        RoomDataPresenter.FetchMessageListner,
        RoomDataPresenter.FetchRoomListner,
        RoomDataPresenter.FetchSubscriptionListner,
        RoomDataPresenter.CreateNewRoomListner,

        CcConnectListener,
        CcMessageCallback.SubscriptionCallback,
        CcMessageCallback.NewRoomCallback,
        CcRoomCallback.GroupCreateCallback,
        CcTypingListener {




    private final int LOG_IN_SUCCESS = 1;
    private final int LOG_IN_FAIL = 2;
    private final int LOAD_ROOM_DATA = 3;
    private final int LOAD_MORE_ROOM_DATA = 4;
    private final int LOAD_NEW_ROOM_DATA = 5;



    private boolean isFirstTimeLoad = true;
    private RoomContract.view view;
    private Application application;
    private CcRocketChatClient chatClient;
    private RestApiExecuter apiExecuter;
    private AppPreference appPreference;
    private Handler handler;
    private RoomDataPresenter roomDataPresenter;


    private List<RoomAdapterModel> modelList;
    private RoomAdapterModel adapterModel;
    private List<RoomEntity> moreList;
    private List<RoomEntity> lastUpdatedRoomList;


    public RoomPresenter(RoomContract.view view, Application application) {
        this.view = view;
        this.application = application;
        apiExecuter = RestApiExecuter.getInstance();
        appPreference = ((CareCluesChatApplication) application).getAppPreference();
        handleMessage();

        roomDataPresenter = new RoomDataPresenter(application);
        roomDataPresenter.registerRoomListner(this);
        roomDataPresenter.registerSubscriptionListner(this);
        roomDataPresenter.registerUpdatedRoomListner(this);
        roomDataPresenter.registerRoomMemberHistoryListner(this);
        roomDataPresenter.registerMessageListner(this);
        roomDataPresenter.registerCreateNewRoom(this);




        initWebSocket();

    }

    private void initWebSocket() {
        chatClient = ((CareCluesChatApplication) application).getRocketChatClient();
        chatClient.connect(this);
    }


    @Override
    public void onConnect(String sessionID) {
        view.displayToastMessage(application.getString(R.string.connected));
        doLogin(AppConstant.USER_NAME, AppConstant.PASSWORD);
    }

    @Override
    public void onDisconnect(boolean closedByServer) {
//        reconnectToServer();
//        view.onConnectionFaild(2);
    }

    @Override
    public void onConnectError(Throwable websocketException) {
//        view.onConnectionFaild(1);
//        reconnectToServer();
    }


    @Override
    public void doLogin(String userId, String password) {
        if (chatClient.getWebsocketImpl().getSocket().getState() == CcSocket.State.CONNECTED) {
            chatClient.login(userId, password, new CcLoginCallback() {
                @Override
                public void onLoginSuccess(CcToken token) {
                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
                    ((CareCluesChatApplication) application).setUserId(token.getUserId());
                    ((CareCluesChatApplication) application).setUserName(AppConstant.USER_NAME);
                    doApiLogin(AppConstant.USER_NAME, AppConstant.PASSWORD);
                    view.displayToastMessage(token.getUserId());

                }

                @Override
                public void onError(CcRocketChatException error) {

                }
            });
        }

    }


    @Override
    public void reconnectToServer() {
        view.displayToastMessage("reconnectToServer");
        chatClient.getWebsocketImpl().getSocket().reconnect();
    }

    @Override
    public void disconnectToServer() {
        chatClient.disconnect();
        chatClient.getWebsocketImpl().getConnectivityManager().unRegister(this);
    }


    //-------------------------------------------------LOAD BASIC DATA----------------------------------------------------------------------------------


    @Override
    public void onFetchRoom(List<RoomEntity> entities) {
        view.displayToastMessage("Fetching Rooms");
        String lastUpdate = appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name());
        roomDataPresenter.getSubscription(lastUpdate, entities);
    }


    @Override
    public void onFetchSubscription(List<SubscriptionEntity> entities) {
        view.displayToastMessage("Fetching Subscriptions");
        roomDataPresenter.getActiveRoomList(10);
    }

    @Override
    public void onFetchDbUpdatedRoom(List<RoomEntity> entities) {
        if (isFirstTimeLoad) {
            roomDataPresenter.fetchMemberAndMessage(entities);
        } else {
            populateAdapterData(entities, LOAD_ROOM_DATA);
        }
    }

    @Override
    public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities) {
        if (isFirstTimeLoad) {
            isFirstTimeLoad = false;
            view.onFetchBasicData();
        } else {
            populateAdapterData(moreList, LOAD_MORE_ROOM_DATA);

        }
    }

    @Override
    public void onFetchMessage(String roomId) {
        view.displyChatScreen(roomId);
    }

    @Override
    public void getOpenRoom() {
        modelList = new ArrayList<>();
        getOpenRoomDb();
    }

    @Override
    public void doApiLogin(String userId, String password) {
        apiExecuter.doLogin(userId, password, new ServiceCallBack<LoginResponse>(LoginResponse.class) {
            @Override
            public void onSuccess(LoginResponse response) {
                RestApiExecuter.getInstance().getAuthToken().saveToken(response.getData().getUserId(), response.getData().getAuthToken());
                getLoginUserDetails(response.getData().getUserId());
                getUserProfile(AppConstant.getUserId());
                subscribeRoomChangeEvent(response.getData().getUserId());
                subscribeSubscriptionChangeEvent(response.getData().getUserId());
                handler.sendEmptyMessage(LOG_IN_SUCCESS);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                handler.sendEmptyMessage(LOG_IN_FAIL);
            }
        });

    }

    @Override
    public void getMoreRoom(final int startCount, final int threshold) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                moreList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getNextRoomList(startCount, threshold);
                subsCribeRoomMessageEvent(moreList);

                if (moreList != null && moreList.size() > 0) {
                    roomDataPresenter.fetchMemberAndMessage(moreList);
                }
            }
        });
    }


    private void getLoginUserDetails(final String userId) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                RoomMemberEntity userDetails = ((CareCluesChatApplication) application).getChatDatabase().roomMemberDao().findById(userId);
                if (userDetails != null) {
                    ((CareCluesChatApplication) application).setUserName(userDetails.userName);
                }
            }
        });
    }

    private void handleMessage() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case LOG_IN_SUCCESS:
                        String lastUpdate = appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name());
                        roomDataPresenter.getRoom(lastUpdate);
                        break;
                    case LOG_IN_FAIL:
                        break;
                    case LOAD_ROOM_DATA:
                        view.displyRoomListScreen(modelList);
                        break;
                    case LOAD_MORE_ROOM_DATA:
                        view.displyMoreRoomList(modelList);
                        break;
                    case LOAD_NEW_ROOM_DATA:
                        view.displyNewRoomList(modelList);
                        break;
                }
            }
        };
    }



    public void getOpenRoomDb() {

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getActiveRoomList();

                    if(lastUpdatedRoomList.size() == 0){
                        lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getNextRoomList(0,10);

                    }
                    subsCribeRoomMessageEvent(lastUpdatedRoomList);
                    populateAdapterData(lastUpdatedRoomList, LOAD_ROOM_DATA);
                } catch (Throwable e) {
                    Log.e("DBERROR", " getOpenRoomDb : " + e.toString());
                }

            }
        });
    }

    private void populateAdapterData(final List<RoomEntity> rooms, final int what) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    modelList = new ArrayList<>();
                    if (rooms != null) {
                        for (final RoomEntity entity : rooms) {
                            adapterModel = new RoomAdapterModel();
                            adapterModel.Id = entity.roomId;
                            adapterModel.roomName = entity.roomName;
                            adapterModel.description = entity.description;
                            List<RoomMemberEntity> memberEntities = ((CareCluesChatApplication) application).getChatDatabase().roomMemberDao().findByRoomId(entity.roomId);
                            MessageEntity lastMessage = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getLastMessage(entity.roomId);

                            if (lastMessage != null) {
                                adapterModel.updatedAt = lastMessage.updatedAt;
                            } else {
                                adapterModel.updatedAt = entity.updatedAt;
                            }

                            adapterModel.name = "New-Consultant";
                            if(memberEntities != null ){
                                for (RoomMemberEntity memberEntity : memberEntities) {
                                    RoomMemberModel memberModel = ModelEntityTypeConverter.roomMemberEntityToModel(memberEntity);
                                    if (memberEntity.userName.equalsIgnoreCase("api_admin") || memberEntity.userName.equalsIgnoreCase("bot-la2zewmltd") || memberEntity.userName.equalsIgnoreCase(((CareCluesChatApplication) application).getUserName())) {
                                        //adapterModel.name = "New-Consultant";
                                        continue;
                                    } else {
                                        adapterModel.name = memberEntity.name;
                                        adapterModel.userName = memberEntity.userName;
                                        break;
                                    }
                                }
                            }


                            if (entity.readOnly == false) {
                                adapterModel.status = application.getResources().getString(R.string.tc_status_ongoing);
                            } else {
                                if (adapterModel.name != null && adapterModel.name.equals("New-Consultant")) {

                                    String msgType = "";

                                    try {
                                        JSONObject jsonObject = new JSONObject(lastMessage.msg);
                                        msgType = jsonObject.optString("type");
                                        if (msgType.equals("unconfirmed_expiry")) {
                                            adapterModel.status = application.getResources().getString(R.string.tc_status_inactivity_expired);

                                        } else if (msgType.equals("unpaid_expiry")) {
                                            adapterModel.status = application.getResources().getString(R.string.tc_status_payment_expired);

                                        } else if (msgType.equals("rejected_by_physician")) {
                                            adapterModel.status = application.getResources().getString(R.string.tc_status_rejected);

                                        }
                                    } catch (Exception e) {

                                    }

                                } else {
                                    adapterModel.status = application.getResources().getString(R.string.tc_status_completed);
                                    ;
                                }
                            }

                            modelList.add(adapterModel);
                        }

                        handler.sendEmptyMessage(what);
                    }

                } catch (Throwable e) {
                    Log.e("RoomPresenter", "ERRORR!!!!!!!!!!!!!!" + e.toString());
                }

            }
        });

    }

    @Override
    public void createNewRoom() {
        roomDataPresenter.createNewRoom(false);
    }

    @Override
    public void getTextConsultant(String roomId) {
        roomDataPresenter.getTextConsultant(roomId);
    }


//    @Override
//    public void createNewRoom() {
//        String roomName = "TC-" + (System.currentTimeMillis() / 1000);
//        String[] members = {"api_admin", "bot-la2zewmltd"};
//        apiExecuter.createPrivateRoom(roomName, members, new ServiceCallBack<GroupResponseModel>(GroupResponseModel.class) {
//            @Override
//            public void onSuccess(GroupResponseModel response) {
//                Log.e("NEWROOM", "New Room : " + response.toString());
//                if (response.success) {
//                    setRoomTopic(response.group.id, "text-consultation");
//                }
//            }
//
//            @Override
//            public void onFailure(List<NetworkError> errorList) {
//
//            }
//        });
//
//    }
//
//    private void setRoomTopic(String roomId, String topic) {
//        apiExecuter.setRoomTopicw(roomId, topic, new ServiceCallBack<SetTopicResponseModel>(SetTopicResponseModel.class) {
//            @Override
//            public void onSuccess(SetTopicResponseModel response) {
//                Log.e("NEWROOM", "SetTopic : " + response.toString());
//            }
//
//            @Override
//            public void onFailure(List<NetworkError> errorList) {
//
//            }
//        });
//    }

    @Override
    public void getMessage(String roomId) {
        roomDataPresenter.fetchMessages(roomId, true);
    }

    @Override
    public void onMessage(String roomId, CcMessage message) {
        Log.e("ROOMUPDATE", "Room Update : " + roomId + "  ,  " + message);
        final MessageEntity messageEntity = ModelEntityTypeConverter.ccMessageToEntity(message);
        updateRoomMessageDb(messageEntity);
        view.updateRoomMessage(roomId, messageEntity);

    }
    

    @Override
    public void onCreateGroup(String roomId) {


    }

    @Override
    public void onError(CcRocketChatException error) {

    }


    private void subsCribeRoomMessageEvent(List<RoomEntity> roomList) {

        for (RoomEntity room : roomList) {
            CcChatRoomFactory roomFactory = chatClient.getChatRoomFactory();
            chatClient.subscribeRoomMessageEvent(room.roomId, true, null, RoomPresenter.this);
            chatClient.subscribeRoomTypingEvent(room.roomId, true, null, RoomPresenter.this);

        }


    }

    private void subscribeRoomChangeEvent(String userId) {
        Log.e("MMM"," subscribeRoomChangeEvent "+userId);
        chatClient.subscribeRoomChanged(userId,null,RoomPresenter.this);
    }

    private void subscribeSubscriptionChangeEvent(String userId) {
        Log.e("MMM"," subscribeRoomChangeEvent "+userId);
        chatClient.subscribeSubscriptionChangeEvent(userId,null,RoomPresenter.this);
    }

    private void subscribeMessage(String userId) {
        chatClient.subscribeMessageChangeEvent(userId,null,RoomPresenter.this);
    }


    private void updateRoomMessageDb(final MessageEntity messageEntity) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((CareCluesChatApplication) application).getChatDatabase().messageDao().addMessage(messageEntity);
                } catch (Throwable e) {
                    Log.e("DBERROR", " updateRoomMessageDb : " + e.toString());
                }

            }
        });
    }


    @Override
    public void onTyping(String roomId, String user, Boolean istyping) {
        view.onUserTyping(roomId, user, istyping);
    }

    private UserProfileResponseModel userProfileModel = null;
    public UserProfileResponseModel getUserProfile(String userId){
        apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.getUserProfile(userId, new ServiceCallBack<UserProfileResponseModel>(UserProfileResponseModel.class) {
            @Override
            public void onSuccess(UserProfileResponseModel response) {
                userProfileModel = response;
                AppConstant.userProfile = userProfileModel;
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                userProfileModel = null;
            }
        });
        return userProfileModel;
    }


    @Override
    public void onNewRoomCreated(boolean reConsult,GroupModel groupModel) {
    }


    @Override
    public void onNewRoom(String userId, final CcRoom roomModel,String type) {
        Log.e("MSG","HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        RoomEntity roomEntity;
        roomEntity = ModelEntityTypeConverter.ccroomToEntity(roomModel);
        List<RoomEntity> roomEntities = new ArrayList<>();
        roomEntities.add(roomEntity);
        roomDataPresenter.insertRoomRecordIntoDb(roomEntities);

        if(type.equals("inserted")){
            populateAdapterData(roomEntities,LOAD_MORE_ROOM_DATA);
            subsCribeRoomMessageEvent(roomEntities);


        }

//        String msg = "@bot-la2zewmltd introduce " + AppConstant.userProfile.data.firstName + " to text consultation";
//
//        if (apiExecuter == null)
//            apiExecuter = RestApiExecuter.getInstance();
//        apiExecuter.sendNewMessage(CcUtils.shortUUID(), roomEntity.roomId, msg, new ServiceCallBack<MessageResponseModel>(MessageResponseModel.class) {
//            @Override
//            public void onSuccess(MessageResponseModel response) {
//
//            }
//
//            @Override
//            public void onFailure(List<NetworkError> errorList) {
//
//            }
//        });
//
//        if( roomModel.topic != null && roomModel.topic.equalsIgnoreCase("text-consultation")
//                && roomModel.type.name() == BaseRoomModel.RoomType.PRIVATE.name()){
//            populateAdapterData(roomEntities,LOAD_MORE_ROOM_DATA);
//        }



    }

    @Override
    public void onNewSubscription(String userId, CcSubscription subscription, String type) {
        Log.e("MSG","SUBCRIPTIONSSSSSSSSSSSSSSSSSSSSSS");

        SubscriptionEntity subscriptionEntity;
        subscriptionEntity = ModelEntityTypeConverter.ccSubscriptionToEntity(subscription);
        List<SubscriptionEntity> subsEntities = new ArrayList<>();
        subsEntities.add(subscriptionEntity);
        roomDataPresenter.insertSubscriptionRecordIntoDb(subsEntities);

        if(type.equals("inserted")){

        }
    }
}
