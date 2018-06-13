package careclues.rocketchat;

import com.google.gson.annotations.SerializedName;

public enum  CcMessageType {
    @SerializedName("connected")
    CONNECTED,
    @SerializedName("result")
    RESULT,
    @SerializedName("ready")
    READY,
    @SerializedName("nosub")
    UNSUBSCRIBED,
    @SerializedName("updated")
    UPDATED,
    @SerializedName("added")
    ADDED,
    @SerializedName("changed")
    CHANGED,
    @SerializedName("removed")
    REMOVED,
    @SerializedName("ping")
    PING,
    @SerializedName("pong")
    PONG
}
