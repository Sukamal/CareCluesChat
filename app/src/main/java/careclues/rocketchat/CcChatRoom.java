package careclues.rocketchat;

import java.util.Date;

import careclues.rocketchat.callback.CcHistoryCallback;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.common.CcCoreStreamMiddleware;
import careclues.rocketchat.listner.CcSubscribeListener;
import careclues.rocketchat.listner.CcTypingListener;
import careclues.rocketchat.models.CcBaseRoom;

public class CcChatRoom {
    private final CcRocketChatClient client;
    private final CcBaseRoom room;

    //Subscription Ids for new subscriptions
    private String roomSubId;  // TODO: 29/7/17 check for persistent SubscriptionId of the room
    private String typingSubId;

    public CcChatRoom(CcRocketChatClient api, CcBaseRoom room) {
        this.client = api;
        this.room = room;
    }

//    public Boolean isSubscriptionObject() {
////        return room instanceof Subscription;
//    }

    public CcBaseRoom getRoomData() {
        return room;
    }


    //RPC methods

//    public void getRoomRoles(SimpleListCallback<RoomRole> callback) {
//        client.getRoomRoles(room.roomId(), callback);
//    }
//
    public void getChatHistory(int limit, Date oldestMessageTimestamp, Date lasttimestamp,
                               CcHistoryCallback callback) {
        client.getChatHistory(room.Id, limit, oldestMessageTimestamp, lasttimestamp, callback);
    }
//
//    public void getMembers(RoomCallback.GetMembersCallback callback) {
//        client.getRoomMembers(room.roomId(), false, callback);
//    }
//
//    public void sendIsTyping(Boolean istyping) {
//        client.sendIsTyping(room.roomId(), client.getMyUserName(), istyping);
//    }
//
    public void sendMessage(String message) {
        client.sendMessage(CcUtils.shortUUID(), room.Id, message, null);
    }

    public void sendMessage(String message, CcMessageCallback.MessageAckCallback callback) {
        client.sendMessage(CcUtils.shortUUID(), room.Id, message, callback);
    }
//
//    // TODO: 27/7/17 Need more attention on replying to message
//    private void replyMessage(Message msg, String message,
//                              MessageCallback.MessageAckCallback callback) {
//            /*message = "[ ](?msg=" + msg.id() + ") @" + msg.sender().getUserName() + " " + message;
//            client.sendMessage(Utils.shortUUID(), room.roomId(), message, callback);*/
//    }
//
//    public void deleteMessage(String msgId, SimpleCallback callback) {
//        client.deleteMessage(msgId, callback);
//    }
//
//    public void updateMessage(String msgId, String message, SimpleCallback callback) {
//        client.updateMessage(msgId, room.roomId(), message, callback);
//    }
//
//    public void pinMessage(String messageId, SimpleCallback callback) {
//        client.pinMessage(messageId, callback);
//    }
//
//    @Deprecated
//    public void pinMessage(JSONObject message, SimpleCallback callback) {
//        client.pinMessage(message, callback);
//    }
//
//    public void unpinMessage(JSONObject message, SimpleCallback callback) {
//        client.unpinMessage(message, callback);
//    }
//
//    public void starMessage(String msgId, Boolean starred, SimpleCallback callback) {
//        client.starMessage(msgId, room.roomId(), starred, callback);
//    }
//
//    public void setReaction(String emojiId, String msgId, SimpleCallback callback) {
//        client.setReaction(emojiId, msgId, callback);
//    }
//
//    public void searchMessage(String message, int limit,
//                              SimpleListCallback<Message> callback) {
//        client.searchMessage(message, room.roomId(), limit, callback);
//    }
//
//    public void deleteGroup(SimpleCallback callback) {
//        client.deleteGroup(room.roomId(), callback);
//    }
//
//    public void archive(SimpleCallback callback) {
//        client.archiveRoom(room.roomId(), callback);
//    }
//
//    public void unarchive(SimpleCallback callback) {
//        client.unarchiveRoom(room.roomId(), callback);
//    }
//
//    public void leave(SimpleCallback callback) {
//        client.leaveGroup(room.roomId(), callback);
//    }
//
//    public void hide(SimpleCallback callback) {
//        client.hideRoom(room.roomId(), callback);
//    }
//
//    public void open(SimpleCallback callback) {
//        client.openRoom(room.roomId(), callback);
//    }
//
//    public void uploadFile(java.io.File file, String newName, String description, FileListener fileListener) {
//        FileUploader uploader = new FileUploader(client, file, newName, description,
//                this, fileListener);
//        uploader.startUpload();
//    }
//
//    public void sendFileMessage(FileDescriptor file, MessageCallback.MessageAckCallback callback) {
//        client.sendFileMessage(room.roomId(), file.getStore(), file.getFileId(),
//                file.getFileType(), file.getSize(), file.getFileName(), file.getDescription(),
//                file.getUrl(), callback);
//    }
//
//    public void setFavourite(Boolean isFavoutite, SimpleCallback callback) {
//        client.setFavouriteRoom(room.roomId(), isFavoutite, callback);
//    }
//
    //Subscription methods

    public void subscribeRoomMessageEvent(CcSubscribeListener subscribeListener,
                                          CcMessageCallback.SubscriptionCallback callback) {
        if (roomSubId == null) {
            roomSubId = client.subscribeRoomMessageEvent(room.Id,
                    true, subscribeListener, callback);
        }
    }

    public void subscribeRoomTypingEvent(CcSubscribeListener subscribeListener, CcTypingListener listener) {
        if (typingSubId == null) {
            typingSubId = client.subscribeRoomTypingEvent(room.Id, true, subscribeListener, listener);
        }
    }

    public void unSubscribeRoomMessageEvent(CcSubscribeListener subscribeListener) {
        if (roomSubId != null) {
            client.removeSubscription(room.Id, CcCoreStreamMiddleware.SubscriptionType.SUBSCRIBE_ROOM_MESSAGE);
            client.unsubscribeRoom(roomSubId, subscribeListener);
            roomSubId = null;
        }
    }

    public void unSubscribeRoomTypingEvent(CcSubscribeListener subscribeListener) {
        if (typingSubId != null) {
            client.removeSubscription(room.Id, CcCoreStreamMiddleware.SubscriptionType.SUBSCRIBE_ROOM_TYPING);
            client.unsubscribeRoom(typingSubId, subscribeListener);
            typingSubId = null;
        }
    }

    public void unSubscribeAllEvents() {
        client.removeAllSubscriptions(room.Id);
        unSubscribeRoomMessageEvent(null);
        unSubscribeRoomTypingEvent(null);
    }

}
