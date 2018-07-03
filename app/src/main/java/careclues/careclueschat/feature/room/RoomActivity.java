package careclues.careclueschat.feature.room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.chat.TestChatACtivity;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.feature.common.OnLoadMoreListener;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.util.AppUtil;

public class RoomActivity extends BaseActivity implements RoomContract.view{

    public RoomPresenter presenter;

    private performFragmentAction activityAction;

    public void setFragmentAction(performFragmentAction activityAction){
        this.activityAction = activityAction;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_room;
    }

    @Override
    public void initComponents() {

        presenter = new RoomPresenter(this,getApplication());
//        presenter.doLogin("sachu-985", "XVQuexlHYvphcWYgtyLZLtf");
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
    public void onConnectionFaild(final int errorType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    @Override
    public void onSoketLoginSuccess() {
//        presenter.doApiLogin("sachu-985", "XVQuexlHYvphcWYgtyLZLtf");
    }

    @Override
    public void onLoginSuccess() {
        presenter.getOpenRoom();
    }

    @Override
    public void displyRoomList(final List<RoomAdapterModel> list) {

        RoomListFragment fragment = new RoomListFragment();
        fragment.list = list;
        addFragment(fragment,false,null);

    }

    @Override
    public void displyMoreRoomList(List<RoomAdapterModel> list) {
        if(activityAction != null){
            activityAction.displyMoreRoomList(list);
        }
    }

    @Override
    public void displyChatScreen(String roomId) {
        Intent intent = new Intent(RoomActivity.this, ChatActivity.class);
//      Intent intent = new Intent(RoomActivity.this, TestChatACtivity.class);
        intent.putExtra("roomId",roomId);
        startActivity(intent);
    }

    @Override
    public void updateRoomMessage(final String roomId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar
                        .make(findViewById(R.id.activity_room), roomId, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void displayMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar
                        .make(findViewById(R.id.activity_room), message, Snackbar.LENGTH_LONG)
                        .show();
            }
        });

    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    public interface performFragmentAction {
        void displyRoomList(List<RoomAdapterModel> list);
        void displyMoreRoomList(List<RoomAdapterModel> list);
    }




}
