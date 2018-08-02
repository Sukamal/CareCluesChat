package careclues.careclueschat.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.MessageModel;
import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomModel;
import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.model.SubscriptionModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcRoom;
import careclues.rocketchat.models.CcSubscription;

public class ModelEntityTypeConverter {

    public static RoomEntity ccroomToEntity(CcRoom data) {
        if (data == null) {
            return null;
        }
        RoomEntity roomEntity = new RoomEntity();

        roomEntity.roomId = data.Id;
        roomEntity.type = data.type.name();

        RoomUserModel userModel = new RoomUserModel();
        userModel.userName = data.user.userName;
        userModel.name = data.user.name;
        userModel.id = data.user.id;

        roomEntity.user = userModel;
//        roomEntity.userId = data.user.id;
//        roomEntity.userName = data.user.userName;
        roomEntity.roomName = data.name;
        roomEntity.roomFname = data.fName;
        roomEntity.topic = data.topic;
        roomEntity.updatedAt = RoomUserTypeConverter.toDate(data.updatedAt.date);
        roomEntity.description = data.description;
        roomEntity.readOnly = data.readOnly;

        return roomEntity;
    }

    public static RoomEntity roomModelToEntity(RoomModel data) {
        if (data == null) {
            return null;
        }
        RoomEntity roomEntity = new RoomEntity();

        roomEntity.roomId = data.Id;
        roomEntity.type = data.type.name();
        roomEntity.user = data.user;
//        roomEntity.userId = data.user.id;
//        roomEntity.userName = data.user.userName;
        roomEntity.roomName = data.name;
        roomEntity.roomFname = data.fName;
        roomEntity.topic = data.topic;
        roomEntity.updatedAt = data.updatedAt;
        roomEntity.description = data.description;
        roomEntity.readOnly = data.readOnly;

        return roomEntity;
    }

    public static RoomModel roomEntityToModel(RoomEntity data) {
        if(data != null){
            RoomModel model = new RoomModel();
            model.Id = data.roomId;
            model.type = BaseRoomModel.RoomType.valueOf(data.type);
            model.user = data.user;
//            model.user.id = data.userId;
//            model.user.userName = data.userName;
            model.name = data.roomName;
            model.fName = data.roomFname;
            model.topic = data.topic;
            model.updatedAt = data.updatedAt;
            model.description = data.description;
            model.readOnly = data.readOnly;
            return model;
        }else{
            return null;
        }

    }


    public static SubscriptionEntity ccSubscriptionToEntity(CcSubscription data) {
        if (data == null) {
            return null;
        }
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        subscriptionEntity.Id  = data.Id;
        subscriptionEntity.type  = data.type.name();
        RoomUserModel userModel = new RoomUserModel();
        userModel.userName = data.user.userName;
        userModel.name = data.user.name;
        userModel.id = data.user.id;
        subscriptionEntity.user  = userModel;
        subscriptionEntity.name  = data.name;
        subscriptionEntity.rId  = data.rid;
        subscriptionEntity.timeStamp  = RoomUserTypeConverter.toDate(data.ts.date);
        subscriptionEntity.lastSeen  = RoomUserTypeConverter.toDate(data.ls.date);
        subscriptionEntity.open  = data.open;
        subscriptionEntity.alert  = data.alert;
        subscriptionEntity.updatedAt  = RoomUserTypeConverter.toDate(data._updatedAt.date);
        subscriptionEntity.unread  = data.unread;
        subscriptionEntity.userMentions  = data.userMentions;
        subscriptionEntity.groupMentions  = data.groupMentions;

        return subscriptionEntity;
    }


    public static SubscriptionEntity subscriptionModelToEntity(SubscriptionModel data) {
        if (data == null) {
            return null;
        }
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        subscriptionEntity.Id  = data.Id;
        subscriptionEntity.type  = data.type.name();
        subscriptionEntity.user  = data.user;
        /*subscriptionEntity.userId  = data.user.id;
        subscriptionEntity.userName  = data.user.userName;*/
        subscriptionEntity.name  = data.name;
        subscriptionEntity.rId  = data.rId;
        subscriptionEntity.timeStamp  = data.timeStamp;
        subscriptionEntity.lastSeen  = data.lastSeen;
        subscriptionEntity.open  = data.open;
        subscriptionEntity.alert  = data.alert;
        subscriptionEntity.updatedAt  = data.updatedAt;
        subscriptionEntity.unread  = data.unread;
        subscriptionEntity.userMentions  = data.userMentions;
        subscriptionEntity.groupMentions  = data.groupMentions;

        return subscriptionEntity;
    }

