package careclues.careclueschat.feature.room;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.common.OnLoadMoreListener;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.util.DateFormatter;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> implements DateFormatter.Formatter{



    public List<RoomAdapterModel> roomObjects;
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
        holder.tvStatus.setText(roomObjects.get(position).status + "\n" + roomObjects.get(position).Id + "\n" + roomObjects.get(position).roomName);
        holder.position = position;
        if(roomObjects.get(position).userName != null){
            String avatarPath = "https://ticklechat.careclues.com/avatar/" + roomObjects.get(position).userName;

            Picasso.with(context)
                    .load(avatarPath)
                    .into(holder.imageView);
        }else{
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
        }

        if(roomObjects.get(position).display){
            holder.tvChange.setVisibility(View.VISIBLE);
        }else{
            holder.tvChange.setVisibility(View.GONE);
        }


        if(roomObjects.get(position).updatedAt != null){
            holder.tvDate.setText(format(roomObjects.get(position).updatedAt));
            holder.tvTime.setText(DateFormatter.format(roomObjects.get(position).updatedAt, DateFormatter.Template.TIME));

        }
        holder.roomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(itemClickListner != null){
                itemClickListner.onListItemClick(position,roomObjects.get(position).Id);
            }


//                Intent intent = new Intent(context, ChatActivity.class);
////                Intent intent = new Intent(context, TestChatACtivity.class);
//                intent.putExtra("roomId",roomObjects.get(position).Id);
//                context.startActivity(intent);
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

        RelativeLayout roomItem;
        TextView tvUserName;
        TextView tvRoomId;
        TextView tvStatus;
        TextView tvDate;
        TextView tvTime;
        TextView tvChange;
        ImageView imageView;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);

            roomItem = (RelativeLayout) itemView.findViewById(R.id.rr_item);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvRoomId = (TextView) itemView.findViewById(R.id.tvRoomId);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            tvChange = (TextView) itemView.findViewById(R.id.tvChange);

        }
    }

    private String getTime(long timestamp ){
        Date date = new Date(timestamp);
        String ss = date.toString();
        return ss.substring(0,20);
    }

    private onAdapterItemClickListner itemClickListner;
    public interface onAdapterItemClickListner{
        public void onListItemClick(int position, String roomId);
    }

    public void setAdapterItemClickListner(onAdapterItemClickListner itemClickListner){
        this.itemClickListner = itemClickListner;
    }
}
