package careclues.careclueschat.feature.room;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.ChatPresenter;
import careclues.careclueschat.feature.common.RoomDataPresenter;
import careclues.careclueschat.model.GroupResponseModel;
import careclues.careclueschat.model.MessageModel;
import careclues.careclueschat.model.MessageResponseModel;
import careclues.careclueschat.model.RoomAdapterModel;
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
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.ModelEntityTypeConverter;

public class RoomPresenter implements RoomContract.presenter,
        RoomDataPresenter.FetchRoomMemberHistoryListner,
        RoomDataPresenter.FetchLastUpdatedRoomDbListner,
        RoomDataPresenter.FetchMessageListner,

        ConnectListener,MessageCallback.SubscriptionCallback,
        RoomCallback.GroupCreateCallback


{

    private RoomContract.view view;
    private Application application;
    private RocketChatClient chatClient;
    private List<RoomAdapterModel> modelList;
    private RestApiExecuter apiExecuter;
    private AppPreference appPreference;
    private Handler handler;
    private RoomAdapterModel adapterModel;
    private List<RoomEntity> moreList;
    private RoomDataPresenter roomDataPresenter;
    private List<RoomEntity> lastUpdatedRoomList;

    private RocketChatClient api;

    private final int LOAD_ROOM_DATA = 1;
    private final int LOAD_MORE_ROOM_DATA = 2;

    public RoomPresenter(RoomContract.view view, Application application) {
        this.view = view;
        this.application = application;
        apiExecuter = RestApiExecuter.getInstance();
        appPreference = ((CareCluesChatApplication) application).getAppPreference();
        handleMessage();

        roomDataPresenter = new RoomDataPresenter(application);
        roomDataPresenter.registerRoomMemberHistoryListner(this);
        roomDataPresenter.registerUpdatedRoomListner(this);
//        api.getWebsocketImpl().getConnectivityManager().register(RoomPresenter.this);
    }


    @Override
    public void onFetchDbUpdatedRoom(List<RoomEntity> entities) {
        populateAdapterData(entities,LOAD_ROOM_DATA);
    }

    @Override
    public void onFetchRoomMemberMessage(List<RoomMemberEntity> roomMemberEntities, List<MessageEntity> messageEntities) {
        populateAdapterData(moreList,LOAD_MORE_ROOM_DATA);
    }

    private void handleMessage(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
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
    public void getRoom() {
        modelList = new ArrayList<>();
//        roomDataPresenter.getUpdatedRoomList(10);
        getOpenRoom();
    }

    public  void getOpenRoom(){

        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lastUpdatedRoomList = ((CareCluesChatApplication) application).getChatDatabase().roomDao().getOpenRoomList();
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
                                if (memberEntity.userName.equalsIgnoreCase("api_admin") || memberEntity.userName.equalsIgnoreCase("bot-la2zewmltd") || memberEntity.userName.equalsIgnoreCase("sachu-985")) {
                                    adapterModel.name = "New-Consultant";
                                } else {
                                    adapterModel.name = memberEntity.name;
                                    break;
                                }
                            }
                            modelList.add(adapterModel);
                        }

                        handler.sendEmptyMessage(what);
                    }

                } catch (Throwable e) {
                    System.out.println("Error111111111111111111111111111111111");
                }

            }
        });

    }

    @Override
    public void getMoreRoom(final int startCount, final int threshold) {
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {

                moreList = ((CareCluesChatApplication)application).getChatDatabase().roomDao().getClosedRoomList(startCount,threshold);
                if(moreList != null && moreList.size() > 0){
                    roomDataPresenter.fetchMemberAndMessage(moreList);
                }
            }
        });
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

    @Override
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

    }
}
