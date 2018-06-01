package careclues.careclueschat.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SukamalD on 27-12-2017.
 */

public class ApiClient {
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public static Retrofit retrofit;
    private static Gson gson;
    private static OkHttpClient client;

    public static Retrofit getClient(){
        client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
