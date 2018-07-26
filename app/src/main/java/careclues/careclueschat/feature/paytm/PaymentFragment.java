package careclues.careclueschat.feature.paytm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.common.BaseFragment;
import careclues.careclueschat.model.UserProfileResponseModel;

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
        userProfileModel = CareCluesChatApplication.userProfile;
        presenter.isPaytmLinked();
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
    }

    @Override
    public void showPaytmBalance(double ballance) {
        tvLinkWalet.setVisibility(View.GONE);
        tvBalance.setVisibility(View.VISIBLE);
        tvBalance.setText("Rs."+ballance);
    }

    @Override
    public void displyNextScreen() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLinkWalet:
                presenter.linkPaytmSendOtp();
                break;
        }
    }
}
