package careclues.careclueschat.feature.room;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rocketchat.core.model.Subscription;

import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.common.OnLoadMoreListener;
import careclues.careclueschat.storage.database.entity.SubscriptionEntity;


public class Room1Adapter extends RecyclerView.Adapter<Room1Adapter.MyViewHolder> {



    private List<SubscriptionEntity> roomObjects;
    private Context context;
    private RecyclerView recyclerView;
    public int visibleThreshold = 10;
    public int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener loadMoreListener;

    public Room1Adapter(List<SubscriptionEntity> roomObjects, final Context context, RecyclerView recyclerView) {
        this.roomObjects = roomObjects;
        this.context = context;
        this.recyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached Do something
//                    if (loadMoreListener != null) {
//                        loadMoreListener.onLoadMore();
//                    }

                    Toast.makeText(context, "Rached End", Toast.LENGTH_SHORT).show();
                    loading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.loadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public void addLoadData(List<SubscriptionEntity> dataList){
        if(roomObjects != null){
            roomObjects.addAll(dataList);
//            this.notifyDataSetChanged();
        }
        setLoaded();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_room, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvUserName.setText(roomObjects.get(position).user.userName);
        holder.tvRoomId.setText(roomObjects.get(position).user.name);
        holder.tvStatus.setText(roomObjects.get(position).open?"Open":"Closed");
//        holder.tvDate.setText(new Date(roomObjects.get(position).updatedAt()).toString());
//        holder.tvTime.setText(new Date(roomObjects.get(position).updatedAt()).toString());


        holder.tvDate.setText((roomObjects.get(position).updatedAt != null)? roomObjects.get(position).updatedAt.toString():"");
//        holder.tvTime.setText(getTime(roomObjects.get(position).updatedAt()));


        holder.roomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("roomId",roomObjects.get(position).rId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView roomItem;
        TextView tvUserName;
        TextView tvRoomId;
        TextView tvStatus;
        TextView tvDate;
        TextView tvTime;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            roomItem = (CardView) itemView.findViewById(R.id.card_view);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvRoomId = (TextView) itemView.findViewById(R.id.tvRoomId);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }

    private String getTime(long timestamp ){
        Date date = new Date(timestamp);
        String ss = date.toString();
        return ss.substring(0,20);
    }
}