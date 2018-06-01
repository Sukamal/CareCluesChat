package careclues.careclueschat.feature.chat;

import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import careclues.careclueschat.R;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.model.PostsModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.ServiceCallBack;

public class ChatActivity extends BaseActivity implements ChatContract.view{

    private ChatPresenter presenter;

    @BindView(R.id.tvText)
    TextView tvText;

    @Override
    public int getContentLayout() {
        return R.layout.activity_chat;
    }

    @Override
    public void initComponents() {
        presenter = new ChatPresenter(this);
        executeServerTask();
    }

    @Override
    public void displyNextScreen() {

    }

    @Override
    public void displayMessage(String message) {

    }

    @Override
    public void displayProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    private void executeServerTask(){

        presenter.loadData(new ServiceCallBack<PostsModel>(PostsModel.class) {
            @Override
            public void onSuccess(PostsModel response) {
                if(response != null){
                    System.out.println("------------onSuccess-----------");
                    System.out.println("Body : "+ response.getBody());
                    System.out.println("Title : "+ response.getTitle());
                    System.out.println("UserId : "+ response.getUserId());
                    System.out.println("Id : "+ response.getId());
                }
            }

            @Override
            public void onFailure(List<NetworkError> errorList) {

            }
        });

    }
}
