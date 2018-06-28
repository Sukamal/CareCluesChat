package careclues.rocketchat.rpc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CcChatHistoryRPC extends CcRPC{

    private static final String HISTORY = "loadHistory";

    public static String loadHistory(int integer, String roomId, Date oldestMessageTimestamp, Integer count, Date lastTimestamp) {
        JSONObject oldestTs = null;
        JSONObject lastTs = null;
        try {
            if (oldestMessageTimestamp != null) {
                oldestTs = new JSONObject();
                oldestTs.put("$date", oldestMessageTimestamp.getTime());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lastTimestamp != null) {
            lastTs = new JSONObject();
            try {
                lastTs.put("$date", lastTimestamp.getTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return getRemoteMethodObject(integer, HISTORY, roomId, oldestTs, count, lastTs).toString();
    }
}
