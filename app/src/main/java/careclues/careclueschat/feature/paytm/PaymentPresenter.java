package careclues.careclueschat.feature.paytm;

import java.util.List;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.model.PaytmBalanceResponseModel;
import careclues.careclueschat.model.PaytmWalletResponseModel;
import careclues.careclueschat.model.UserProfileResponseModel;
import careclues.careclueschat.network.ApiClient;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;

/**
 * Created by SukamalD on 7/26/2018.
 */

public class PaymentPresenter implements PaymentContract.presenter {

    private PaymentContract.view view;
    private UserProfileResponseModel userProfileModel;
    private RestApiExecuter apiExecuter;
    private PaytmWalletResponseModel paytmWalletResponseModel;
    private PaytmBalanceResponseModel paytmBalanceResponseModel;



    public PaymentPresenter(PaymentContract.view view){
        this.view = view;
        userProfileModel = CareCluesChatApplication.userProfile;
        apiExecuter = RestApiExecuter.getInstance();

    }

    @Override
    public void isPaytmLinked() {
        if(userProfileModel.data.getLink("paytm_wallet_balance") == null){
            view.showPaytmNotLinked();
        }else {
            fetchPaytmBalance();
        }

    }

    @Override
    public void fetchPaytmData() {
        String url = userProfileModel.data.getLink("paytm_wallet");
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.getServerResponse(url, new ServiceCallBack<PaytmWalletResponseModel>(PaytmWalletResponseModel.class) {
            @Override
            public void onSuccess(PaytmWalletResponseModel response) {
                paytmWalletResponseModel = response;
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                paytmWalletResponseModel = null;
            }
        });


    }

    @Override
    public void fetchPaytmBalance() {
        String url = userProfileModel.data.getLink("paytm_wallet_balance");
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.getServerResponse(url, new ServiceCallBack<PaytmBalanceResponseModel>(PaytmBalanceResponseModel.class) {
            @Override
            public void onSuccess(PaytmBalanceResponseModel response) {
                paytmBalanceResponseModel = response;
                if(paytmBalanceResponseModel != null){
                    if(paytmBalanceResponseModel.data.status != null && paytmBalanceResponseModel.data.status.equalsIgnoreCase("ACTIVE")){
                        view.showPaytmBalance(paytmBalanceResponseModel.data.walletBalance);
                    }else{
                        view.showPaytmNotLinked();
                    }
                }else{
                    view.showPaytmNotLinked();
                }
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                paytmBalanceResponseModel = null;
            }
        });
    }

    @Override
    public void linkPaytmSendOtp() {
        String url = ApiClient.API_BASE_URL + "wallets/paytm/otp";
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

//        apiExecuter.getServerResponse(url, new ServiceCallBack<PaytmBalanceResponseModel>(PaytmBalanceResponseModel.class) {
//            @Override
//            public void onSuccess(PaytmBalanceResponseModel response) {
//                paytmBalanceResponseModel = response;
//                if(paytmBalanceResponseModel != null){
//                    if(paytmBalanceResponseModel.data.status != null && paytmBalanceResponseModel.data.status.equalsIgnoreCase("ACTIVE")){
//                        view.showPaytmBalance(paytmBalanceResponseModel.data.walletBalance);
//                    }else{
//                        view.showPaytmNotLinked();
//                    }
//                }else{
//                    view.showPaytmNotLinked();
//                }
//            }
//
//            @Override
//            public void onFailure(List<NetworkError> errorList) {
//                paytmBalanceResponseModel = null;
//            }
//        });
    }

    private void validateOtp(){
        String url = ApiClient.API_BASE_URL + "wallets/paytm/otp_validation";
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

//        apiExecuter.getServerResponse(url, new ServiceCallBack<PaytmBalanceResponseModel>(PaytmBalanceResponseModel.class) {
//            @Override
//            public void onSuccess(PaytmBalanceResponseModel response) {
//                paytmBalanceResponseModel = response;
//                if(paytmBalanceResponseModel != null){
//                    if(paytmBalanceResponseModel.data.status != null && paytmBalanceResponseModel.data.status.equalsIgnoreCase("ACTIVE")){
//                        view.showPaytmBalance(paytmBalanceResponseModel.data.walletBalance);
//                    }else{
//                        view.showPaytmNotLinked();
//                    }
//                }else{
//                    view.showPaytmNotLinked();
//                }
//            }
//
//            @Override
//            public void onFailure(List<NetworkError> errorList) {
//                paytmBalanceResponseModel = null;
//            }
//        });
    }
}
