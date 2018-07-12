package careclues.careclueschat.feature.chat;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.common.OnAdapterItemClickListener;

public class MoreAnswerCustomView extends ConstraintLayout implements View.OnClickListener, OnAdapterItemClickListener{

    private ChatAnsAdapter chatAnsAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Button okButton;
    private Button cancelButton;
    private Context context;
    private AnswerSelectedListner selectedListner;
    private List<String> answers;
    private boolean isMultiSelect;


    public interface AnswerSelectedListner{
        void onAnswerSelected(List<String> answers);
    }

    public void setAnswerSelectedListner(AnswerSelectedListner selectedListner){
        this.selectedListner = selectedListner;
    }


    public MoreAnswerCustomView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MoreAnswerCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MoreAnswerCustomView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }


    private void initView(){
        View view =  LayoutInflater.from(context).inflate(
                R.layout.more_ans_view, null);
        this.addView(view);
        recyclerView = findViewById(R.id.rv_moreAns);
        okButton = findViewById(R.id.btnOk);
        cancelButton = findViewById(R.id.btnCancel);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        answers = new ArrayList<>();

    }

    public void displayAnswerList(List<ChatAnsModel> answers, boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
        if(isMultiSelect){
            okButton.setVisibility(VISIBLE);
            cancelButton.setVisibility(VISIBLE);
            okButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }else {
            okButton.setVisibility(GONE);
            cancelButton.setVisibility(GONE);
        }
        chatAnsAdapter = new ChatAnsAdapter(context, answers,true);
        chatAnsAdapter.setItemClickListener(this);
        recyclerView.setAdapter(chatAnsAdapter);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnOk:
                if(selectedListner != null){
                    selectedListner.onAnswerSelected(answers);
                }
                break;

            case R.id.btnCancel:
                if(selectedListner != null){
                    selectedListner.onAnswerSelected(null);
                }
                break;
        }

    }

    @Override
    public void onItemClick(Object value) {
        ChatAnsModel ansModel = (ChatAnsModel) value;
        if(ansModel.isSelected){
            answers.add(ansModel.answer);
        }else{
            answers.remove(ansModel.answer);
        }

        if(!isMultiSelect){
            if(selectedListner != null){
                selectedListner.onAnswerSelected(answers);
            }
        }

    }
}
