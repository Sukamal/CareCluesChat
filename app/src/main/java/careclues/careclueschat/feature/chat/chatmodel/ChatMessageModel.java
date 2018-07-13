package careclues.careclueschat.feature.chat.chatmodel;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import careclues.careclueschat.storage.database.entity.MessageEntity;

/**
 * Created by SukamalD on 6/14/2018.
 */

public class ChatMessageModel implements Comparable<ChatMessageModel>{
    public String id;
    public String text;
    public Date createdAt;
    public String userId;
    public ServerMessageModel messageModel;

    public ChatMessageModel(String id,String text,Date createdAt,String userId){
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
        this.messageModel = parseMessage(text);
    }

    public ChatMessageModel(MessageEntity messageEntity){
        this.id = messageEntity.Id;
        this.text = messageEntity.msg;
        this.createdAt = messageEntity.timeStamp;
        this.userId = messageEntity.user.id;
        this.messageModel = parseMessage(messageEntity.msg);
    }

    @Override
    public int compareTo(@NonNull ChatMessageModel messageModel) {
        if(createdAt != null && messageModel.createdAt!= null){
            if(createdAt.equals(messageModel.createdAt))
                return 0;
            else if(createdAt.after(messageModel.createdAt))
                return 1;
            else
                return -1;
        } else
            return -1;
    }


    private ServerMessageModel parseMessage(String msg){
        ServerMessageModel messageModel = null;
        boolean isJson = false;
        JSONObject jsonObject = null;
        if(msg != null && msg.trim().length() > 0){
            try {
                jsonObject = new JSONObject(msg);
                isJson = true;
            } catch (JSONException e) {
                isJson = false;
            }

            if(isJson){
                Gson gson = new Gson();
                messageModel = gson.fromJson(jsonObject.toString(),ServerMessageModel.class);
            }else{
                messageModel = new ServerMessageModel();
                messageModel.content = msg;
                messageModel.type = "info";
                messageModel.control = "text";
            }

        }
        return messageModel;
    }
}
