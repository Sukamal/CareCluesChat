package careclues.careclueschat.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
//import rx.Observable;

/**
 * Created by SukamalD on 27-12-2017.
 */

public interface ApiInterface {

    @GET("posts")
    Call<ResponseBody> getRawPostsData();

    @GET("posts/{user}")
    Call<ResponseBody> getUserPosts(@Path("user") int user);




}
