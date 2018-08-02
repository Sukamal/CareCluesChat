package careclues.careclueschat.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.adapters.AchievementsAdapter;
import careclues.careclueschat.adapters.FacilitiesAdapter;
import careclues.careclueschat.adapters.QualificationsAdapter;
import careclues.careclueschat.adapters.ReviewsAdapter;
import careclues.careclueschat.adapters.ServicesAdapter;
import careclues.careclueschat.adapters.SpecializationsAdapter;
import careclues.careclueschat.model.AchievementModel;
import careclues.careclueschat.model.AchievementResponse;
import careclues.careclueschat.model.ApiResponse;
import careclues.careclueschat.model.FacilityModel;
import careclues.careclueschat.model.FacilityResponse;
import careclues.careclueschat.model.QualificationModel;
import careclues.careclueschat.model.QualificationResponse;
import careclues.careclueschat.model.ReviewModel;
import careclues.careclueschat.model.ReviewResponse;
import careclues.careclueschat.model.ServiceModel;
import careclues.careclueschat.model.ServiceResponse;
import careclues.careclueschat.model.SpecializationModel;
import careclues.careclueschat.model.SpecializationResponse;
import careclues.careclueschat.network.ApiClient;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServerApiInterface;
import careclues.careclueschat.network.ServiceCallBack;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DoctorProfileCardView extends CardView {

    TextView title;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;


    public DoctorProfileCardView(Context context) {
        super(context);
        initView(context);

    }

    public DoctorProfileCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DoctorProfileCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        inflate(context, R.layout.view_doctor_profile_card, this);

        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.serviceRv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void setTitle(int heading){
        title.setText(heading);
    }

    public void populate(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

}

//TODO
// customizing rating bar
// change icon
// show progressbar and retry option
// show more and
// Make one function call

