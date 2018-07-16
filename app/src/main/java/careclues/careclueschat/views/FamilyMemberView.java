package careclues.careclueschat.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
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

    private void initView(Context context){
        this.context = context;
        inflate(context, R.layout.view_family_member,this);
        rvFamillyMember = (RecyclerView) findViewById(R.id.rvFamillyMember);
        initAnsRecycleView();
        createSelfObject();
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
            patientModel.lLink = dataModel.getLink("text_consultations");
            patientList.add(patientModel);
        }
    }

    public void addMembers(List<DataModel> dataModels){
        if(dataModels != null && dataModels.size() > 0){
            for(DataModel dataModel : dataModels){
                addMember(dataModel);
            }
        }
        displayFamilyMember();
    }

    private void displayFamilyMember(){
        chatFamilyMemberAdapter = new ChatFamilyMemberAdapter(context,patientList);
        rvFamillyMember.setAdapter(chatFamilyMemberAdapter);
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
