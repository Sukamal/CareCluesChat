package careclues.careclueschat.feature.chat;

public class ChatPresenter implements ChatContract.presenter {

    private ChatContract.view view;

    public ChatPresenter(ChatContract.view view){
        this.view = view;
    }



}
