package careclues.careclueschat.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import careclues.careclueschat.util.AppConstant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SukamalD on 27-12-2017.
 */

public class ApiClient {

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
                    .baseUrl(AppConstant.CHAT_BASE_URL)
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
                    .baseUrl(AppConstant.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return serverRetrofit;
    }

    public static Retrofit getUploadFileRetrofit(){

        client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new ServerHeaderFileUploadInterceptor())
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        if (serverRetrofit==null) {
            serverRetrofit = new Retrofit.Builder()
                    .baseUrl(AppConstant.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return serverRetrofit;
    }
}
