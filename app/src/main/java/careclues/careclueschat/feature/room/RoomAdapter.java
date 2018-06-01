package careclues.careclueschat.feature.room;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocketchat.common.utils.Utils;
import com.rocketchat.core.model.Subscription;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.ChatActivity;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {



    private List<Subscription> roomObjects;
    private Context context;

    public RoomAdapter(List<Subscription> roomObjects, Context context) {
        this.roomObjects = roomObjects;
        this.context = context;
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
        holder.tvUserName.setText(roomObjects.get(position).user().username());
        holder.tvRoomId.setText(roomObjects.get(position).name());
        holder.tvStatus.setText(roomObjects.get(position).open()?"Open":"Closed");
//        holder.tvDate.setText(new Date(roomObjects.get(position).updatedAt()).toString());
//        holder.tvTime.setText(new Date(roomObjects.get(position).updatedAt()).toString());

        holder.tvDate.setText(getTime(roomObjects.get(position).updatedAt()));
        holder.tvTime.setText(getTime(roomObjects.get(position).updatedAt()));


        holder.roomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("roomId",roomObjects.get(position).roomId());
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
