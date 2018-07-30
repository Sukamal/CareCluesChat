package careclues.careclueschat.network;

import java.io.IOException;

import careclues.careclueschat.model.AuthToken;
import careclues.careclueschat.util.AppConstant;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class ServerHeaderFileUploadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        AuthToken authToken = AuthToken.getInstance();
        Request original = chain.request();
        Request request = original.newBuilder()
//                .header("Content-Type", "application/json")
                .header("Authorization", AppConstant.PASSWORD !=null ?AppConstant.PASSWORD:"")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
