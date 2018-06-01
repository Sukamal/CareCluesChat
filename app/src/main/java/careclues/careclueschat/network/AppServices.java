package careclues.careclueschat.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by SukamalD on 29-12-2017.
 */

public class AppServices {

    private static Retrofit retrofitInstance;

    static{
        retrofitInstance = ApiClient.getClient();
    }

    public static Call<ResponseBody> getAllPosts(){

        return retrofitInstance.create(ApiInterface.class).getRawPostsData();
    }

    public static Call<ResponseBody> getPosts(int user){

        return retrofitInstance.create(ApiInterface.class).getUserPosts(user);
    }


}
