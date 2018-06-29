package careclues.careclueschat.feature.chat;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.lightdb.collection.Collection;
import com.rocketchat.common.data.lightdb.document.UserDocument;
import com.rocketchat.common.data.model.BaseRoom;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.listener.TypingListener;
import com.rocketchat.common.network.Socket;
import com.rocketchat.common.utils.Utils;
import com.rocketchat.core.ChatRoom;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.FileListener;
import com.rocketchat.core.callback.HistoryCallback;
import com.rocketchat.core.callback.LoginCallback;
import com.rocketchat.core.callback.MessageCallback;
import com.rocketchat.core.internal.rpc.MessageRPC;
import com.rocketchat.core.model.FileDescriptor;
import com.rocketchat.core.model.Message;
import com.rocketchat.core.model.Token;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.User;
import careclues.careclueschat.model.BaseUserModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.util.AppUtil;
import careclues.rocketchat.CcChatRoom;
import careclues.rocketchat.CcRocketChatClient;
import careclues.rocketchat.CcSocket;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.common.CcRocketChatException;
import careclues.rocketchat.listner.CcConnectListener;
import careclues.rocketchat.listner.CcTypingListener;
import careclues.rocketchat.models.CcBaseRoom;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcToken;
import careclues.rocketchat.models.CcUser;

public class ChatPresenter implements ChatContract.presenter,

        CcConnectListener,
        CcMessageCallback.SubscriptionCallback,
        CcTypingListener

        /*ConnectListener,
        MessageCallback.SubscriptionCallback,
        TypingListener*/ {

    private ChatContract.view view;
    private Application application;
    private String userId;
    private RoomMemberEntity userDetails;
    private String roomId;

    private AtomicInteger integer;


//    private RocketChatClient api;
//    private ChatRoom chatRoom;

    private CcRocketChatClient api;
    private CcChatRoom chatRoom;



    public ChatPresenter(ChatContract.view view,String roomId, Application application){
        this.view = view;
        this.application = application;
        this.roomId = roomId;
        integer = new AtomicInteger(1);

        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
        getLoginUserDetails(userId);


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

   /* private void initWebSoket(){
        api = ((CareCluesChatApplication) application).getRocketChatAPI();
        api.getWebsocketImpl().getConnectivityManager().register(ChatPresenter.this);

        List<BaseRoom> rooms = new ArrayList<>();
        BaseRoom  baseRoom;
        baseRoom  = new BaseRoom() {
            @Override
            public String roomId() {
                return roomId;
            }

            @Nullable
            @Override
            public RoomType type() {
                return RoomType.PRIVATE;
            }

            @Nullable
            @Override
            public com.rocketchat.common.data.model.User user() {
                return new com.rocketchat.common.data.model.User() {
                    @Nullable
                    @Override
                    public String id() {
//                        return "2eRbrjSZnsACYkRx4";
                        return userDetails.Id;
                    }

                    @Nullable
                    @Override
                    public String username() {
//                        return "sachu-985";
                        return userDetails.userName;
                    }

                    @Nullable
                    @Override
                    public List<String> roles() {
                        return null;
                    }
                };
            }

            @Nullable
            @Override
            public String name() {
                return null;
            }
        };
        rooms.add(baseRoom);
        api.getChatRoomFactory().createChatRooms(rooms);
        api.getDbManager().getUserCollection().register(userId, new Collection.Observer<UserDocument>() {
            @Override
            public void onUpdate(Collection.Type type, UserDocument document) {
                switch (type) {
                    case ADDED:
                        break;
                    case CHANGED:
                        break;
                    case REMOVED:
                        break;
                }
            }
        });
        chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);
        chatRoom.subscribeRoomMessageEvent(null, ChatPresenter.this);
        chatRoom.subscribeRoomTypingEvent(null, ChatPresenter.this);

    }*/

    private void initWebSoket(){
        api = ((CareCluesChatApplication) application).getRocketChatClient();
        api.getWebsocketImpl().getConnectivityManager().register(ChatPresenter.this);
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
        chatRoom.subscribeRoomMessageEvent(null, ChatPresenter.this);
        chatRoom.subscribeRoomTypingEvent(null, ChatPresenter.this);

    }

    public void deregisterSocket(){
//        chatRoom.unSubscribeAllEvents();
    }


    @Override
    public void loadData(int count) {
        getChatHistory(roomId,count);
    }

    @Override
    public void sendMessage(String msg) {

//       MessageEntity messageEntity =  insertMessageIntoDB(msg);
//        List<ChatMessageModel> list = new ArrayList<>();
//        ChatMessageModel chatMessageModel = new ChatMessageModel(messageEntity.Id,messageEntity.msg,messageEntity.updatedAt,messageEntity.user.id);
//        list.add(chatMessageModel);
//        view.displayMoreChatList(list);
//
//        api.getWebsocketImpl().getSocket().sendData(MessageRPC.sendMessage(integer.getAndIncrement(), messageEntity.Id, messageEntity.rId,messageEntity.msg));


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
                    System.out.println("Error111111111111111111111111111111111");
                }

            }
        });
    }

    @Override
    public void onConnect(String sessionID) {
        view.displayMessage(application.getString(R.string.connected));
        String token = ((CareCluesChatApplication)application).getToken();
//        String token = RestApiExecuter.getInstance().getAuthToken().getToken();
        if (api.getWebsocketImpl().getSocket().getState() == CcSocket.State.CONNECTED) {
            api.loginUsingToken(token, new CcLoginCallback() {
                @Override
                public void onError(CcRocketChatException error) {

                }

                @Override
                public void onLoginSuccess(CcToken token) {
                    chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);
                    chatRoom.subscribeRoomMessageEvent(null, ChatPresenter.this);
                    chatRoom.subscribeRoomTypingEvent(null, ChatPresenter.this);
                }
//                @Override
//                public void onLoginSuccess(Token token) {
//                    chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);
//                    chatRoom.subscribeRoomMessageEvent(null, ChatPresenter.this);
//                    chatRoom.subscribeRoomTypingEvent(null, ChatPresenter.this);
//
//                }
//
//                @Override
//                public void onError(RocketChatException error) {
//                    System.out.println("Connection Error : " + error.toString());
//                }
            });
        }else{
            api.getWebsocketImpl().getSocket().reconnect();
        }
    }

    @Override
    public void onDisconnect(boolean closedByServer) {
        view.onConnectionFaild(2);
    }

    @Override
    public void onConnectError(Throwable websocketException) {

    }

//    @Override
//    public void onMessage(String roomId, Message message) {
//
//        Log.e("Message"," On Message : "+ message.id() + " " +message.toString());
//
//        insertIntoDB(message);
//        List<ChatMessageModel> list = new ArrayList<>();
//        ChatMessageModel chatMessageModel = new ChatMessageModel(message.id(),message.message(),new Date(message.updatedAt()),message.sender().id());
//        list.add(chatMessageModel);
//        view.displayMoreChatList(list);
//
//    }

    @Override
    public void onMessage(String roomId, CcMessage message) {

        Log.e("Message"," On Message : "+ message.id + " " +message.toString());

//        insertIntoDB(message);
        List<ChatMessageModel> list = new ArrayList<>();
        ChatMessageModel chatMessageModel = new ChatMessageModel(message.id,message.msg,new Date(/*message.updatedAt*/),message.user.id);
        list.add(chatMessageModel);
        view.displayMoreChatList(list);

    }

    @Override
    public void onTyping(String roomId, String user, Boolean istyping) {

        if(istyping){
            view.displyTypingStatus(user + " is typing...");
        }else{
            view.displyTypingStatus("");

        }
    }

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


}
