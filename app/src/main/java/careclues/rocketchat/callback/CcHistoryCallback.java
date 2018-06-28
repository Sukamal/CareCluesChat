package careclues.rocketchat.callback;

import java.util.List;

import careclues.rocketchat.models.CcMessage;

public interface CcHistoryCallback extends CcCallback {
    void onLoadHistory(List<CcMessage> list, int unreadNotLoaded);

}
