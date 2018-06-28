package careclues.rocketchat.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.JsonDataException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import careclues.rocketchat.callback.CcCallback;
import careclues.rocketchat.callback.CcHistoryCallback;
import careclues.rocketchat.callback.CcLoginCallback;
import careclues.rocketchat.models.CcMessage;
import careclues.rocketchat.models.CcToken;

/**
 * Created by SukamalD on 6/28/2018.
 */

public class CcCoreMiddleware {

    private ConcurrentHashMap<Long, CcPair<? extends CcCallback, CallbackType>> callbacks;


    public CcCoreMiddleware() {
        callbacks = new ConcurrentHashMap<>();
    }

    public void createCallback(long i, CcCallback callback, CallbackType type) {
        callbacks.put(i, CcPair.create(callback, type));
    }


    public void processCallback(long i, JSONObject object) {
        JSONArray array;
        if (callbacks.containsKey(i)) {
            CcPair<? extends CcCallback, CallbackType> callbackPair = callbacks.remove(i);
            CcCallback callback = callbackPair.first;
            CallbackType type = callbackPair.second;
            Object result = object.opt("result");

            /*
             * Possibly add a validateResponse(result, type) here or return some
             * RocketChatInvalidResponseException...
             */
            if (result == null) {
                JSONObject error = object.optJSONObject("error");
                if (error == null) {
                    String message = "Missing \"result\" or \"error\" values: " + object.toString();
                    callback.onError(new CcRocketChatInvalidResponseException(message));
                } else {
                    callback.onError(new CcRocketChatApiException(object.optJSONObject("error")));
                }
                return;
            }

            try {
                switch (type) {
                    case LOGIN:
                        CcLoginCallback loginCallback = (CcLoginCallback) callback;
                        CcToken tokenObject = new CcToken((JSONObject) result);
                        loginCallback.onLoginSuccess(tokenObject);
                        break;
//                    case GET_PERMISSIONS:
//                        SimpleListCallback<Permission> permissionCallback = (SimpleListCallback<Permission>) callback;
//                        array = (JSONArray) result;
//                        List<Permission> permissions = new ArrayList<>(array.length());
//                        for (int j = 0; j < array.length(); j++) {
//                            permissions.add(new Permission(array.optJSONObject(j)));
//                        }
//                        permissionCallback.onSuccess(permissions);
//                        break;
//                    case GET_PUBLIC_SETTINGS:
//                        SimpleListCallback<PublicSetting> settingsCallback = (SimpleListCallback<PublicSetting>) callback;
//                        array = (JSONArray) result;
//                        List<PublicSetting> settings = new ArrayList<>(array.length());
//                        for (int j = 0; j < array.length(); j++) {
//                            settings.add(new PublicSetting(array.optJSONObject(j)));
//                        }
//                        settingsCallback.onSuccess(settings);
//                        break;
//                    case GET_USER_ROLES:
//                        SimpleListCallback<User> rolesCallback = (SimpleListCallback<User>) callback;
//                        array = (JSONArray) result;
//                        List<User> userObjects = getUserListAdapter().fromJson(array.toString());
//                        rolesCallback.onSuccess(userObjects);
//                        break;
//                    case GET_SUBSCRIPTIONS:
//                        SimpleListCallback<Subscription> subscriptionCallback = (SimpleListCallback<Subscription>) callback;
//                        array = (JSONArray) result;
//                        List<Subscription> subscriptions = getSubscriptionListAdapter().fromJson(array.toString());
//                        subscriptionCallback.onSuccess(subscriptions);
//                        break;
//                    case GET_ROOMS:
//                        SimpleListCallback<Room> roomCallback = (SimpleListCallback<Room>) callback;
//                        array = (JSONArray) result;
//                        List<Room> rooms = getRoomListAdapter().fromJson(array.toString());
//                        roomCallback.onSuccess(rooms);
//                        break;
//                    case GET_ROOM_ROLES:
//                        SimpleListCallback<RoomRole> roomRolesCallback = (SimpleListCallback<RoomRole>) callback;
//                        array = (JSONArray) result;
//                        List<RoomRole> roomRoles = new ArrayList<>(array.length());
//                        for (int j = 0; j < array.length(); j++) {
//                            roomRoles.add(new RoomRole(array.optJSONObject(j)));
//                        }
//                        roomRolesCallback.onSuccess(roomRoles);
//                        break;
//                    case LIST_CUSTOM_EMOJI:
//                        SimpleListCallback<Emoji> emojiCallback = (SimpleListCallback<Emoji>) callback;
//                        array = (JSONArray) result;
//                        List<Emoji> emojis = new ArrayList<>(array.length());
//                        for (int j = 0; j < array.length(); j++) {
//                            emojis.add(new Emoji(array.optJSONObject(j)));
//                        }
//                        emojiCallback.onSuccess(emojis);
//                        break;
                    case LOAD_HISTORY:
                        CcHistoryCallback historyCallback = (CcHistoryCallback) callback;
                        array = ((JSONObject) result).optJSONArray("messages");
//                        List<CcMessage> messages = getMessageListAdapter()
//                                .fromJson(array.toString());

                        Type listType = new TypeToken<ArrayList<CcMessage>>(){}.getType();
                        List<CcMessage> messages = new Gson().fromJson(array.toString(), listType);

                        int unreadNotLoaded = ((JSONObject) result).optInt("unreadNotLoaded");
                        historyCallback.onLoadHistory(messages, unreadNotLoaded);
                        break;
//                    case GET_ROOM_MEMBERS:
//                        RoomCallback.GetMembersCallback membersCallback = (RoomCallback.GetMembersCallback) callback;
//                        array = ((JSONObject) result).optJSONArray("records");
//                        Integer total = ((JSONObject) result).optInt("total");
//                        List<User> users = getUserListAdapter().fromJson(array.toString());
//                        membersCallback.onGetRoomMembers(total, users);
//                        break;
//                    case SEND_MESSAGE:
//                        MessageCallback.MessageAckCallback ackCallback = (MessageCallback.MessageAckCallback) callback;
//                        Message message = getMessageAdapter().fromJson(result.toString());
//                        ackCallback.onMessageAck(message);
//                        break;
//                    case SEARCH_MESSAGE:
//                        SimpleListCallback<Message> searchMessageCallback = (SimpleListCallback<Message>) callback;
//                        array = ((JSONObject) result).optJSONArray("messages");
//                        List<Message> searchMessages = getMessageListAdapter().fromJson(array.toString());
//                        searchMessageCallback.onSuccess(searchMessages);
//                        break;
//                    case CREATE_GROUP:
//                        RoomCallback.GroupCreateCallback createCallback = (RoomCallback.GroupCreateCallback) callback;
//                        String roomId = ((JSONObject) result).optString("rid");
//                        createCallback.onCreateGroup(roomId);
//                        break;
//                    case UFS_CREATE:
//                        IFileUpload.UfsCreateCallback ufsCreateCallback = (IFileUpload.UfsCreateCallback) callback;
//                        FileUploadToken token = new FileUploadToken((JSONObject) result);
//                        ufsCreateCallback.onUfsCreate(token);
//                        break;
//                    case UFS_COMPLETE:
//                        IFileUpload.UfsCompleteListener completeCallback = (IFileUpload.UfsCompleteListener) callback;
//                        FileDescriptor file = new FileDescriptor((JSONObject) result);
//                        completeCallback.onUfsComplete(file);
//                        break;
//                    case MESSAGE_OP:
//                    case DELETE_GROUP:
//                    case ARCHIVE:
//                    case UNARCHIVE:
//                    case JOIN_PUBLIC_GROUP:
//                    case LEAVE_GROUP:
//                    case OPEN_ROOM:
//                    case HIDE_ROOM:
//                    case SET_FAVOURITE_ROOM:
//                    case SET_STATUS:
//                    case LOGOUT:
//                        ((SimpleCallback) callback).onSuccess();
//                        break;
                }
            } catch (JsonDataException | JSONException jsonException) {
                callback.onError(new CcRocketChatInvalidResponseException(jsonException.getMessage(), jsonException));
            }

//            catch (IOException e) {
//                callback.onError(new CcRocketChatInvalidResponseException(e.getMessage(), e));
//                e.printStackTrace();
//            }
        }
    }




    public enum CallbackType {
        LOGIN,
        GET_PERMISSIONS,
        GET_PUBLIC_SETTINGS,
        GET_USER_ROLES,
        GET_SUBSCRIPTIONS,
        GET_ROOMS,
        GET_ROOM_ROLES,
        LIST_CUSTOM_EMOJI,
        LOAD_HISTORY,
        GET_ROOM_MEMBERS,
        SEND_MESSAGE,
        MESSAGE_OP,
        SEARCH_MESSAGE,
        CREATE_GROUP,
        DELETE_GROUP,
        ARCHIVE,
        UNARCHIVE,
        JOIN_PUBLIC_GROUP,
        LEAVE_GROUP,
        OPEN_ROOM,
        HIDE_ROOM,
        SET_FAVOURITE_ROOM,
        SET_STATUS,
        UFS_CREATE,
        UFS_COMPLETE,
        LOGOUT
    }
}
