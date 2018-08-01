package careclues.careclueschat.network;

import careclues.careclueschat.model.AddMoneyRequest;
import careclues.careclueschat.model.CreateTextConsultantModel;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.FileUploadRequest;
import careclues.careclueschat.model.LanguageModel;
import careclues.careclueschat.model.LinkWalletSendOtpRequest;
import careclues.careclueschat.model.LinkWalletValidateOtpRequest;
import careclues.careclueschat.model.UpdatePaymentModeRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    public Call<ResponseBody> addUserLanguage(@Url String url, @Body LanguageModel body );

    @POST
    public Call<ResponseBody> createTextConsultant(@Url String url, @Body CreateTextConsultantModel body );

    @POST
    public Call<ResponseBody> linkWalletSendOtp(@Url String url, @Body LinkWalletSendOtpRequest body );

    @POST
    public Call<ResponseBody> linkWalletValidateOtp(@Url String url, @Body LinkWalletValidateOtpRequest body );

    @PATCH
    public Call<ResponseBody> updatePaymentMode(@Url String url, @Body UpdatePaymentModeRequest body );

    @POST
    public Call<ResponseBody> addMoneyToWallet(@Url String url, @Body AddMoneyRequest body );

    @POST
    public Call<ResponseBody> payViaWallet(@Url String url );

    @POST
    public Call<ResponseBody> payViaGetway(@Url String url);

//    @POST
//    public Call<ResponseBody> uploadFile(@Url String url,@Body RequestBody bytes);

//    @POST
//    public Call<ResponseBody> uploadFile(@Url String url, @Body FileUploadRequest body );

    @POST
    @Multipart
    public Call<ResponseBody> uploadFile(@Url String url,@Part("description") RequestBody description, @Part MultipartBody.Part file);
}
