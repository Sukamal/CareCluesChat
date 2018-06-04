package careclues.careclueschat.feature.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.feature.room.RoomActivity;
import careclues.careclueschat.util.AppUtil;

public class LoginActivity extends BaseActivity implements LoginContract.view {

    @BindView(R.id.etUserName)
    AppCompatEditText etUserName;
    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;
    @BindView(R.id.btnLogin)
    AppCompatButton btnLogin;

    private LoginPresenter presenter;

    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initComponents() {
        presenter = new LoginPresenter(this,getApplication());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.disconnectToServer();
        super.onDestroy();
    }

    @OnClick(R.id.btnLogin)
    void onloinButtonClick(){
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        presenter.doLogin(etUserName.getText().toString().trim(), etPassword.getText().toString().trim());
    }

    @Override
    public void onConnectionFaild(int errorType) {

        if(errorType == 1){
            AppUtil.getSnackbarWithAction(findViewById(R.id.rlActivityLogin), R.string.connection_error)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.reconnectToServer();
                        }
                    })
                    .show();
        }else if(errorType == 2){
            AppUtil.getSnackbarWithAction(findViewById(R.id.rlActivityLogin),  R.string.disconnected_from_server)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.reconnectToServer();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void displyNextScreen() {

        startActivity(new Intent(LoginActivity.this, RoomActivity.class));
    }

    @Override
    public void displayMessage(String message) {
        Snackbar
                .make(findViewById(R.id.rlActivityLogin), message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
