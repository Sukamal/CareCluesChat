package careclues.careclueschat.feature.room;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.lightdb.collection.Collection;
import com.rocketchat.common.data.lightdb.document.UserDocument;
import com.rocketchat.common.data.model.BaseRoom;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.core.ChatRoom;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.MessageCallback;
import com.rocketchat.core.callback.RoomCallback;
import com.rocketchat.core.model.Message;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.ChatPresenter;
import careclues.careclueschat.feature.common.RoomDataPresenter;
import careclues.careclueschat.feature.login.model.LoginResponse;
import careclues.careclueschat.model.GroupResponseModel;
import careclues.careclueschat.model.MessageModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomMemberResponse;
import careclues.careclueschat.model.RoomModel;
import careclues.careclueschat.model.SetTopicResponseModel;
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
import careclues.rocketchat.CcChatRoom;
import careclues.rocketchat.CcRocketChatClient;
import careclues.rocketchat.CcSocket;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.callback.CcRoomCallback;
import careclues.rocketchat.common.CcRocketChatException;
import careclues.rocketchat.listner.CcChatRoomFactory;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcToken;

public class RoomPresenter implements RoomContract.presenter,
        RoomDataPresenter.FetchRoomMemberHistoryListner,
        RoomDataPresenter.FetchLastUpdatedRoomDbListner,
        RoomDataPresenter.FetchMessageListner,
        RoomDataPresenter.FetchRoomListner,
        RoomDataPresenter.FetchSubscriptionListner,

        CcConnectListener,
        CcMessageCallback.SubscriptionCallback,
        CcRoomCallback.GroupCreateCallback

        /*ConnectListener,
        MessageCallback.SubscriptionCallback,
        RoomCallback.GroupCreateCallback*/


{


    private final int LOG_IN_SUCCESS = 1;
    private final int LOG_IN_FAIL = 2;
    private final int LOAD_ROOM_DATA = 3;
    private final int LOAD_MORE_ROOM_DATA = 4;

    private boolean isFirstTimeLoad = true;

    private RoomContract.view view;
    private Application application;
//    private RocketChatClient chatClient;
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

        initWebSocket();

    }

    private void initWebSocket(){
//      api.getWebsocketImpl().getConnectivityManager().register(RoomPresenter.this);
        chatClient = ((CareCluesChatApplication) application).getRocketChatClient();
        chatClient.connect(this);

    }



    @Override
    public void doLogin(String userId,String password) {
        if(chatClient.getWebsocketImpl().getSocket().getState() == CcSocket.State.CONNECTED){
            chatClient.login(userId, password, new CcLoginCallback() {
                @Override
                public void onLoginSuccess(CcToken token) {
                    ((CareCluesChatApplication) application).setToken(token.getAuthToken());
                    ((CareCluesChatApplication) application).setUserId(token.getUserId());
                    ((CareCluesChatApplication) application).setUserName(AppConstant.USER_NAME);
                    doApiLogin(AppConstant.USER_NAME, AppConstant.PASSWORD);
                    view.displayMessage(token.getUserId());

                }

                @Override
                public void onError(CcRocketChatException error) {

                }
            });
        }

    }



    @Override
    public void reconnectToServer() {
        view.displayMessage("reconnectToServer");
        chatClient.getWebsocketImpl().getSocket().reconnect();
    }

    @Override
    public void disconnectToServer() {
        view.displayMessage("disconnectToServer");
//        chatClient.getWebsocketImpl().getConnectivityManager().unRegister(this);
    }

    @Override
    public void onConnect(String sessionID) {
        view.displayMessage(application.getString(R.string.connected));
        doLogin(AppConstant.USER_NAME, AppConstant.PASSWORD);
    }

    @Override
    public void onDisconnect(boolean closedByServer) {
        reconnectToServer();
//        view.onConnectionFaild(2);
    }

    @Override
    public void onConnectError(Throwable websocketException) {
//        view.onConnectionFaild(1);
//        reconnectToServer();
    }


    //-------------------------------------------------LOAD BASIC DATA----------------------------------------------------------------------------------


    @Override
    public void onFetchRoom(List<RoomEntity> entities) {
        Toast.makeText(application, "onFetchRoom", Toast.LENGTH_SHORT).show();
        roomDataPresenter.getSubscription(null,entities);
    }


    @Override
    public void onFetchSubscription(List<SubscriptionEntity> entities) {
        Toast.makeText(application, "onFetchSubscription", Toast.LENGTH_SHORT).show();

        roomDataPresenter.getUpdatedRoomList(10);
    }

    @Override
    public void onFetchDbUpdatedRoom(List<RoomEntity> entities) {
        if(isFirstTimeLoad){
            roomDataPresenter.fetchMemberAndMessage(entities);
        }else{
            populateAdapterData(entities,LOAD_ROOM_DATA);
        }

    }

    @Override
    public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities) {
        if(isFirstTimeLoad){
            isFirstTimeLoad = false;
            view.onLoginSuccess();
        }else{
            populateAdapterData(moreList,LOAD_MORE_ROOM_DATA);

        }


    }

    @Override
    public void doApiLogin(String userId,String password){

//        careclues.rocketchat.CcRocketChatClient chatClient = new careclues.rocketchat.CcRocketChatClient();
//        chatClient.websocketImpl.login(userId,password);

        apiExecuter.doLogin(userId, password, new ServiceCallBack<LoginResponse>(LoginResponse.class) {
            @Override
            public void onSuccess(LoginResponse response) {
//                ((CareCluesChatApplication) application).setToken(response.getData().getAuthToken());
                RestApiExecuter.getInstance().getAuthToken().saveToken(response.getData().getUserId(),response.getData().getAuthToken());
//                System.out.println("Api Response : "+response.toString());
                getLoginUserDetails(response.getData().getUserId());
                handler.sendEmptyMessage(LOG_IN_SUCCESS);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                handler.sendEmptyMessage(LOG_IN_FAIL);
            }
        });

    }

    private void getLoginUserDetails(final String userId){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                RoomMemberEntity userDetails = ((CareCluesChatApplication)application).getChatDatabase().roomMemberDao().findById(userId);
                if(userDetails != null){
                    ((CareCluesChatApplication) application).setUserName(userDetails.userName);
                }
            }
        });
    }





