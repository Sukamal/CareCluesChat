package careclues.careclueschat.feature.chat.chatmodel;

import android.support.annotation.NonNull;
import android.util.Log;

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
    public String image_url;
    public ServerMessageModel messageModel;

//    public ChatMessageModel(String id,String text,Date createdAt,String userId){
//        this.id = id;
//        this.text = text;
//        this.createdAt = createdAt;
//        this.userId = userId;
//        this.messageModel = parseMessage(text);
//    }

    public ChatMessageModel(MessageEntity messageEntity){
        this.id = messageEntity.Id;
        this.text = messageEntity.msg;
        this.createdAt = messageEntity.timeStamp;
        this.userId = messageEntity.user.id;
        this.image_url = messageEntity.image_url;
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
        msg = msg.replaceAll("\\\\","");
        msg = msg.replaceAll("\"\\{","{");
        msg = msg.replaceAll("\\}\"","}");

        ServerMessageModel messageModel = null;
        boolean isJson = false;
        JSONObject jsonObject = null;
        if(msg != null && msg.trim().length() > 0){
            if(msg.startsWith("{")){
                isJson = true;
            }else{
                isJson = false;
            }

            if(isJson){
                Gson gson = new Gson();
                messageModel = gson.fromJson(msg,ServerMessageModel.class);
//                try {
//                    jsonObject = new JSONObject(msg);
//
//                } catch (JSONException e) {
//
//                }

            }else{
                messageModel = new ServerMessageModel();
                messageModel.content = msg;
                messageModel.type = "info";
                messageModel.control = "text";
            }

        }
        return messageModel;
    }

    public ServerMessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(ServerMessageModel messageModel) {
        this.messageModel = messageModel;
    }
}
