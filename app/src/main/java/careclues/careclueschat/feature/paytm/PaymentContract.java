package careclues.careclueschat.feature.paytm;

import java.util.List;

import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.PaymentSuccessResponseModel;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;

public interface PaymentContract {

    interface view extends CommonViewInterface {

        public void showPaytmNotLinked();
        public void showPaytmBalance(double ballance);
        public void onOtpSend();
        public void otpValidationSuccess();
        public void otpValidationFail();
        public void displyWebView(String path);
        public void onPaymentModeUpdated();
        public void onFetchTextConsultant();
        public void onSuccessWalletPayment();

    }

    interface presenter {
        public void isPaytmLinked();
        public void fetchPaytmData();
        public void fetchPaytmBalance();
        public void linkPaytmSendOtp();
        public void validateOtp(String otp);
        public void getTextConsultant();
        public void updatePaymentMode(String mode);
        public void addMoney(double amount);
        public void payViaWallet();
        public void payViaGateway();
    }
}
