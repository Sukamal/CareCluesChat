package careclues.careclueschat.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SukamalD on 27-12-2017.
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d("LOG","----------API CALL -------------------");
        Log.d("LOG",request.url().toString());
        Log.d("LOG",request.toString());
        Response response = chain.proceed(request);

//        Log.d("LOG RESPONSE",response.body().string());

        return response;
    }

}
