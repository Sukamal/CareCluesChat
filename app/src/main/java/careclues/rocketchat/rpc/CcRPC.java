package careclues.rocketchat.rpc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CcRPC {

    //Currently Used
    private static final String TYPE_PING = "ping";
    private static final String TYPE_CONNECTED = "connected";
    private static final String TYPE_ADDED = "added";
    private static final String TYPE_RESULT = "result";
    private static final String TYPE_READY = "ready";
    private static final String TYPE_CHANGED = "changed";
    private static final String TYPE_REMOVED = "removed";
    private static final String TYPE_NOSUB = "nosub";
    private static final String TYPE_PONG = "pong";
    public static final String PING_MESSAGE = "{\"msg\":\"ping\"}";
    public static final String PONG_MESSAGE = "{\"msg\":\"pong\"}";
    //Maybe required in future
    public static final String TYPE_UPDATED = "updated";
    public static final String TYPE_ERROR = "error";
    public static final String TYPE_CLOSED = "closed";
    public static final String TYPE_UNSUB = "unsub";
    public static final String TYPE_SUB = "sub";


    public static CcRPC.MsgType getMessageType(String s) {
        if (s.equals(TYPE_PING)) {
            return CcRPC.MsgType.PING;
        } else if (s.equals(TYPE_CONNECTED)) {
            return CcRPC.MsgType.CONNECTED;
        } else if (s.equals(TYPE_ADDED)) {
            return CcRPC.MsgType.ADDED;
        } else if (s.equals(TYPE_RESULT)) {
            return CcRPC.MsgType.RESULT;
        } else if (s.equals(TYPE_READY)) {
            return CcRPC.MsgType.READY;
        } else if (s.equals(TYPE_CHANGED)) {
            return CcRPC.MsgType.CHANGED;
        } else if (s.equals(TYPE_REMOVED)) {
            return CcRPC.MsgType.REMOVED;
        } else if (s.equals(TYPE_NOSUB)) {
            return CcRPC.MsgType.NOSUB;
        } else if (s.equals(TYPE_PONG)) {
            return CcRPC.MsgType.PONG;
        } else {
            return CcRPC.MsgType.OTHER;
        }
    }

    /**
     * Tested
     */

    public static String ConnectObject() {
        return "{\"msg\":\"connect\",\"version\":\"1\",\"support\":[\"1\",\"pre2\",\"pre1\"]}";
    }

    public static JSONObject getRemoteMethodObject(int integer, String methodName, Object... args) {
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "method");
            object.put("method", methodName);
            object.put("id", String.valueOf(integer));
            JSONArray params = new JSONArray();
            for (int i = 0; i < args.length; i++) {
                params.put(args[i]);
            }
            object.put("params", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public enum MsgType {
        PING,
        PONG,
        CONNECTED,
        ADDED,
        RESULT,
        READY,
        CHANGED,
        REMOVED,
        NOSUB,
        OTHER
    }

}
