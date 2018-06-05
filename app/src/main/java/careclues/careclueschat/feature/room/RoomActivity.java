package careclues.careclueschat.feature.room;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rocketchat.core.model.Subscription;

import java.util.List;

import butterknife.BindView;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.util.AppUtil;

public class RoomActivity extends BaseActivity implements RoomContract.view{

    @BindView(R.id.rvRoom)
    RecyclerView rvRoom;

//    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;
    private RoomPresenter presenter;
    private RoomAdapter roomAdapter;

    @Override
    public int getContentLayout() {
        return R.layout.activity_room;
    }

    @Override
    public void initComponents() {
        presenter = new RoomPresenter(this,getApplication());
        initRecycleView();
//        presenter.getRoom();
    }

    private void initRecycleView(){
//        rvRoom.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(layoutManager);
//        rvRoom.setItemAnimator(new DefaultItemAnimator());
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


    @Override
    public void onConnectionFaild(int errorType) {
        if(errorType == 1){
            AppUtil.getSnackbarWithAction(findViewById(R.id.activity_room), R.string.connection_error)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.reconnectToServer();
                        }
                    })
                    .show();
        }else if(errorType == 2){
            AppUtil.getSnackbarWithAction(findViewById(R.id.activity_room),  R.string.disconnected_from_server)
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
    public void displyRoomList(final List<Subscription> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roomAdapter = new RoomAdapter(list,RoomActivity.this,rvRoom);
                rvRoom.setAdapter(roomAdapter);
            }
        });

    }

    @Override
    public void displyNextScreen() {

    }

    @Override
    public void displayMessage(String message) {
        Snackbar
                .make(findViewById(R.id.activity_room), message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
