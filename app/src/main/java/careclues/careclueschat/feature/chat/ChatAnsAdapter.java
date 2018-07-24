package careclues.careclueschat.feature.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.common.OnAdapterItemClickListener;
import careclues.careclueschat.model.PhysicianResponseModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;

/**
 * Created by SukamalD on 6/15/2018.
 */

public class ChatAnsAdapter extends RecyclerView.Adapter<ChatAnsAdapter.MyViewHolder> implements Filterable{

    private List<ChatAnsModel> ansList;
    private List<ChatAnsModel> mFilteredList;

    private Context context;
    private boolean isMultiSelect;
    private OnAdapterItemClickListener itemClickListener;


    public ChatAnsAdapter(Context context, List<ChatAnsModel> ansList, boolean isMultiSelect){
        this.context = context;
        this.ansList = ansList ;
        this.mFilteredList = ansList;
        this.isMultiSelect = isMultiSelect;

    }

    public void setItemClickListener(OnAdapterItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ChatAnsModel ansModel = mFilteredList.get(position);
        String ans = ansModel.answer;
        holder.position = position;

        holder.llAnsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMultiSelect){
                    ansModel.isSelected = !ansModel.isSelected;
                }else{
                    for(ChatAnsModel ansModel1 : mFilteredList){
                        ansModel1.isSelected = false;
                    }
                    ansModel.isSelected = !ansModel.isSelected;

                }
                notifyDataSetChanged();
                itemClickListener.onItemClick(ansModel);
            }
        });


        holder.tvAns.setText(ans);
        if(ansModel.isSelected){
            holder.tvAns.setTextColor(context.getResources().getColor(R.color.blue));
        }else{
            holder.tvAns.setTextColor(context.getResources().getColor(R.color.black));

        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = ansList;
                }else{
                    List<ChatAnsModel> filterList = new ArrayList<>();
                    for(ChatAnsModel ansModel : ansList){
                        if (ansModel.answer.toLowerCase().contains(charString)) {
                            filterList.add(ansModel);
                        }
                    }
                    mFilteredList = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<ChatAnsModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAns;
        int position;
        LinearLayout llAnsItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            llAnsItem = (LinearLayout) itemView.findViewById(R.id.ll_ans_item);
            tvAns = (TextView) itemView.findViewById(R.id.tv_ans);

        }
    }



}
