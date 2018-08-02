package careclues.careclueschat.feature.doctordetail;

import android.util.Log;


import butterknife.BindView;
import careclues.careclueschat.R;
import careclues.careclueschat.adapters.AchievementsAdapter;
import careclues.careclueschat.adapters.FacilitiesAdapter;
import careclues.careclueschat.adapters.LanguagesAdapter;
import careclues.careclueschat.adapters.QualificationsAdapter;
import careclues.careclueschat.adapters.ReviewsAdapter;
import careclues.careclueschat.adapters.ServicesAdapter;
import careclues.careclueschat.adapters.SpecializationsAdapter;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.model.AchievementResponse;
import careclues.careclueschat.model.FacilityResponse;
import careclues.careclueschat.model.LanguageResponse;
import careclues.careclueschat.model.QualificationResponse;
import careclues.careclueschat.model.ReviewResponse;
import careclues.careclueschat.model.ServiceResponse;
import careclues.careclueschat.model.SpecializationResponse;
import careclues.careclueschat.views.DoctorProfileCardView;

public class DoctorDetailActivity extends BaseActivity implements DoctorDetailContract.view {

    public DoctorDetailPresenter presenter;

    @BindView(R.id.facilityCard)
    DoctorProfileCardView facilityCard;

    @BindView(R.id.serviceCard)
    DoctorProfileCardView serviceCard;

    @BindView(R.id.educationCard)
    DoctorProfileCardView educationCard;

    @BindView(R.id.specializationCard)
    DoctorProfileCardView specializationCard;

    @BindView(R.id.awardCard)
    DoctorProfileCardView awardCard;

    @BindView(R.id.languagePreferenceCard)
    DoctorProfileCardView languagePreferenceCard;

    @BindView(R.id.reviewRatingCard)
    DoctorProfileCardView reviewRatingCard;

    @Override
    public int getContentLayout() {
        return R.layout.activity_doctor_detail;
    }

    @Override
    public void initComponents() {

        //presenter = new DoctorDetailPresenter(this, getApplication());
        presenter = new DoctorDetailPresenter(this, getApplication());

        //  ButterKnife.bind(this);

        facilityCard.setTitle(R.string.my_clinics);
        educationCard.setTitle(R.string.my_educations);
        serviceCard.setTitle(R.string.my_services);

        reviewRatingCard.setTitle(R.string.my_reviews);

        specializationCard.setTitle(R.string.my_specialization);
        awardCard.setTitle(R.string.my_awards);
        languagePreferenceCard.setTitle(R.string.my_language);


    }

    @Override
    public void showDoctorDetail() {

    }

    @Override
    public void showFacilities(FacilityResponse facilityResponse) {

        Log.d("ddd", "showFacilities: ");

        FacilitiesAdapter facilitiesAdapter = new FacilitiesAdapter(facilityResponse.getData());
        facilityCard.populate(facilitiesAdapter);

    }

    @Override
    public void showReviewRatings(ReviewResponse reviewResponse) {

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviewResponse.getData());
        reviewRatingCard.populate(reviewsAdapter);
    }

    @Override
    public void showEducations(QualificationResponse qualificationResponse) {

        QualificationsAdapter qualificationsAdapter = new QualificationsAdapter(qualificationResponse.getData());
        educationCard.populate(qualificationsAdapter);
    }

    @Override
    public void showServices(ServiceResponse serviceResponse) {

        ServicesAdapter servicesAdapter = new ServicesAdapter(serviceResponse.getData());
        serviceCard.populate(servicesAdapter);
    }

    @Override
    public void showSpecializations(SpecializationResponse specializationResponse) {

        SpecializationsAdapter specializationsAdapter = new SpecializationsAdapter(specializationResponse.getData());
        specializationCard.populate(specializationsAdapter);
    }

    @Override
    public void showAchievements(AchievementResponse achievementResponse) {

        AchievementsAdapter achievementsAdapter = new AchievementsAdapter(achievementResponse.getData());
        awardCard.populate(achievementsAdapter);
    }

    @Override
    public void showLanguages(LanguageResponse languageResponse) {

        LanguagesAdapter languagesAdapter =  new LanguagesAdapter(languageResponse.getData());
        languagePreferenceCard.populate(languagesAdapter);

    }

    @Override
    public void displayToastMessage(String message) {

    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

}

