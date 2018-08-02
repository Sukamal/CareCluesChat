package careclues.careclueschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.model.FacilityModel;
import careclues.careclueschat.model.QualificationModel;

public class QualificationsAdapter extends RecyclerView.Adapter<QualificationsAdapter.MyViewHolder> {

    private List<QualificationModel> list;

    public void addMessage(List<QualificationModel> data) {
        list.addAll(data);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

    public QualificationsAdapter(List<QualificationModel> list) {
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
        QualificationModel qualificationModel = list.get(position);
        holder.name.setText((CharSequence) qualificationModel.toString());
    }

    @Override
    public int getItemCount() {

        Log.d("ddfd", "getItemCount: " + list.size());
        return list.size();
    }

}
