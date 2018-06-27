package careclues.rocketchat.callback;

import careclues.rocketchat.models.CcToken;

/**
 * Created by SukamalD on 6/28/2018.
 */

public interface CcLoginCallback extends CcCallback{

    void onLoginSuccess(CcToken token);


}
