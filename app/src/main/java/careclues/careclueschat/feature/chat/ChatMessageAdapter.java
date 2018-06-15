package careclues.careclueschat.feature.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        this.messageList = messageList;
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
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cvLeft;
        TextView tvLeft;
        CardView cvRight;
        TextView tvRight;

        public MyViewHolder(View itemView) {
            super(itemView);

            cvLeft = (CardView) itemView.findViewById(R.id.card_view_left);
            tvLeft = (TextView) itemView.findViewById(R.id.tv_left);
            cvRight = (CardView) itemView.findViewById(R.id.card_view_right);
            tvRight = (TextView) itemView.findViewById(R.id.tv_right);
        }
    }
}
