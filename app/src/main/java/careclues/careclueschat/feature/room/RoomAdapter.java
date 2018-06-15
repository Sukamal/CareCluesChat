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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.chat.TestChatACtivity;
import careclues.careclueschat.feature.common.OnLoadMoreListener;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.util.AppUtil;
import careclues.careclueschat.util.DateFormatter;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> implements DateFormatter.Formatter{



    private List<RoomAdapterModel> roomObjects;
    private Context context;
    private RecyclerView recyclerView;
    public int visibleThreshold = 10;
    public int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener loadMoreListener;

    public RoomAdapter(List<RoomAdapterModel> roomObjects, final Context context, RecyclerView recyclerView) {
        this.roomObjects = new ArrayList<>();
        this.roomObjects.addAll(roomObjects);
        Collections.sort(roomObjects);
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

                System.out.println("totalItemCount : " + String.valueOf(totalItemCount)+"lastVisibleItem : "+ String.valueOf(lastVisibleItem));

                if (!loading && totalItemCount == lastVisibleItem +1) {
                    // End has been reached Do something
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore();
                    }

                    Toast.makeText(context, "Rached End", Toast.LENGTH_SHORT).show();
                    loading = true;
                }

//                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                    // End has been reached Do something
//                    if (loadMoreListener != null) {
//                        loadMoreListener.onLoadMore();
//                    }
//
//                    Toast.makeText(context, "Rached End", Toast.LENGTH_SHORT).show();
//                    loading = true;
//                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.loadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public void addLoadData(List<RoomAdapterModel> dataList){
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
        holder.tvUserName.setText(roomObjects.get(position).name);
        holder.tvRoomId.setText(roomObjects.get(position).description);
        holder.tvStatus.setText(roomObjects.get(position).Id);

        if(roomObjects.get(position).updatedAt != null){
            holder.tvDate.setText(format(roomObjects.get(position).updatedAt));
            holder.tvTime.setText(DateFormatter.format(roomObjects.get(position).updatedAt, DateFormatter.Template.TIME));

        }
        holder.roomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
//                Intent intent = new Intent(context, TestChatACtivity.class);
                intent.putExtra("roomId",roomObjects.get(position).Id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomObjects.size();
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "Today";
        } else if (DateFormatter.isYesterday(date)) {
            return "Yesterday";
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
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
