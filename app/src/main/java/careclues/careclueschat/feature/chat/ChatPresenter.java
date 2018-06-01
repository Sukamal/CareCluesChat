package careclues.careclueschat.feature.chat;

import java.util.List;

import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.network.AppServices;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.network.ServiceExecuter;

public class ChatPresenter implements ChatContract.presenter {

    private ChatContract.view view;

    public ChatPresenter(ChatContract.view view){
        this.view = view;
    }

    @Override
    public void loadData(ServiceCallBack<PostsModel> callback) {
        ServiceExecuter.execute(AppServices.getPosts(1),callback);

        /*Call<ResponseBody> call = AppServices.getAllPosts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String rawRespons = response.body().string();
                    System.out.println("Response : "+rawRespons);
                } catch (IOException e) {
                    System.out.println("ERROR : "+e.getMessage().toString());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("ERROR : "+t.getMessage().toString());
            }
        });*/

    }

}
