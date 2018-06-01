package careclues.careclueschat.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by SukamalD on 19-02-2018.
 */

public class NetworkService {

    private static Retrofit retrofitInstance;

    static{
        retrofitInstance = ApiClient.getClient();
    }

    public static Call<ResponseBody> getAllPosts(){

        return retrofitInstance.create(ApiInterface.class).getRawPostsData();
    }

}
