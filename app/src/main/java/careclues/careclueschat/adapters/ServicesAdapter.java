package careclues.careclueschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.model.ServiceModel;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    private List<ServiceModel> serviceList;

    public void addMessage(List<ServiceModel> data) {
        serviceList.addAll(data);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

    public ServicesAdapter(List<ServiceModel> serviceList) {
        this.serviceList = serviceList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_service, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServiceModel service = serviceList.get(position);
        holder.name.setText(service.getName());
    }

    @Override
    public int getItemCount() {

        Log.d("ddfd", "getItemCount: " + serviceList.size());
        return serviceList.size();
    }

}
