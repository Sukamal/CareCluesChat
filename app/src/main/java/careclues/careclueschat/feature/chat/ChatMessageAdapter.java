package careclues.careclueschat.feature.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.room.RoomAdapter;
import careclues.careclueschat.model.RoomAdapterModel;
import careclues.careclueschat.util.DateFormatter;

/**
 * Created by SukamalD on 6/15/2018.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> implements DateFormatter.Formatter {

    private List<ChatMessageModel> messageList;
    private Context context;

    public ChatMessageAdapter(Context context,List<ChatMessageModel> messageList){
        this.context = context;
        this.messageList = messageList ;
        Collections.sort(this.messageList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_chat, parent, false);
        ChatMessageAdapter.MyViewHolder viewHolder = new ChatMessageAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatMessageModel chatMessageModel = messageList.get(position);

        Date previousDate = null;
        if(position>1){
            ChatMessageModel previousModel = messageList.get(position-1);
            previousDate = previousModel.createdAt;
        }
        setTimeTextVisibility(chatMessageModel.createdAt, previousDate, holder.tvDate);


        if(chatMessageModel.userId.equalsIgnoreCase("sachu-985")){
            holder.cvLeft.setVisibility(View.GONE);
            holder.cvRight.setVisibility(View.VISIBLE);
            holder.tvRight.setText(chatMessageModel.text);
        }else{
            holder.cvLeft.setVisibility(View.VISIBLE);
            holder.cvRight.setVisibility(View.GONE);
            holder.tvLeft.setText(chatMessageModel.text);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
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

    public void addMessage(List<ChatMessageModel> dataList){
        if(messageList != null){
            messageList.addAll(dataList);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cvLeft;
        TextView tvLeft;
        LinearLayout cvRight;
        TextView tvRight;
        TextView tvDate;



        public MyViewHolder(View itemView) {
            super(itemView);

            cvLeft = (LinearLayout) itemView.findViewById(R.id.card_view_left);
            tvLeft = (TextView) itemView.findViewById(R.id.tv_left);
            cvRight = (LinearLayout) itemView.findViewById(R.id.card_view_right);
            tvRight = (TextView) itemView.findViewById(R.id.tv_right);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    private void setTimeTextVisibility(Date date1, Date date2, TextView timeText){

        if(date2 == null){
            timeText.setVisibility(View.VISIBLE);
            timeText.setText(DateFormatter.format(date1, DateFormatter.Template.STRING_DAY_MONTH));
        }else {
            boolean isSameDay = DateFormatter.isSameDay(date1,date2);

            if(isSameDay){
                timeText.setVisibility(View.GONE);
                timeText.setText("");
//                timeText.setText("-------- " + DateFormatter.format(date2, DateFormatter.Template.STRING_DAY_MONTH));
            }else {
                timeText.setVisibility(View.VISIBLE);
                timeText.setText(DateFormatter.format(date2, DateFormatter.Template.STRING_DAY_MONTH));
            }

        }
    }
}
