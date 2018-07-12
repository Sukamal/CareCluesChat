package careclues.careclueschat.feature.chat;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;

public class MoreAnswerView extends RecyclerView {

    private ChatAnsAdapter chatAnsAdapter;
    private LinearLayoutManager layoutManager;
    private Context context;


    public MoreAnswerView(Context context) {
        super(context);
        this.context = context;
    }

    public MoreAnswerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MoreAnswerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    public void displayAnswerList(List<ChatAnsModel> answers) {
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        chatAnsAdapter = new ChatAnsAdapter(context, answers,true);
        setAdapter(chatAnsAdapter);
    }


}
