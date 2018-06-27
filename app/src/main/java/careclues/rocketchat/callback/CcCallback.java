package careclues.rocketchat.callback;

import careclues.rocketchat.common.CcRocketChatException;

/**
 * Created by SukamalD on 6/28/2018.
 */

public interface CcCallback {

    void onError(CcRocketChatException error);

}
