package careclues.careclueschat.feature.room;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.common.BaseFragment;
import careclues.careclueschat.feature.common.OnLoadMoreListener;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.storage.database.entity.MessageEntity;

public class RoomListFragment extends BaseFragment implements RoomMainActivity.performRoomFragmentAction{

    private LinearLayoutManager layoutManager;
    private RoomAdapter room1Adapter;
    @BindView(R.id.rvRoom)
    RecyclerView rvRoom;


    public List<RoomAdapterModel> list;
    public void setRoomList(List<RoomAdapterModel> list){
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_list, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        ((RoomMainActivity)getActivity()).setRoomFragmentAction(this);
        ((RoomMainActivity)getActivity()).dispalyFragment = RoomListFragment.class.getName();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycleView();
    }

    private void initRecycleView(){
//        rvRoom.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(layoutManager);
        displyRoomList(list);
//        rvRoom.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void displyRoomList(final List<RoomAdapterModel> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                room1Adapter = new RoomAdapter(list,getActivity(),rvRoom);
                room1Adapter.setAdapterItemClickListner(new RoomAdapter.onAdapterItemClickListner() {
                    @Override
                    public void onListItemClick(int position,String roomId) {
                        Log.d("Item clicked", "onListItemClick: ");
                        updateRoomStatus(roomId,false);
                        ((RoomMainActivity)getActivity()).presenter.getTextConsultant(roomId);
                        ((RoomMainActivity)getActivity()).presenter.getMessage(roomId);
                    }
                });
                rvRoom.setAdapter(room1Adapter);
                room1Adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        ((RoomMainActivity)getActivity()).presenter.getMoreRoom(room1Adapter.lastVisibleItem + 1, room1Adapter.visibleThreshold);
                    }
                });
            }
        });

    }

    @Override
    public void displyMoreRoomList(List<RoomAdapterModel> list) {
        room1Adapter.addLoadData(list);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                room1Adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void displyNewRoomList(List<RoomAdapterModel> list) {
        room1Adapter.addNewData(list);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                room1Adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void updateRoomMessage(MessageEntity msg) {
        for(RoomAdapterModel adapterModel : room1Adapter.roomObjects){
            if(adapterModel.Id.equals(msg.rId)){
//                adapterModel.updatedAt = msg.updatedAt;
                adapterModel.updatedAt = new Date();
                adapterModel.display = true;
                break;
            }
        }

        room1Adapter.rearangedata();
    }


    private void updateRoomStatus(String roomId, boolean status){
        List<RoomAdapterModel> roomObjects = room1Adapter.roomObjects;
        for (RoomAdapterModel adapterModel : roomObjects){
            if(adapterModel.Id.equals(roomId)){
                adapterModel.display = status;
            }
        }
    }

    @OnClick(R.id.fab)
    void createNewRoom(){
        ((RoomMainActivity)getActivity()).presenter.createNewRoom();
    }

    public void testupdateRoomMessage(String roomId) {
        for(RoomAdapterModel adapterModel : room1Adapter.roomObjects){
            if(adapterModel.Id.equals(roomId)){
                adapterModel.updatedAt = new Date();
                break;
            }
        }

        room1Adapter.rearangedata();
    }



}
