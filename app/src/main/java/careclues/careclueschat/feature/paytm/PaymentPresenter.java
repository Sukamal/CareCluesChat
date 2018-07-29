package careclues.careclueschat.feature.paytm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.model.AddMoneyRequest;
import careclues.careclueschat.model.LinkWalletSendOtpRequest;
import careclues.careclueschat.model.LinkWalletValidateOtpRequest;
import careclues.careclueschat.model.OtpResponseModel;
import careclues.careclueschat.model.PaymentSuccessResponseModel;
import careclues.careclueschat.model.PaytmBalanceResponseModel;
import careclues.careclueschat.model.PaytmWalletResponseModel;
import careclues.careclueschat.model.PhysicianResponseModel;
import careclues.careclueschat.model.TextConsultantResponseModel;
import careclues.careclueschat.model.UpdatePaymentModeRequest;
import careclues.careclueschat.model.UpdatePaymentModeResponseModel;
import careclues.careclueschat.model.UserProfileResponseModel;
import careclues.careclueschat.model.ValidateOtpModel;
import careclues.careclueschat.network.ApiClient;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;

import static android.content.ContentValues.TAG;

/**
 * Created by SukamalD on 7/26/2018.
 */

public class PaymentPresenter implements PaymentContract.presenter {

    private PaymentContract.view view;
    private UserProfileResponseModel userProfileModel;
    private RestApiExecuter apiExecuter;
    private PaytmWalletResponseModel paytmWalletResponseModel;
    private PaytmBalanceResponseModel paytmBalanceResponseModel;
    private OtpResponseModel otpResponseModel;
    private ValidateOtpModel validateOtpModel;
    private TextConsultantResponseModel textConsultantResponseModel;
    private PaymentSuccessResponseModel paymentSuccessResponseModel;



    public PaymentPresenter(PaymentContract.view view){
        this.view = view;
        userProfileModel = CareCluesChatApplication.userProfile;
        apiExecuter = RestApiExecuter.getInstance();

    }

    @Override
    public void isPaytmLinked() {
        getTextConsultant();
        fetchPaytmData();
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


        LinkWalletSendOtpRequest linkWalletSendOtpRequest = new LinkWalletSendOtpRequest();
        linkWalletSendOtpRequest.mobileNo = CareCluesChatApplication.userProfile.data.mobileNumber;

        apiExecuter.linkWalletSendOtp(url,linkWalletSendOtpRequest, new ServiceCallBack<OtpResponseModel>(OtpResponseModel.class) {
            @Override
            public void onSuccess(OtpResponseModel response) {
                otpResponseModel = response;
                view.onOtpSend();
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                otpResponseModel = null;
            }
        });
    }

    @Override
    public void validateOtp(String otp){
        String url = ApiClient.API_BASE_URL + "wallets/paytm/otp_validation";
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        LinkWalletValidateOtpRequest linkWalletValidateOtpRequest = new LinkWalletValidateOtpRequest();
        linkWalletValidateOtpRequest.state = otpResponseModel.data.state;
        linkWalletValidateOtpRequest.otp = otp;

        apiExecuter.linkWalletValidateOtp(url,linkWalletValidateOtpRequest, new ServiceCallBack<ValidateOtpModel>(ValidateOtpModel.class) {
            @Override
            public void onSuccess(ValidateOtpModel response) {
                validateOtpModel = response;
                view.otpValidationSuccess();
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                validateOtpModel = null;
                view.otpValidationFail();

            }
        });
    }

    @Override
    public void getTextConsultant() {
            String urlLink = CareCluesChatApplication.messageModel.textConsultationLink;
            if (apiExecuter == null)
                apiExecuter = RestApiExecuter.getInstance();


            apiExecuter.getServerResponse(urlLink, new ServiceCallBack<TextConsultantResponseModel>(TextConsultantResponseModel.class) {
                @Override
                public void onSuccess(TextConsultantResponseModel response) {
                    textConsultantResponseModel =  response;
                    CareCluesChatApplication.textConsultantResponseModel = response;


                }

                @Override
                public void onFailure(List<NetworkError> errorList) {
                    textConsultantResponseModel = null;
                }
            });

    }

    @Override
    public void addMoney() {
        String urlLink = paytmWalletResponseModel.data.getLink("credits");
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        AddMoneyRequest addMoneyRequest = new AddMoneyRequest();
        addMoneyRequest.amount = 100;


        apiExecuter.addMoneyToWallet(urlLink,addMoneyRequest, new ServiceCallBack<String>(String.class) {
            @Override
            public void onSuccess(String response) {
                String path = saveAsHtmlFile(response,"AddMoney.html");

            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
            }
        });
    }

    @Override
    public void payViaWallet() {
        String urlLink = textConsultantResponseModel.data.getLink("debit_transactions");
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.payViaWallet(urlLink, new ServiceCallBack<PaymentSuccessResponseModel>(PaymentSuccessResponseModel.class) {
            @Override
            public void onSuccess(PaymentSuccessResponseModel response) {
                paymentSuccessResponseModel = response;
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                paymentSuccessResponseModel = null;

            }
        });
    }

    @Override
    public void payViaGateway() {
        String urlLink = textConsultantResponseModel.data.getLink("payments");
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();

        apiExecuter.payViaGetway(urlLink,new ServiceCallBack<String>(String.class) {
            @Override
            public void onSuccess(String response) {
                String path = saveAsHtmlFile(response,"PayMoney.html");
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });
    }

    @Override
    public void updatePaymentMode(String mode) {
        String urlLink = CareCluesChatApplication.messageModel.textConsultationLink;
        if (apiExecuter == null)
            apiExecuter = RestApiExecuter.getInstance();
        UpdatePaymentModeRequest paymentModeRequest = new UpdatePaymentModeRequest();
        paymentModeRequest.mode_of_payment = mode;

        apiExecuter.updatePaymentMode(urlLink,paymentModeRequest, new ServiceCallBack<TextConsultantResponseModel>(TextConsultantResponseModel.class) {
            @Override
            public void onSuccess(TextConsultantResponseModel response) {
                textConsultantResponseModel = response;
                CareCluesChatApplication.textConsultantResponseModel = response;
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {
                textConsultantResponseModel = null;

            }
        });
    }

    public  String saveAsHtmlFile(String html,String filename){

        File directory = new File(Environment.getExternalStorageDirectory(), "ccchat");
        if(!directory.exists()){
            directory.mkdirs();
        }
        File mypath = new File(directory,filename);

//        String directory = Environment.getExternalStorageDirectory().getPath();
////        String fileName = DateFormat.format("dd_MM_yyyy_hh_mm_ss", System.currentTimeMillis()).toString();
//        String fileName = "PyatmAdd";
//
//        fileName = fileName + ".html";
//        File mypath = new File(directory, fileName);


        try {
            FileOutputStream out = new FileOutputStream(mypath);
            byte[] data = html.getBytes();
            out.write(data);
            out.close();
            view.displyWebView(mypath.getAbsolutePath());

            Log.e(TAG, "File Save : " + mypath.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }
}
