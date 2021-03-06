package careclues.rocketchat.common;

import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import careclues.careclueschat.model.BaseRoomModel;
import careclues.careclueschat.model.RoomModel;
import careclues.rocketchat.callback.CcMessageCallback;
import careclues.rocketchat.listner.CcListener;
import careclues.rocketchat.listner.CcSubscribeListener;
import careclues.rocketchat.listner.CcTypingListener;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcRoom;
import careclues.rocketchat.models.CcSubscription;

public class CcCoreStreamMiddleware {
    private ConcurrentHashMap<String, CcSubscribeListener> listeners;
    private ConcurrentHashMap<String, ConcurrentHashMap<SubscriptionType, CcListener>> subs;

    public CcCoreStreamMiddleware() {
        listeners = new ConcurrentHashMap<>();
        subs = new ConcurrentHashMap<>();
    }


    public void createSubscription(String roomId, CcListener listener, SubscriptionType type) {
        if (listener != null) {
            if (subs.containsKey(roomId)) {
                subs.get(roomId).put(type, listener);
            } else {
                ConcurrentHashMap<SubscriptionType, CcListener> map = new ConcurrentHashMap<>();
                map.put(type, listener);
                subs.put(roomId, map);
            }
        }
    }

    public void removeAllSubscriptions(String roomId) {
        subs.remove(roomId);
    }

    public void removeSubscription(String roomId, SubscriptionType type) {
        if (subs.containsKey(roomId)) {
            subs.get(roomId).remove(type);
        }
    }


    public void createSubscriptionListener(String subId, CcSubscribeListener callback) {
        if (callback != null) {
            listeners.put(subId, callback);
        }
    }

    public void processListeners(JSONObject object) {
        String s = object.optString("collection");
        JSONArray array = object.optJSONObject("fields").optJSONArray("args");
        String roomId = object.optJSONObject("fields").optString("eventName")
                .replace("/typing", "")
                .replace("/rooms-changed", "")
                .replace("/subscriptions-changed", "");
        String eventName = object.optJSONObject("fields").optString("eventName");

        CcListener listener;

        if (subs.containsKey(roomId)) {
            switch (parse(s)) {
                case SUBSCRIBE_ROOM_MESSAGE:
                    listener = subs.get(roomId).get(SubscriptionType.SUBSCRIBE_ROOM_MESSAGE);
                    CcMessageCallback.SubscriptionCallback subscriptionListener = (CcMessageCallback.SubscriptionCallback) listener;
                    try {
                        Gson gson = new Gson();
                        CcMessage message = gson.fromJson(array.getJSONObject(0).toString().trim(),CcMessage.class);
                        subscriptionListener.onMessage(roomId, message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SUBSCRIBE_ROOM_TYPING:
                    listener = subs.get(roomId).get(SubscriptionType.SUBSCRIBE_ROOM_TYPING);
                    CcTypingListener typingListener = (CcTypingListener) listener;
                    typingListener.onTyping(roomId, array.optString(0), array.optBoolean(1));
                    break;
                case ROOM_SUBSCRIPTION_CHANGED:
                    if(eventName.contains("/rooms-changed")){
                        listener = subs.get(roomId).get(SubscriptionType.ROOM_SUBSCRIPTION_CHANGED);
                        try {
                            Gson gson = new Gson();
                            CcMessageCallback.NewRoomCallback newRoomCallback = (CcMessageCallback.NewRoomCallback) listener;
                            CcRoom roomModel = gson.fromJson(array.getJSONObject(1).toString().trim(),CcRoom.class);
                            newRoomCallback.onNewRoom(roomId, roomModel,array.optString(0).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(eventName.contains("/subscriptions-changed")){
                        listener = subs.get(roomId).get(SubscriptionType.ROOM_SUBSCRIPTION_CHANGED);
                        try {
                            Gson gson = new Gson();
                            CcMessageCallback.NewRoomCallback newRoomCallback = (CcMessageCallback.NewRoomCallback) listener;
                            CcSubscription subscription = gson.fromJson(array.getJSONObject(1).toString().trim(),CcSubscription.class);
                            newRoomCallback.onNewSubscription(roomId, subscription,array.optString(0).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;

                case OTHER:
                    break;
            }
        }
    }

    public void processSubscriptionSuccess(JSONObject subObj) {
        if (subObj.optJSONArray("subs") != null) {
            String id = subObj.optJSONArray("subs").optString(0);
            if (listeners.containsKey(id)) {
                listeners.remove(id).onSubscribe(true, id);
            }
        }
    }

    public void processUnsubscriptionSuccess(JSONObject unsubObj) {
        String id = unsubObj.optString("id");
        if (listeners.containsKey(id)) {
            CcSubscribeListener subscribeListener = listeners.remove(id);
            subscribeListener.onSubscribe(false, id);
        }
    }



    public enum SubscriptionType {
        SUBSCRIBE_ROOM_MESSAGE,
        SUBSCRIBE_ROOM_TYPING,
        ROOM_SUBSCRIPTION_CHANGED,
        OTHER
    }

    private static SubscriptionType parse(String s) {
        if (s.equals("stream-room-messages")) {
            return SubscriptionType.SUBSCRIBE_ROOM_MESSAGE;
        } else if (s.equals("stream-notify-room")) {
            return SubscriptionType.SUBSCRIBE_ROOM_TYPING;
        }else if (s.equals("stream-notify-user")) {
            return SubscriptionType.ROOM_SUBSCRIPTION_CHANGED;
        }
        return SubscriptionType.OTHER;
    }

}
