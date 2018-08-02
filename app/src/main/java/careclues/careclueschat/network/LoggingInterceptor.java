package careclues.careclueschat.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by SukamalD on 27-12-2017.
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d("LOG","----------API CALL -------------------");
        Log.d("URL : ",request.url().toString());
        if(request.body() != null){
            Log.d("Body : ",bodyToString(request));
        }
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();
        Log.d("RESPONSE : ", responseBodyString);

        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

        return newResponse;
    }

    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
