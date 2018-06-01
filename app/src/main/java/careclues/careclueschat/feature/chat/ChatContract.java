package careclues.careclueschat.feature.chat;


import java.util.List;

import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.network.ServiceCallBack;

public interface ChatContract {

    interface view extends CommonViewInterface {

        public void displyNextScreen();
    }

    interface presenter {

        public void loadData(ServiceCallBack<PostsModel> callback);
    }
}
