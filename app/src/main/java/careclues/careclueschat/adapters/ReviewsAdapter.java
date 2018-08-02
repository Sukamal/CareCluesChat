package careclues.careclueschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.model.FacilityModel;
import careclues.careclueschat.model.QualificationModel;
import careclues.careclueschat.model.ReviewModel;
import careclues.careclueschat.model.ReviewerModel;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private List<ReviewModel> list;

    public void addMessage(List<ReviewModel> data) {
        list.addAll(data);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewer;
        public RatingBar ratingBar;
        public TextView review;

        public MyViewHolder(View view) {
            super(view);
            reviewer = (TextView) view.findViewById(R.id.reviewer);
            review = (TextView) view.findViewById(R.id.review);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }

    public ReviewsAdapter(List<ReviewModel> list) {
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_rating, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviewModel reviewModel = list.get(position);
        holder.reviewer.setText(reviewModel.getReviewer().getFirstName());
        holder.review.setText(reviewModel.getContent());
        holder.ratingBar.setRating(reviewModel.getRating());
    }

    @Override
    public int getItemCount() {
        Log.d("ddfd", "getItemCount: " + list.size());
        return list.size();
    }

}
