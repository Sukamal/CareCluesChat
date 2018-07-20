package careclues.careclueschat.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;
import careclues.careclueschat.feature.chat.ChatPresenter1;
import careclues.careclueschat.feature.chat.chatmodel.PatientModel;
import careclues.careclueschat.feature.common.OnAdapterItemClickListener;
import careclues.careclueschat.model.DataModel;

/**
 * Created by SukamalD on 7/15/2018.
 */

public class FamilyMemberView extends RelativeLayout {

    private LinearLayoutManager layoutManager;
    private RecyclerView rvFamillyMember;
    private Context context;
    private List<PatientModel> patientList;
    private ChatFamilyMemberAdapter chatFamilyMemberAdapter;
    private AnswerSelectionListner answerSelectionListner;
    private LinearLayout llNewMember;
    private ConstraintLayout llAddNewMember;
    private LinearLayout llViewMember;


    public FamilyMemberView(Context context) {
        this(context,null);
    }

    public FamilyMemberView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FamilyMemberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setAnsSelectionListner(AnswerSelectionListner listner){
        this.answerSelectionListner = listner;
    }


    private void initView(Context context){
        this.context = context;
        inflate(context, R.layout.view_family_member,this);
        rvFamillyMember = (RecyclerView) findViewById(R.id.rvFamillyMember);
        llNewMember = (LinearLayout) findViewById(R.id.llNewMember);
//        llAddNewMember = (ConstraintLayout) findViewById(R.id.ll_add_new_member);
//        llViewMember = (LinearLayout) findViewById(R.id.ll_view_member);

        initClickListner();
        initAnsRecycleView();
    }
    
    private void initClickListner(){
        llNewMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                llAddNewMember.setVisibility(VISIBLE);
                Toast.makeText(getContext(), "Add New Member", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAnsRecycleView() {
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvFamillyMember.setLayoutManager(layoutManager);
    }

    private void createSelfObject(){
        patientList = new ArrayList<>();
        PatientModel patientModel = new PatientModel();
        patientModel.displayName = "Self";
        if(CareCluesChatApplication.userProfile != null){
            patientModel.first_name = CareCluesChatApplication.userProfile.data.firstName;
            patientModel.last_name = CareCluesChatApplication.userProfile.data.lastName;
            patientModel.gender = CareCluesChatApplication.userProfile.data.gender;
            patientModel.self = true;
            patientModel.lLink = CareCluesChatApplication.userProfile.data.getLink("text_consultations");
        }

        patientList.add(patientModel);
    }

    private void addMember(DataModel dataModel){
        if(dataModel != null){
            PatientModel patientModel = new PatientModel();
            patientModel.displayName = dataModel.relationship;
            patientModel.first_name = dataModel.firstName;
            patientModel.last_name = dataModel.lastName;
            patientModel.gender = dataModel.gender;
            patientModel.self = false;
            patientModel.lLink = dataModel.getLink("text_consultations");
            try {
                patientModel.age = getAge(dataModel.dateOfBirth);
            } catch (Exception e) {
                e.printStackTrace();
            }
            patientList.add(patientModel);
        }
    }

    public void addMembers(List<DataModel> dataModels){
        createSelfObject();
        if(dataModels != null && dataModels.size() > 0){
            for(DataModel dataModel : dataModels){
                addMember(dataModel);
            }
        }
        displayFamilyMember();
    }

    public static int getAge(String dateofbirth) throws Exception {

        int age = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dob = Calendar.getInstance();
        dob.setTime(sdf.parse(dateofbirth));

        Calendar today = Calendar.getInstance();

        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);

        age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }

        return age;
    }

    private void displayFamilyMember(){
        chatFamilyMemberAdapter = new ChatFamilyMemberAdapter(context,patientList);
        rvFamillyMember.setAdapter(chatFamilyMemberAdapter);
        chatFamilyMemberAdapter.setItemClickListener(new OnAdapterItemClickListener() {
            @Override
            public void onItemClick(Object value) {
                PatientModel patientModel = (PatientModel) value;
                if(answerSelectionListner != null){
                    answerSelectionListner.onPatientSelected(patientModel);
                }
            }
        });
    }


    public class ChatFamilyMemberAdapter extends RecyclerView.Adapter<ChatFamilyMemberAdapter.MyViewHolder> {

        private List<PatientModel> memberList;
        private Context context;
        private boolean isMultiSelect;
        private OnAdapterItemClickListener itemClickListener;

        public ChatFamilyMemberAdapter(Context context, List<PatientModel> ansList){
            this.context = context;
            this.memberList = ansList ;
            this.isMultiSelect = isMultiSelect;

        }

        public void setItemClickListener(OnAdapterItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @NonNull
        @Override
        public ChatFamilyMemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_familly_member, parent, false);
            ChatFamilyMemberAdapter.MyViewHolder viewHolder = new ChatFamilyMemberAdapter.MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChatFamilyMemberAdapter.MyViewHolder holder, final int position) {
            final PatientModel patientModel = memberList.get(position);
            holder.position = position;
            holder.tvMemberName.setText(patientModel.displayName);
        }

        @Override
        public int getItemCount() {
            return memberList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView ivMember;
            TextView tvMemberName;
            int position;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivMember = (ImageView) itemView.findViewById(R.id.ivMember);
                tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);

                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemClickListener != null){
                            itemClickListener.onItemClick(memberList.get(position));
                        }
                    }
                });

            }
        }

    }


}
