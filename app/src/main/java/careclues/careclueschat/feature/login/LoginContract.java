package careclues.careclueschat.feature.login;

import careclues.careclueschat.feature.common.CommonViewInterface;

public interface LoginContract {

    interface view extends CommonViewInterface {

        public void onConnectionFaild(int errorType);
        public void displyNextScreen();
    }

    interface presenter {

        public void doLogin(String userId, String password);
        public void reconnectToServer();
        public void disconnectToServer();
    }
}
