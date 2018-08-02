package careclues.careclueschat.network;

import android.util.Log;

import java.io.IOException;

import careclues.careclueschat.model.AuthToken;
import careclues.careclueschat.util.AppConstant;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class ChatHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        AuthToken authToken = AuthToken.getInstance();
        Request original = chain.request();

        Log.d("Token", "intercept: " + authToken.getToken());
        Log.d("User_id", "intercept: " + authToken.getUserId());
        //Log.d(TAG, "intercept: ");("X-Auth-Token : ",authToken.getToken());
       //Log.d("X-User-Id : ",authToken.getUserId());

        Request request = original.newBuilder()
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", authToken.getToken() !=null ?authToken.getToken():"")
                .header("X-User-Id", authToken.getUserId() !=null ?authToken.getUserId():"")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
