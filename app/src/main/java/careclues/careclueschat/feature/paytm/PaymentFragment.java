package careclues.careclueschat.feature.paytm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.chat.ChatFragment;
import careclues.careclueschat.feature.common.BaseFragment;
import careclues.careclueschat.model.UserProfileResponseModel;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppDialog;
import careclues.careclueschat.util.AppUtil;

/**
 * Created by SukamalD on 7/26/2018.
 */

public class PaymentFragment extends BaseFragment implements PaymentContract.view, View.OnClickListener{

    private View view;
    private PaymentPresenter presenter;
    private UserProfileResponseModel userProfileModel;

    @BindView(R.id.tvLinkWalet)
    TextView tvLinkWalet;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvCard)
    TextView tvCard;


    @BindView(R.id.btnPay)
    Button btnPay;

    private double debitAmount = 200;

    private int btnType;

    private final int TYPE_PAY = 1;
    private final int TYPE_ADD_MONEY = 2;

    private final String TNX_MODE_WALLET = "paytm";
    private final String TNX_MODE_CARD = "payment_gateway";

    private String selectedMode;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.payment_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        presenter = new PaymentPresenter(this);
        userProfileModel = AppConstant.userProfile;
        presenter.isPaytmLinked();
        tvCard.setOnClickListener(this);

    }

    @Override
    public void displayToastMessage(String message) {

    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }


    @Override
    public void showPaytmNotLinked() {
        tvLinkWalet.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
        tvLinkWalet.setOnClickListener(this);
        btnPay.setVisibility(View.GONE);

    }

    @Override
    public void showPaytmBalance(double ballance) {
        tvLinkWalet.setVisibility(View.GONE);
        tvBalance.setVisibility(View.VISIBLE);
        tvBalance.setText("Rs."+ballance);

        btnPay.setVisibility(View.VISIBLE);
        btnPay.setOnClickListener(this);

        if(debitAmount > ballance){
            btnPay.setText("Add Money");
            btnType = TYPE_ADD_MONEY;
        }else{
            btnPay.setText("Pay");
            btnType = TYPE_PAY;

        }


    }

    @Override
    public void onOtpSend() {
        showValidateOTPDialog();
    }

    @Override
    public void otpValidationSuccess() {

    }

    @Override
    public void otpValidationFail() {
        showValidateOTPDialog();
    }

    @Override
    public void displyWebView(String path) {
        Fragment fragment = new LoadWebPage();
        Bundle bundle = new Bundle();
        bundle.putString("path",path);
        addFragment(fragment,true,bundle);
    }

    @Override
    public void onPaymentModeUpdated() {

        if(selectedMode.equals(TNX_MODE_WALLET)){
            presenter.payViaWallet();
        }else if(selectedMode.equals(TNX_MODE_CARD)){
            presenter.payViaGateway();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLinkWalet:
                showLinkWalletDialog();
                break;
            case R.id.tvCard:
                selectedMode = TNX_MODE_CARD;
                presenter.updatePaymentMode(TNX_MODE_CARD);
                break;
            case R.id.btnPay:
                showAddAmountPDialog();

//                if (AppUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    if(btnType == TYPE_PAY){
//                        selectedMode = TNX_MODE_WALLET;
//                        presenter.updatePaymentMode(TNX_MODE_WALLET);
//                    }else if(btnType == TYPE_ADD_MONEY){
//                        showAddAmountPDialog();
//                    }
//                } else {
//                    askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                }

                break;
        }
    }

    private void showLinkWalletDialog(){
        AppDialog appDialog = new AppDialog();
        appDialog.showLinkWalletDialog(getActivity(), "", "", new AppDialog.DialogListener() {
            @Override
            public void OnPositivePress(Object val) {
                presenter.linkPaytmSendOtp();
                listenToSms();
            }

            @Override
            public void OnNegativePress() {

            }
        });
    }

    private void showValidateOTPDialog(){
        AppDialog appDialog = new AppDialog();
        appDialog.showValidateOTPDialog(getActivity(), "", "", new AppDialog.DialogListener() {
            @Override
            public void OnPositivePress(Object val) {
                presenter.validateOtp((String) val);
            }

            @Override
            public void OnNegativePress() {
                presenter.linkPaytmSendOtp();
            }
        });
    }

    private void showAddAmountPDialog(){
        AppDialog appDialog = new AppDialog();
        appDialog.showAddMoneyAmountDialog(getActivity(), "", "", new AppDialog.DialogListener() {
            @Override
            public void OnPositivePress(Object val) {

                selectedMode = TNX_MODE_WALLET;
                presenter.addMoney(Double.valueOf((String)val));
            }

            @Override
            public void OnNegativePress() {
                presenter.linkPaytmSendOtp();
            }
        });
    }

    private void listenToSms(){
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
            }
        });
    }



    public void askPermission(String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST);
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstant.RequestTag.PERMISSION_REQUEST_CODE_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(btnType == TYPE_PAY){
                        selectedMode = TNX_MODE_WALLET;
                        presenter.updatePaymentMode(TNX_MODE_WALLET);
                    }else if(btnType == TYPE_ADD_MONEY){
                        showAddAmountPDialog();
                    }
                } else {
                    Toast.makeText(getActivity(), "WRITE_EXTERNAL_STORAGE Permission Denied.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
