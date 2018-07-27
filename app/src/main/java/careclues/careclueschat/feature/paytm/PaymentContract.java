package careclues.careclueschat.feature.paytm;

import java.util.List;

import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;

public interface PaymentContract {

    interface view extends CommonViewInterface {

        public void showPaytmNotLinked();
        public void showPaytmBalance(double ballance);
        public void onOtpSend();
        public void otpValidationSuccess();
        public void otpValidationFail();

        public void displyNextScreen();

    }

    interface presenter {
        public void isPaytmLinked();
        public void fetchPaytmData();
        public void fetchPaytmBalance();
        public void linkPaytmSendOtp();
        public void validateOtp(String otp);
    }
}
