package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import careclues.careclueschat.model.BaseMessageModel;
import careclues.careclueschat.model.BaseUserModel;
import careclues.careclueschat.model.UrlModel;

/**
 * Created by SukamalD on 6/3/2018.
 */

public class CcMessage extends CcBaseMessage{


    @SerializedName("groupable")
    public Boolean groupable;

    @SerializedName("urls")
    public List<UrlModel> urls = null;

    @SerializedName("mentions")
    public List<BaseUserModel> mentions;

//    @SerializedName("attachments")
//    public List<Object> attachments = null;
//
//    @SerializedName("channels")
//    public List<Object> channels = null;

    @SerializedName("parseUrls")
    public Boolean parseUrls;

    @SerializedName("meta")
    public String meta;

    @SerializedName("starred")
    public List<CcUser> starred;


    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n\n---------------Message Start-------------------\n\n");
        stringBuffer.append("\nid : "+id);
        stringBuffer.append("\nroomId : "+rId);
        stringBuffer.append("\nmsg : "+msg);
//        stringBuffer.append("\ntimeStamp : "+timeStamp);
        stringBuffer.append("\nuser : "+user.toString());
//        stringBuffer.append("\nupdatedAt : "+updatedAt);
        stringBuffer.append("\ntype : "+type);
        stringBuffer.append("\nalias : "+alias);
        stringBuffer.append("\ngroupable : "+groupable);
        stringBuffer.append("\nurls : "+urls);
        stringBuffer.append("\nmentions : "+mentions);
        stringBuffer.append("\nparseUrls : "+parseUrls);
        stringBuffer.append("\nmeta : "+meta);
        stringBuffer.append("\n\n---------------Message End-------------------\n\n");
        return stringBuffer.toString();
    }

    public enum MessageType {
        TEXT,
        ATTACHMENT,
        MESSAGE_EDITED,
        MESSAGE_STARRED,
        MESSAGE_REACTION,
        MESSAGE_REMOVED,
        ROOM_NAME_CHANGED,
        ROOM_ARCHIVED,
        ROOM_UNARCHIVED,
        USER_ADDED,
        USER_REMOVED,
        USER_JOINED,
        USER_LEFT,
        USER_MUTED,
        USER_UNMUTED,
        WELCOME,
        SUBSCRIPTION_ROLE_ADDED,
        SUBSCRIPTION_ROLE_REMOVED,
        OTHER
    }

    private static final String TYPE_MESSAGE_REMOVED = "rm";
    private static final String TYPE_ROOM_NAME_CHANGED = "r";
    private static final String TYPE_ROOM_ARCHIVED = "room-archived";
    private static final String TYPE_ROOM_UNARCHIVED = "room-unarchived";
    private static final String TYPE_USER_ADDED = "au";
    private static final String TYPE_USER_REMOVED = "ru";
    private static final String TYPE_USER_JOINED = "uj";
    private static final String TYPE_USER_LEFT = "ul";
    private static final String TYPE_USER_MUTED = "user-muted";
    private static final String TYPE_USER_UNMUTED = "user-unmuted";
    private static final String TYPE_WELCOME = "wm";
    private static final String TYPE_SUBSCRIPTION_ROLE_ADDED = "subscription-role-added";
    private static final String TYPE_SUBSCRIPTION_ROLE_REMOVED = "subscription-role-removed";

    // TODO: 22/8/17 Try sending each type of message and test it accordingly
    public MessageType getMsgType() {
        if (type != null && !type.equals("")) {
            return getType(type);
        } else {
            if (user != null) {
                return MessageType.MESSAGE_EDITED;
            } else if (starred != null) {
                return MessageType.MESSAGE_STARRED;
                // TODO - implement reactions and attachments...
            /*} else if (reactions != null) {
                return MessageType.MESSAGE_REACTION;
            } else if (attachments != null) {
                return MessageType.ATTACHMENT;*/
            } else {
                return MessageType.TEXT;
            }
        }
    }

    private static MessageType getType(String s) {
        switch (s) {
            case TYPE_MESSAGE_REMOVED:
                return MessageType.MESSAGE_REMOVED;
            case TYPE_ROOM_NAME_CHANGED:
                return MessageType.ROOM_NAME_CHANGED;
            case TYPE_ROOM_ARCHIVED:
                return MessageType.ROOM_ARCHIVED;
            case TYPE_ROOM_UNARCHIVED:
                return MessageType.ROOM_UNARCHIVED;
            case TYPE_USER_ADDED:
                return MessageType.USER_ADDED;
            case TYPE_USER_REMOVED:
                return MessageType.USER_REMOVED;
            case TYPE_USER_JOINED:
                return MessageType.USER_JOINED;
            case TYPE_USER_LEFT:
                return MessageType.USER_LEFT;
            case TYPE_USER_MUTED:
                return MessageType.USER_MUTED;
            case TYPE_USER_UNMUTED:
                return MessageType.USER_UNMUTED;
            case TYPE_WELCOME:
                return MessageType.WELCOME;
            case TYPE_SUBSCRIPTION_ROLE_ADDED:
                return MessageType.SUBSCRIPTION_ROLE_ADDED;
            case TYPE_SUBSCRIPTION_ROLE_REMOVED:
                return MessageType.SUBSCRIPTION_ROLE_REMOVED;
        }
        return MessageType.OTHER;
    }

}
