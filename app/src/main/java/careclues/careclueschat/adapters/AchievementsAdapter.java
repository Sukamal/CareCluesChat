package careclues.careclueschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.model.AchievementModel;
import careclues.careclueschat.model.FacilityModel;
import careclues.careclueschat.model.QualificationModel;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.MyViewHolder> {

    private List<AchievementModel> list;

    public void addMessage(List<AchievementModel> data) {
        list.addAll(data);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

    public AchievementsAdapter(List<AchievementModel> list) {
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_service, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AchievementModel achievementModel = list.get(position);
        holder.name.setText((CharSequence) achievementModel.getContent());
    }

    @Override
    public int getItemCount() {

        Log.d("ddfd", "getItemCount: " + list.size());
        return list.size();
    }

}
