package careclues.rocketchat.callback;

import java.util.List;

import careclues.rocketchat.models.CcUser;

/**
 * Created by SukamalD on 7/1/2018.
 */

public class CcRoomCallback {

    public interface GroupCreateCallback extends CcCallback {
        void onCreateGroup(String roomId);
    }

    public interface GetMembersCallback extends CcCallback {
        void onGetRoomMembers(Integer total, List<CcUser> members);
    }
}
