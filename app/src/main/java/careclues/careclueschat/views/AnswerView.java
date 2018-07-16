package careclues.careclueschat.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.chat.ChatAnsAdapter;
import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.chat.chatmodel.PatientModel;
import careclues.careclueschat.feature.common.OnAdapterItemClickListener;
import careclues.careclueschat.model.DataModel;

/**
 * Created by SukamalD on 7/15/2018.
 */

public class AnswerView extends RelativeLayout {

    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManagermore;

    private RecyclerView rvAnswers;
    private RecyclerView rvMoreAnswers;
    private ImageButton ibSubmitAns;
    private Context context;
    private List<ChatAnsModel> answers;
    private List<ChatAnsModel> lessAnsList;
    private ChatAnsAdapter chatAnsAdapter;
    private ChatAnsAdapter moreChatAnsAdapter;
    private List<String> selectedAnswerList;
    private boolean isMultiSelect;



    public AnswerView(Context context) {
        this(context,null);
    }

    public AnswerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AnswerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setAnswerList(List<ChatAnsModel> answers, boolean isMultiSelect){
        this.answers = answers;
        this.isMultiSelect = isMultiSelect;

        if(answers.size() > 5){
            lessAnsList = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                lessAnsList.add(answers.get(i));
            }
            lessAnsList.add(new ChatAnsModel("Load More",false));

        }else{
            lessAnsList = answers;
        }
        displayAnsList();

    }

    private void initView(Context context){
        this.context = context;
        inflate(context, R.layout.view_answer,this);
        rvAnswers = (RecyclerView) findViewById(R.id.rvAnswers);
        rvMoreAnswers = (RecyclerView) findViewById(R.id.rvMoreAnswers);
        ibSubmitAns = (ImageButton) findViewById(R.id.ib_submit_ans);
        selectedAnswerList = new ArrayList<>();
        initClickListner();
        initAnsRecycleView();
        initMoreAnsRecycleView();
        //        setAnswerList(populateTestAnsList(),true);

    }

    private void initClickListner(){
        ibSubmitAns.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedAnswers();
            }
        });
    }

    private void initAnsRecycleView() {
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAnswers.setLayoutManager(layoutManager);
    }

    private void initMoreAnsRecycleView() {
        layoutManagermore = new LinearLayoutManager(context);
        layoutManagermore.setOrientation(LinearLayoutManager.VERTICAL);
        rvMoreAnswers.setLayoutManager(layoutManagermore);
    }



    private void displayAnsList(){
        chatAnsAdapter = new ChatAnsAdapter(context,lessAnsList,isMultiSelect);
        rvAnswers.setAdapter(chatAnsAdapter);
        rvMoreAnswers.setVisibility(GONE);
        rvAnswers.setVisibility(VISIBLE);

        chatAnsAdapter.setItemClickListener(new OnAdapterItemClickListener() {
            @Override
            public void onItemClick(Object value) {
                ChatAnsModel ansModel = (ChatAnsModel) value;
                if(ansModel.answer.equals("Load More")){
                    rvAnswers.setVisibility(GONE);
                    rvMoreAnswers.setVisibility(VISIBLE);
                    displayMoreAnsList();
                }else{
                    if(ansModel.isSelected){
                        selectedAnswerList.add(ansModel.answer);
                    }else{
                        selectedAnswerList.remove(ansModel.answer);
                    }
                }
            }
        });
    }

    private void displayMoreAnsList(){
        moreChatAnsAdapter = new ChatAnsAdapter(context,answers,isMultiSelect);
        rvMoreAnswers.setAdapter(moreChatAnsAdapter);
        moreChatAnsAdapter.setItemClickListener(new OnAdapterItemClickListener() {
            @Override
            public void onItemClick(Object value) {
                ChatAnsModel ansModel = (ChatAnsModel) value;
                if(ansModel.isSelected){
                    selectedAnswerList.add(ansModel.answer);
                }else{
                    selectedAnswerList.remove(ansModel.answer);
                }
            }
        });
    }


    private void displaySelectedAnswers(){
        String selectedAnswers = "";
        if(selectedAnswerList != null){
            for(String ans : selectedAnswerList){
                if(selectedAnswers != null && selectedAnswers.length()>0){
                    selectedAnswers = selectedAnswers + "\n";
                }
                selectedAnswers = selectedAnswers + ans;
            }

            Toast.makeText(getContext(), selectedAnswers, Toast.LENGTH_SHORT).show();

        }
    }

    private List<ChatAnsModel> populateTestAnsList(){
        List<ChatAnsModel> answers = new ArrayList<>();
        answers.add(new ChatAnsModel("Yes",false));
        answers.add(new ChatAnsModel("No",false));
        answers.add(new ChatAnsModel("Answer 1",false));
        answers.add(new ChatAnsModel("Answer 222222222",false));
        answers.add(new ChatAnsModel("Answer 3",false));
        answers.add(new ChatAnsModel("Answer 4222222222222222222",false));
        answers.add(new ChatAnsModel("Ans 5",false));
        answers.add(new ChatAnsModel("Answer 6",false));
        answers.add(new ChatAnsModel("Answer 7345gdf dfgd",false));
        return answers;
    }





}