//    @Override
//    public void onFetchDbUpdatedRoom(List<RoomEntity> entities) {
//        populateAdapterData(entities,LOAD_ROOM_DATA);
//    }
//
//    @Override
//    public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities) {
//        populateAdapterData(moreList,LOAD_MORE_ROOM_DATA);
//    }

    private void handleMessage(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
                    case LOG_IN_SUCCESS:
                        String lastUpdate = appPreference.getStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name());
                        roomDataPresenter.getRoom(lastUpdate);
                        break;
                    case LOG_IN_FAIL:

                        break;
                    case LOAD_ROOM_DATA:
                        view.displyRoomList(modelList);
                        break;
                    case LOAD_MORE_ROOM_DATA:
                        view.displyMoreRoomList(modelList);
                        break;
                }
            }
        };
    }

    @Override
    public void getOpenRoom() {
        modelList = new ArrayList<>();
//        roomDataPresenter.getUpdatedRoomList(10);
        getOpenRoomDb();
    }

    public  void getOpenRoomDb(){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {

//                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getOpenRoomList();
                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getActiveRoomList();
                    subsCribeRoomMessageEvent(lastUpdatedRoomList);
                    populateAdapterData(lastUpdatedRoomList,LOAD_ROOM_DATA);
                } catch (Throwable e) {
                    Log.e("DBERROR", e.toString());
                }

            }
        });
    }

    private void populateAdapterData(final List<RoomEntity> rooms,final int what) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    modelList = new ArrayList<>();
                    if (rooms != null) {
                        for (final RoomEntity entity : rooms) {
                            adapterModel = new RoomAdapterModel();
                            adapterModel.Id = entity.roomId;
                            adapterModel.description = entity.description;
                            List<RoomMemberEntity> memberEntities = ((CareCluesChatApplication) application).getChatDatabase().roomMemberDao().findByRoomId(entity.roomId);
                            List<MessageEntity> messageEntities = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getChatMessage(entity.roomId, 1);

                            if (messageEntities != null && messageEntities.size() > 0) {
                                adapterModel.updatedAt = messageEntities.get(0).updatedAt;
                            }else{
                                adapterModel.updatedAt = entity.updatedAt;
                            }

                            for (RoomMemberEntity memberEntity : memberEntities) {
                                RoomMemberModel memberModel = ModelEntityTypeConverter.roomMemberEntityToModel(memberEntity);
                                if (memberEntity.userName.equalsIgnoreCase("api_admin") || memberEntity.userName.equalsIgnoreCase("bot-la2zewmltd") || memberEntity.userName.equalsIgnoreCase(((CareCluesChatApplication) application).getUserName())) {
                                    adapterModel.name = "New-Consultant";
                                } else {
                                    adapterModel.name = memberEntity.name;
                                    adapterModel.userName = memberEntity.userName;
                                    break;
                                }
                            }

                            if(entity.readOnly == false){
                                adapterModel.status = application.getResources().getString(R.string.tc_status_ongoing);;
                            }else{
                                if(adapterModel.name.equals("New-Consultant")){

                                    String msgType = "";

                                    MessageEntity lastMessage = ((CareCluesChatApplication) application).getChatDatabase().messageDao().getLastMessage(entity.roomId);
                                    try{
                                        JSONObject jsonObject = new JSONObject(lastMessage.msg);
                                        msgType = jsonObject.optString("type");
                                        if(msgType.equals("unconfirmed_expiry")){
                                            adapterModel.status = application.getResources().getString(R.string.tc_status_inactivity_expired);

                                        }else if(msgType.equals("unpaid_expiry")){
                                            adapterModel.status = application.getResources().getString(R.string.tc_status_payment_expired);

                                        }else if(msgType.equals("rejected_by_physician")){
                                            adapterModel.status = application.getResources().getString(R.string.tc_status_rejected);
                                            ;

                                        }
                                    }catch (Exception e){

                                    }

                                }else{
                                    adapterModel.status = application.getResources().getString(R.string.tc_status_completed);;
                                }
                            }

                            modelList.add(adapterModel);
                        }

                        handler.sendEmptyMessage(what);
                    }

                } catch (Throwable e) {
                    Log.e("RoomPresenter","ERRORR!!!!!!!!!!!!!!" + e.toString());
                }

            }
        });

    }

    @Override
    public void getMoreRoom(final int startCount, final int threshold) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {

//                moreList = ((CareCluesChatApplication)application).getChatDatabase().roomDao().getClosedRoomList(startCount,threshold);
                moreList = ((CareCluesChatApplication)application).getChatDatabase().roomDao().getNextRoomList(startCount,threshold);
                subsCribeRoomMessageEvent(moreList);

                if(moreList != null && moreList.size() > 0){
                    roomDataPresenter.fetchMemberAndMessage(moreList);
                }
            }
        });
    }

