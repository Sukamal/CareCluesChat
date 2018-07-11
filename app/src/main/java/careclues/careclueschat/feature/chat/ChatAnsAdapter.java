package careclues.careclueschat.feature.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.util.DateFormatter;

/**
 * Created by SukamalD on 6/15/2018.
 */

public class ChatAnsAdapter extends RecyclerView.Adapter<ChatAnsAdapter.MyViewHolder> {

    private List<String> ansList;
    private Context context;

    public ChatAnsAdapter(Context context, List<String> ansList){
        this.context = context;
        this.ansList = ansList ;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_ans, parent, false);
        ChatAnsAdapter.MyViewHolder viewHolder = new ChatAnsAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String ans = ansList.get(position);
        holder.tvAns.setText(ans);
    }

    @Override
    public int getItemCount() {
        return ansList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAns;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvAns = (TextView) itemView.findViewById(R.id.tv_ans);
        }
    }

}
