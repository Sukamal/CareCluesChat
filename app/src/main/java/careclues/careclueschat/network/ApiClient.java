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
//    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    public static final String CHAT_BASE_URL = "https://ticklechat.careclues.com/api/v1/";
    public static final String API_BASE_URL = "https://tickleapi.careclues.com/api/v1/";
//    patients/985/

    public static Retrofit chatRetrofit;
    public static Retrofit serverRetrofit;
    private static Gson gson;
    private static OkHttpClient client;

    public static Retrofit getChatRetrofit(){

        client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new ChatHeaderInterceptor())
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        if (chatRetrofit==null) {
            chatRetrofit = new Retrofit.Builder()
                    .baseUrl(CHAT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return chatRetrofit;
    }

    public static Retrofit getApiRetrofit(){

        client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new ServerHeaderInterceptor())
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        if (serverRetrofit==null) {
            serverRetrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return serverRetrofit;
    }
}