//    @Override
//    public void reconnectToServer() {
//    }
//
//    @Override
//    public void disconnectToServer() {
//    }

    @Override
    public void createNewRoom() {
        String roomName = "SUKU-TEST-" + (System.currentTimeMillis() / 1000);
        String[] members = {"api_admin", "bot-la2zewmltd", "sachu-985"};
        apiExecuter.createPrivateRoom(roomName, members, new ServiceCallBack<GroupResponseModel>(GroupResponseModel.class) {
            @Override
            public void onSuccess(GroupResponseModel response) {
                Log.e("NEWROOM","New Room : " + response.toString());
                if(response.success){
                    setRoomTopic(response.group.id,"text-consultation");
                }
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });

    }

    private void setRoomTopic(String roomId,String topic){
        apiExecuter.setRoomTopicw(roomId, topic, new ServiceCallBack<SetTopicResponseModel>(SetTopicResponseModel.class) {
            @Override
            public void onSuccess(SetTopicResponseModel response) {
                Log.e("NEWROOM","SetTopic : " + response.toString());
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    @Override
    public void getMessage(String roomId) {
        roomDataPresenter.registerMessageListner(this);
        roomDataPresenter.fetchMessages(roomId);
    }

    @Override
    public void onFetchMessage(String roomId) {
        view.displyChatScreen(roomId);
    }

  /*  @Override
    public void onConnect(String sessionID) {
        view.displayMessage(application.getString(R.string.connected));
    }

    @Override
    public void onDisconnect(boolean closedByServer) {
        view.onConnectionFaild(2);
    }

    @Override
    public void onConnectError(Throwable websocketException) {
        view.onConnectionFaild(1);
    }

    @Override
    public void onCreateGroup(String roomId) {

    }

    @Override
    public void onError(RocketChatException error) {

    }

    @Override
    public void onMessage(String roomId, Message message) {

    }*/

    @Override
    public void onMessage(String roomId, CcMessage message) {
        Log.e("ROOMUPDATE","Room Update : " + roomId + "  ,  " + message);
//        view.displayMessage("Room Update : " + roomId + "  ,  " + message.msg);
        view.updateRoomMessage(roomId);

    }

    @Override
    public void onCreateGroup(String roomId) {

    }

    @Override
    public void onError(CcRocketChatException error) {

    }


    private void subsCribeRoomMessageEvent(List<RoomEntity> roomList){

        for(RoomEntity room : roomList ){
            CcChatRoomFactory roomFactory = chatClient.getChatRoomFactory();
//            roomFactory.addChatRoom()
//            addChatRoom
//            CcChatRoom chatRoom = .getChatRoomById(room.roomId);
//            chatRoom.subscribeRoomMessageEvent(null, RoomPresenter.this);

            chatClient.subscribeRoomMessageEvent(room.roomId,
                    true, null, RoomPresenter.this);
        }

    }



}
