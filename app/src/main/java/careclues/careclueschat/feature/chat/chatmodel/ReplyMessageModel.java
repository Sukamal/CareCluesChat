package careclues.careclueschat.feature.chat.chatmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyMessageModel {
    @SerializedName("replyToQuestionId")
    @Expose
    public String replyToQuestionId;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("patient")
    @Expose
    public PatientModel patient;
}
