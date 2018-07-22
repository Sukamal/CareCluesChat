package careclues.careclueschat.feature.room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.chat.ChatFragment;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppUtil;

public class RoomMainActivity extends BaseActivity implements RoomContract.view{

    public RoomPresenter presenter;

    @BindView(R.id.cl_loading)
    ConstraintLayout clLoading;

    public String dispalyFragment;
    private performRoomFragmentAction roomFragmentAction;
    private performChatFragmentAction chatFragmentAction;


    public void setRoomFragmentAction(performRoomFragmentAction activityAction){
        this.roomFragmentAction = activityAction;
    }

    public void setChatFragmentAction(performChatFragmentAction activityAction){
        this.chatFragmentAction = activityAction;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_room;
    }

    @Override
    public void initComponents() {
        presenter = new RoomPresenter(this,getApplication());
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
    public void onFetchBasicData() {
        presenter.getOpenRoom();
    }

    @Override
    public void displyRoomListScreen(final List<RoomAdapterModel> list) {
        clLoading.setVisibility(View.GONE);
        RoomListFragment fragment = new RoomListFragment();
        fragment.setRoomList(list);
        addFragment(fragment,false,null);
    }

    @Override
    public void displyChatScreen(String roomId) {
        Fragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("roomId",roomId);
        addFragment(fragment,true,bundle);
    }

    @Override
    public void displyMoreRoomList(List<RoomAdapterModel> list) {
        if(roomFragmentAction != null){
            roomFragmentAction.displyMoreRoomList(list);
        }
    }

    @Override
    public void onUserTyping(String roomId, String user, Boolean istyping) {
        if(chatFragmentAction != null){
            chatFragmentAction.displyUserTyping(roomId,user,istyping);
        }
    }

    @Override
    public void updateRoomMessage(final String roomId,final MessageEntity messageEntity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar
                        .make(findViewById(R.id.activity_room), roomId, Snackbar.LENGTH_LONG)
                        .show();
                if(dispalyFragment.equals(RoomListFragment.class.getName())){
                    roomFragmentAction.updateRoomMessage(messageEntity);

                }else if(dispalyFragment.equals(ChatFragment.class.getName())){
                    chatFragmentAction.updateChatMessage(messageEntity);

                }
            }
        });
    }


    @Override
    public void displayToastMessage(final String message) {
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

    public interface performRoomFragmentAction {
        void displyRoomList(List<RoomAdapterModel> list);
        void displyMoreRoomList(List<RoomAdapterModel> list);
        void updateRoomMessage(MessageEntity msg);
    }

    public interface performChatFragmentAction {
        void displyUserTyping(String roomId, String user, Boolean istyping);
        void updateChatMessage(MessageEntity msg);
    }



}
