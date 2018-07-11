package careclues.careclueschat.network;

import careclues.careclueschat.model.LoginRequest;
import careclues.careclueschat.model.CreateRoomRequest;
import careclues.careclueschat.model.SendMassageRequest;
import careclues.careclueschat.model.SetTopicRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
//import rx.Observable;

/**
 * Created by SukamalD on 27-12-2017.
 */

public interface ChatApiInterface {

//    @GET("posts")
//    Call<ResponseBody> getRawPostsData();
//
//    @GET("posts/{user}")
//    Call<ResponseBody> getUserPosts(@Path("user") int user);

    @POST("login")
    Call<ResponseBody> doLogin(@Body LoginRequest request);

    @GET("rooms.get")
    Call<ResponseBody> getRooms();

    @GET("rooms.get")
    Call<ResponseBody> getRooms(@Query("updatedSince") String updatedSince);

    @GET("subscriptions.get")
    Call<ResponseBody> getSubscription();

    @GET("subscriptions.get")
    Call<ResponseBody> getSubscription(@Query("updatedSince") String updatedSince);

    @GET("groups.members")
    Call<ResponseBody> getRoomMembers(@Query("roomId") String roomId);

    @GET("groups.history")
    Call<ResponseBody> getChatHistory(@Query("roomId") String roomId,@Query("count") int count,@Query("latest") String updatedSince);

    @POST("groups.create")
    Call<ResponseBody> createRoom(@Body CreateRoomRequest request);

    @POST("groups.setTopic")
    Call<ResponseBody> setRoomTopic(@Body SetTopicRequest request);

    @POST("chat.sendMessage")
    Call<ResponseBody> sendNewMessage(@Body SendMassageRequest request);

}
