package careclues.careclueschat.network;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import careclues.careclueschat.feature.login.model.LoginRequest;
import careclues.careclueschat.model.AuthToken;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class RestApiExecuter {

    private static RestApiExecuter apiExecuter;
    private AuthToken authToken;


    private RestApiExecuter(){

    }

    public static RestApiExecuter getInstance(){
        if(apiExecuter == null){
            synchronized (RestApiExecuter.class){
                if(apiExecuter == null){
                    apiExecuter = new RestApiExecuter();
                }
            }
        }
        return apiExecuter;
    }

    public static<T> void execute(Call<ResponseBody> call, final ServiceCallBack<T> serviceCallBack) {

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    serviceCallBack.onFailure(Arrays.asList(new NetworkError("","Api Error")));
                    return;
                }

                try {
                    String rawRespons = response.body().string();
                    JSONObject jsonObject = new JSONObject(rawRespons);
                    Gson gson = new Gson();
                    T resPonse = (T) gson.fromJson(jsonObject.toString(),serviceCallBack.getClassType());
                    serviceCallBack.onSuccess(resPonse);

                } catch (IOException e) {
                    serviceCallBack.onFailure(Arrays.asList(new NetworkError("","Api Error")));
                } catch (JSONException e) {
                    serviceCallBack.onFailure(Arrays.asList(new NetworkError("","Api Error")));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                serviceCallBack.onFailure(null);
            }
        });
    }

    public AuthToken getAuthToken(){
        return authToken;
    }

    public <T> void doLogin(String userId,String password,final ServiceCallBack<T> serviceCallBack) {
        LoginRequest loginRequest  = new LoginRequest();
        loginRequest.setUsername(userId);
        loginRequest.setPassword(password);
        authToken = AuthToken.getInstance();
        Call<ResponseBody> call = ApiClient.getRetrofit().create(ApiInterface.class).doLogin(loginRequest);
        execute(call,serviceCallBack);
    }

    public <T> void getRooms(final ServiceCallBack<T> serviceCallBack) {
        Call<ResponseBody> call = ApiClient.getRetrofit().create(ApiInterface.class).getRooms();
        execute(call,serviceCallBack);
    }

    public <T> void getSubscription(final ServiceCallBack<T> serviceCallBack) {
        Call<ResponseBody> call = ApiClient.getRetrofit().create(ApiInterface.class).getSubscription();
        execute(call,serviceCallBack);
    }

    public <T> void getRoomMembers(String roomId,final ServiceCallBack<T> serviceCallBack) {
        Call<ResponseBody> call = ApiClient.getRetrofit().create(ApiInterface.class).getRoomMembers(roomId);
        execute(call,serviceCallBack);
    }

    public <T> void getChatMessage(String roomId,int count,final ServiceCallBack<T> serviceCallBack) {
        Call<ResponseBody> call = ApiClient.getRetrofit().create(ApiInterface.class).getChatHistory(roomId,count);
        execute(call,serviceCallBack);
    }



}
