package careclues.rocketchat.models;

import com.google.gson.annotations.SerializedName;

import careclues.rocketchat.CcMessageType;

public class CcSocketMessage {

    @SerializedName("msg")
    public CcMessageType messageType;

    public String id;
}