    public static SubscriptionModel subscriptionEntityToModel(SubscriptionEntity data) {
        if(data != null){
            SubscriptionModel model = new SubscriptionModel();

            model.Id  = data.Id;
            model.type  = BaseRoomModel.RoomType.valueOf(data.type);
            model.user  = data.user;
//            model.user.id  = data.userId;
//            model.user.userName = data.userName;
            model.name  = data.name;
            model.rId  = data.rId;
            model.timeStamp  = data.timeStamp;
            model.lastSeen  = data.lastSeen;
            model.open  = data.open;
            model.alert  = data.alert;
            model.updatedAt  = data.updatedAt;
            model.unread  = data.unread;
            model.userMentions  = data.userMentions;
            model.groupMentions  = data.groupMentions;

            return model;
        }else{
            return null;
        }

    }

    public static MessageEntity messageModelToEntity(MessageModel data) {
        if (data == null) {
            return null;
        }
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.Id = data.id;
        messageEntity.rId = data.rId;
        messageEntity.msg = data.msg;
        messageEntity.timeStamp = data.timeStamp;
        messageEntity.user = data.user;
        messageEntity.updatedAt = data.updatedAt;
        messageEntity.type = data.type;
        messageEntity.alias = data.alias;
        messageEntity.groupable = data.groupable;
        messageEntity.mentions = data.mentions;
        messageEntity.parseUrls = data.parseUrls;
        messageEntity.meta = data.meta;
        messageEntity.synced = data.synced;

        return messageEntity;
    }

    public static MessageModel messageEntityToModel(MessageEntity data) {
        if(data != null){

            MessageModel model = new MessageModel();
            model.id = data.Id;
            model.rId = data.rId;
            model.msg = data.msg;
            model.timeStamp = data.timeStamp;
//            model.user = data.user;
            model.updatedAt = data.updatedAt;
            model.type = data.type;
            model.alias = data.alias;
            model.groupable = data.groupable;
//            model.mentions = data.mentions;
            model.parseUrls = data.parseUrls;
            model.meta = data.meta;
            model.synced = data.synced;
            return model;
        }else{
            return null;
        }

    }

    public static RoomMemberEntity roomMemberToEntity(RoomMemberModel data) {
        if (data == null) {
            return null;
        }
        RoomMemberEntity memberEntity = new RoomMemberEntity();
        memberEntity.Id = data.id;
        memberEntity.userName = data.userName;
        memberEntity.name = data.name;
        memberEntity.status = data.status;
        memberEntity.utcOffset = data.utcOffset;


        return memberEntity;
    }

    public static RoomMemberModel roomMemberEntityToModel(RoomMemberEntity data) {
        if(data != null){

            RoomMemberModel model = new RoomMemberModel();
            model.id = data.Id ;
            model.userName = data.userName ;
            model.name = data.name ;
            model.status = data.status;
            model.utcOffset = data.utcOffset;
            return model;
        }else{
            return null;
        }

    }

    public static MessageEntity ccMessageToEntity(CcMessage data) {
        if (data == null) {
            return null;
        }
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.Id = data.id;
        messageEntity.rId = data.rId;
        messageEntity.msg = data.msg;
        messageEntity.timeStamp = new Date(data.timeStamp.date);

        RoomUserModel roomUserModel = new RoomUserModel();
        roomUserModel.id = data.user.id;
        roomUserModel.name = data.user.name;
        roomUserModel.userName = data.user.userName;
        messageEntity.user = roomUserModel;
        messageEntity.updatedAt = new Date(data.updatedAt.date);
        messageEntity.type = data.type;
        messageEntity.alias = data.alias;
        messageEntity.groupable = data.groupable;
        messageEntity.mentions = data.mentions;
        messageEntity.parseUrls = data.parseUrls;
        messageEntity.meta = data.meta;
        messageEntity.synced = true;

        return messageEntity;
    }



}
