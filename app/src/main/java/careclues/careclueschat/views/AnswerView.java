package careclues.careclueschat.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;
import careclues.careclueschat.feature.chat.ChatAnsAdapter;
import careclues.careclueschat.feature.chat.ChatPresenter1;
import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.common.OnAdapterItemClickListener;
import careclues.careclueschat.model.FeeRangeModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.LanguageModel;
import careclues.careclueschat.model.SymptomModel;

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
    private List<ChatAnsModel> selectedAnswerList;
    private boolean isMultiSelect;
    private AnswerSelectionListner answerSelectionListner;
    private ChatPresenter1.ControlType controlType;
    private SearchView svSearch;



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

    public void setAnsSelectionListner(AnswerSelectionListner listner){
        this.answerSelectionListner = listner;
    }


    public void removeAllListner(){
        this.answerSelectionListner = null;
    }

    public void setAnswerList(List<ChatAnsModel> answers, ChatPresenter1.ControlType type,  boolean isMultiSelect){

        resetlist();
        this.answers = answers;
        this.isMultiSelect = isMultiSelect;
        this.controlType = type;

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

    private void resetlist(){
        this.answers = null;
        this.lessAnsList = null;
        this.selectedAnswerList = null;
        selectedAnswerList = new ArrayList<>();

    }

    private void initView(Context context){
        this.context = context;
        inflate(context, R.layout.view_answer,this);
        rvAnswers = (RecyclerView) findViewById(R.id.rvAnswers);
        rvMoreAnswers = (RecyclerView) findViewById(R.id.rvMoreAnswers);
        ibSubmitAns = (ImageButton) findViewById(R.id.ib_submit_ans);
        svSearch = (SearchView) findViewById(R.id.sv_search);
        selectedAnswerList = new ArrayList<>();
        initListner();
        initAnsRecycleView();
        initMoreAnsRecycleView();
        //        setAnswerList(populateTestAnsList(),true);

    }

    private void initListner(){
        ibSubmitAns.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedAnswers();
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(moreChatAnsAdapter != null){
                    moreChatAnsAdapter.getFilter().filter(newText);

                }
                return true;
            }
        });
    }

    private void initAnsRecycleView() {
        svSearch.setVisibility(GONE);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAnswers.setLayoutManager(layoutManager);
    }

    private void initMoreAnsRecycleView() {
        svSearch.setVisibility(VISIBLE);
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
                        selectedAnswerList.add(ansModel);
                    }else{
                        selectedAnswerList.remove(ansModel);
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
                    selectedAnswerList.add(ansModel);
                }else{
                    selectedAnswerList.remove(ansModel);
                }
            }
        });
    }


    private void displaySelectedAnswers(){
        Log.d("AnswerView", "displaySelectedAnswers: " + answers.toString());
        String selectedAnswers = "";
        if(selectedAnswerList != null){
            for(ChatAnsModel ans : selectedAnswerList){
                if(selectedAnswers != null && selectedAnswers.length()>0){
                    selectedAnswers = selectedAnswers + "\n";
                }
                selectedAnswers = selectedAnswers + ans.answer;
            }
            Toast.makeText(getContext(), selectedAnswers, Toast.LENGTH_SHORT).show();

            if(controlType == ChatPresenter1.ControlType.CONTROL_HEALTH_TOPIC_SELECT){
                answerSelectionListner.onHealthTopicSelected((HealthTopicModel) selectedAnswerList.get(0).ansObject);
            }else if(controlType == ChatPresenter1.ControlType.CONTROL_FEE_SELECT){
                answerSelectionListner.onFeeRangeSelect((FeeRangeModel) selectedAnswerList.get(0).ansObject);
            }else if(controlType == ChatPresenter1.ControlType.CONTROL_PRIMARY_SYMPTOM_SELECT){
                answerSelectionListner.onSymptomSelected((SymptomModel) selectedAnswerList.get(0).ansObject);
            }else if(controlType == ChatPresenter1.ControlType.CONTROL_SYMPTOM_SELECT){
                answerSelectionListner.onSymptomSelected((SymptomModel) selectedAnswerList.get(0).ansObject);
            }else if(controlType == ChatPresenter1.ControlType.CONTROL_SELECT){
                answerSelectionListner.onOptionSelected((String)selectedAnswerList.get(0).ansObject);
            }else if(controlType == ChatPresenter1.ControlType.CONTROL_SELECT_LANGUAGE){
                answerSelectionListner.onLanguageSelected(((LanguageModel)selectedAnswerList.get(0).ansObject).name);
            }else if(controlType == ChatPresenter1.ControlType.CONTROL_SELECT_AGE){
                answerSelectionListner.onAgeSelected((String)selectedAnswerList.get(0).ansObject);
            }

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
