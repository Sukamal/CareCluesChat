package careclues.careclueschat.feature.doctordetail;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import careclues.careclueschat.application.CareCluesChatApplication;
//import careclues.careclueschat.feature.chat.ChatActivity;
import careclues.careclueschat.feature.chat.ChatContract;
import careclues.careclueschat.feature.common.RoomDataPresenter;
import careclues.careclueschat.feature.room.RoomContract;
import careclues.careclueschat.model.AchievementResponse;
import careclues.careclueschat.model.FacilityResponse;
import careclues.careclueschat.model.QualificationResponse;
import careclues.careclueschat.model.ReviewResponse;
import careclues.careclueschat.model.ServiceResponse;
import careclues.careclueschat.model.SpecializationResponse;
import careclues.careclueschat.network.ApiClient;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServerApiInterface;
import careclues.careclueschat.network.ServiceCallBack;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DoctorDetailPresenter implements DoctorDetailContract.presenter {

    private DoctorDetailContract.view view;
    private Application application;

    public DoctorDetailPresenter(DoctorDetailContract.view view, Application application) {
        this.view = view;
        this.application = application;

        getFacilities();
        getReviewRatings();
        getEducations();
        getServices();
        getSpecializations();
        getAchievements();
        getLanguges();

    }

    @Override
    public void getFacilities() {

        String link = "https://tickleapi.careclues.com/api/v1/public/physicians/671/facilities";

        Call<ResponseBody> call = ApiClient.getApiRetrofit().create(ServerApiInterface.class).getServerResponse(link);
        RestApiExecuter apiExecuter = RestApiExecuter.getInstance();
        RestApiExecuter.execute(call, new ServiceCallBack<FacilityResponse>(FacilityResponse.class) {

            @Override
            public void onSuccess(final FacilityResponse response) {
                view.showFacilities(response);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }

        });
    }

    @Override
    public void getReviewRatings() {

        String serviceLink = "https://tickleapi.careclues.com/api/v1/public/physicians/671/reviews";

        Call<ResponseBody> call = ApiClient.getApiRetrofit().create(ServerApiInterface.class).getServerResponse(serviceLink);
        RestApiExecuter apiExecuter = RestApiExecuter.getInstance();
        RestApiExecuter.execute(call, new ServiceCallBack<ReviewResponse>(ReviewResponse.class) {

            @Override
            public void onSuccess(final ReviewResponse response) {
                view.showReviewRatings(response);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }

        });

    }


    public void getServices() {

        String serviceLink = "https://tickleapi.careclues.com/api/v1/public/physicians/671/services";

        Call<ResponseBody> call = ApiClient.getApiRetrofit().create(ServerApiInterface.class).getServerResponse(serviceLink);
        RestApiExecuter apiExecuter = RestApiExecuter.getInstance();
        RestApiExecuter.execute(call, new ServiceCallBack<ServiceResponse>(ServiceResponse.class) {

            @Override
            public void onSuccess(final ServiceResponse response) {
                view.showServices(response);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }

        });

    }

    public void getEducations() {

        String serviceLink = "https://tickleapi.careclues.com/api/v1/public/physicians/671/qualifications";

        Call<ResponseBody> call = ApiClient.getApiRetrofit().create(ServerApiInterface.class).getServerResponse(serviceLink);
        RestApiExecuter apiExecuter = RestApiExecuter.getInstance();
        RestApiExecuter.execute(call, new ServiceCallBack<QualificationResponse>(QualificationResponse.class) {

            @Override
            public void onSuccess(final QualificationResponse response) {
                view.showEducations(response);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }

        });

    }

    public void getSpecializations() {

        String serviceLink = "https://tickleapi.careclues.com/api/v1/public/physicians/671/specializations";

        Call<ResponseBody> call = ApiClient.getApiRetrofit().create(ServerApiInterface.class).getServerResponse(serviceLink);
        RestApiExecuter apiExecuter = RestApiExecuter.getInstance();
        RestApiExecuter.execute(call, new ServiceCallBack<SpecializationResponse>(SpecializationResponse.class) {

            @Override
            public void onSuccess(final SpecializationResponse response) {
                view.showSpecializations(response);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }

        });

    }

    public void getAchievements() {

        String serviceLink = "https://tickleapi.careclues.com/api/v1/public/physicians/671/achievements";

        Call<ResponseBody> call = ApiClient.getApiRetrofit().create(ServerApiInterface.class).getServerResponse(serviceLink);
        RestApiExecuter apiExecuter = RestApiExecuter.getInstance();
        RestApiExecuter.execute(call, new ServiceCallBack<AchievementResponse>(AchievementResponse.class) {

            @Override
            public void onSuccess(final AchievementResponse response) {
                view.showAchievements(response);
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }

        });

    }

    @Override
    public void getLanguges() {

    }

}
