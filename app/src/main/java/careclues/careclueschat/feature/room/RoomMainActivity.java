package careclues.careclueschat.feature.room;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import careclues.careclueschat.R;
//import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.chat.ChatFragment;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppDialog;
import careclues.careclueschat.util.AppUtil;

public class RoomMainActivity extends BaseActivity implements RoomContract.view{

    public RoomPresenter presenter;

    @BindView(R.id.cl_loading)
    ConstraintLayout clLoading;

    public String dispalyFragment;
    private performRoomFragmentAction roomFragmentAction;
    private performChatFragmentAction chatFragmentAction;
    private AppDialog appDialog;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;



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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            executeUserPermissionTree();
        }else {
            presenter = new RoomPresenter(this,getApplication());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(presenter != null)
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
    public void displyNewRoomList(List<RoomAdapterModel> list) {
        if(roomFragmentAction != null){
            roomFragmentAction.displyNewRoomList(list);
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
                    if(roomFragmentAction != null){
                        roomFragmentAction.updateRoomMessage(messageEntity);
                    }

                }else if(dispalyFragment.equals(ChatFragment.class.getName())){
                    if(chatFragmentAction != null){
                        chatFragmentAction.updateChatMessage(messageEntity);
                    }

                }
            }
        });
    }


    public void executeUserPermissionTree() {

        appDialog = new AppDialog();
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!AppUtil.addPermission(this,permissionsList, android.Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");
        if (!AppUtil.addPermission(this,permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write File");
        if (!AppUtil.addPermission(this,permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read File");
        if (!AppUtil.addPermission(this,permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera Access");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                appDialog.showErrorDialog(this, "Permission", message, new AppDialog.DialogListener() {
                    @Override
                    public void OnPositivePress(Object val) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        }
                    }

                    @Override
                    public void OnNegativePress() {
                        finish();
                    }
                });

                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }else{
            presenter = new RoomPresenter(this,getApplication());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA,PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    presenter = new RoomPresenter(this,getApplication());

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
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
        void displyNewRoomList(List<RoomAdapterModel> list);
        void updateRoomMessage(MessageEntity msg);
    }

    public interface performChatFragmentAction {
        void displyUserTyping(String roomId, String user, Boolean istyping);
        void updateChatMessage(MessageEntity msg);
    }



}
