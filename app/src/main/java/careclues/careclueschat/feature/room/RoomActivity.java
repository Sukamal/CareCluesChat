package careclues.careclueschat.feature.room;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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

    @BindView(R.id.rvRoom)
    RecyclerView rvRoom;

//    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;
    private RoomPresenter presenter;
    private RoomAdapter room1Adapter;

    @Override
    public int getContentLayout() {
        return R.layout.activity_room;
    }

    @Override
    public void initComponents() {

        String apiuserId = RestApiExecuter.getInstance().getAuthToken().getUserId();
        String apiTocken = RestApiExecuter.getInstance().getAuthToken().getToken();
        String socuserId = ((CareCluesChatApplication)getApplicationContext()).getUserId();
        String soctoken = ((CareCluesChatApplication)getApplicationContext()).getToken();

        System.out.println("apiuserId : "+apiuserId+"  , apiTocken : " +apiTocken+"  , socuserId : "+socuserId+"  , soctoken : " +soctoken);


        presenter = new RoomPresenter(this,getApplication());
        initRecycleView();
        presenter.getRoom();
    }

    private void initRecycleView(){
//        rvRoom.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(layoutManager);
//        rvRoom.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick(R.id.fab)
    void createNewRoom(){
        presenter.createNewRoom();
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
    public void displyRoomList(final List<RoomAdapterModel> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                room1Adapter = new RoomAdapter(list,RoomActivity.this,rvRoom);
                room1Adapter.setAdapterItemClickListner(new RoomAdapter.onAdapterItemClickListner() {
                    @Override
                    public void onListItemClick(int position,String roomId) {
                        presenter.getMessage(roomId);
                    }
                });
                rvRoom.setAdapter(room1Adapter);
                room1Adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        presenter.getMoreRoom(room1Adapter.lastVisibleItem + 1, room1Adapter.visibleThreshold);
                    }
                });
            }
        });

    }

    @Override
    public void displyMoreRoomList(List<RoomAdapterModel> list) {
        room1Adapter.addLoadData(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                room1Adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void displyChatScreen(String roomId) {
        Intent intent = new Intent(RoomActivity.this, ChatActivity.class);
//      Intent intent = new Intent(RoomActivity.this, TestChatACtivity.class);
        intent.putExtra("roomId",roomId);
        startActivity(intent);
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

}
