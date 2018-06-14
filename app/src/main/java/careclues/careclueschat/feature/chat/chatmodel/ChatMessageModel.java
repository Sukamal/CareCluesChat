package careclues.careclueschat.feature.chat.chatmodel;

import java.util.Date;

import careclues.careclueschat.storage.database.entity.MessageEntity;

/**
 * Created by SukamalD on 6/14/2018.
 */

public class ChatMessageModel {
    public String id;
    public String text;
    public Date createdAt;
    public String userId;

    public ChatMessageModel(String id,String text,Date createdAt,String userId){
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public ChatMessageModel(MessageEntity messageEntity){
        this.id = messageEntity.Id;
        this.text = messageEntity.msg;
//        this.createdAt = messageEntity.timeStamp;
//        this.userId = messageEntity.user.id;
    }
}