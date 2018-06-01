package careclues.careclueschat.network;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SukamalD on 29-12-2017.
 */

public class ServiceExecuter {


    public static<T> void execute(Call<ResponseBody> call, final ServiceCallBack<T> serviceCallBack) {

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response != null) {
                        String rawRespons = response.body().string();
                        JSONObject jsonObject = new JSONObject(rawRespons);
                        Gson gson = new Gson();
                        T resPonse = (T) gson.fromJson(jsonObject.toString(),serviceCallBack.getClassType());
                        serviceCallBack.onSuccess(resPonse);
                    }

                } catch (IOException e) {
                    serviceCallBack.onFailure(null);
                } catch (JSONException e) {
                    serviceCallBack.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                serviceCallBack.onFailure(null);
            }
        });
    }


}
