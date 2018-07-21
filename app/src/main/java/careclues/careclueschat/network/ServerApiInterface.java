package careclues.careclueschat.network;

import careclues.careclueschat.model.CreateRoomRequest;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.LanguageModel;
import careclues.careclueschat.model.LoginRequest;
import careclues.careclueschat.model.SendMassageRequest;
import careclues.careclueschat.model.SetTopicRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by SukamalD on 27-12-2017.
 */

public interface ServerApiInterface {

    @GET("patients/{user_id}")
    Call<ResponseBody> getUserProfile(@Path("user_id") String userId);

    @GET("health_topics?text_consultation_availability=true")
    Call<ResponseBody> getHealthTopic();

    @GET
    public Call<ResponseBody> getServerResponse(@Url String url);

    @POST
    public Call<ResponseBody> createFamilyMember(@Url String url, @Body DataModel body );

    @POST
    public Call<ResponseBody> caddUserLanguage(@Url String url, @Body LanguageModel body );

}
