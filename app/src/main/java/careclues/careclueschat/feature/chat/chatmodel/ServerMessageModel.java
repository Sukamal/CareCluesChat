package careclues.careclueschat.feature.chat.chatmodel;

import com.google.gson.annotations.SerializedName;

public class ServerMessageModel {

    @SerializedName("id")
    public String id;
    @SerializedName("content")
    public String content;
    @SerializedName("type")
    public String type;
    @SerializedName("control")
    public String control;

    @SerializedName("patient")
    public PatientModel patientModel;

}
