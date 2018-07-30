package careclues.careclueschat.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.addnewmember.AddMemberContract;
import careclues.careclueschat.feature.addnewmember.AddMemberPresenter;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;
import careclues.careclueschat.feature.chat.chatmodel.PatientModel;
import careclues.careclueschat.feature.common.OnAdapterItemClickListener;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.RelationshipModel;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppDialog;
import careclues.careclueschat.util.AppUtil;

/**
 * Created by SukamalD on 7/15/2018.
 */

public class FamilyMemberView extends RelativeLayout implements AddMemberContract.view{

    @BindView(R.id.rvFamillyMember)
    RecyclerView rvFamillyMember;
    @BindView(R.id.llNewMember)
    LinearLayout llNewMember;
    @BindView(R.id.ll_add_new_member)
    ConstraintLayout llAddNewMember;
    @BindView(R.id.ll_view_member)
    LinearLayout llViewMember;
    @BindView(R.id.relation_ship)
    MaterialBetterSpinner relationShip;
    @BindView(R.id.first_name)
    TextInputEditText firstName;
    @BindView(R.id.last_name)
    TextInputEditText lastName;
    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.mobile_number)
    TextInputEditText mobileNumber;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.blood_group)
    MaterialBetterSpinner bloodGroup;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.radioGroup)
    RadioGroup genderGroup;

    private LinearLayoutManager layoutManager;
    private Context context;
    private List<PatientModel> patientList;
    private ChatFamilyMemberAdapter chatFamilyMemberAdapter;
    private AnswerSelectionListner answerSelectionListner;

    private ArrayAdapter<RelationshipModel> relationshipAdapter;
    private List<RelationshipModel> relationshipModelList;
    private int relationShipIndex = -1;
    private RestApiExecuter apiExecuter;
    private Activity activity;
    private AppDialog appDialog;
    private AddMemberPresenter addMemberPresenter;
    private String roomId;



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


    public void setInitData(Activity activity,String roomId){
        this.activity = activity;
        this.roomId = roomId;
        appDialog = new AppDialog();
    }


    private void initView(Context context){
        this.context = context;
        View view = inflate(context, R.layout.view_family_member,this);
        ButterKnife.bind(this, view);
        initClickListner();
        initAnsRecycleView();
    }
    
    private void initClickListner(){
        llNewMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llViewMember.setVisibility(GONE);
                llAddNewMember.setVisibility(VISIBLE);
                initAddNewMember();

            }
        });

    }

    private void initAddNewMember(){
        addMemberPresenter = new AddMemberPresenter(this,roomId,activity.getApplication());
        initBloodGroup();
        initRelationship();
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    displayProgressBar();
                    AppUtil.hideSoftKeyBoard(activity);
                    addFamilyMember(view);
                }

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
        if(AppConstant.userProfile != null){
            patientModel.first_name = AppConstant.userProfile.data.firstName;
            patientModel.last_name = AppConstant.userProfile.data.lastName;
            patientModel.gender = AppConstant.userProfile.data.gender;
            patientModel.self = true;
            patientModel.lLink = AppConstant.userProfile.data.getLink("text_consultations");
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

    @Override
    public void displayToastMessage(String message) {

    }

    @Override
    public void displayProgressBar() {
        appDialog.showProgress(activity);

    }

    @Override
    public void hideProgressBar() {
        appDialog.dismissProgress();

    }

    @Override
    public void displayNewMember(DataModel familyMember) {
        llViewMember.setVisibility(VISIBLE);
        llAddNewMember.setVisibility(GONE);
        addMember(familyMember);
        hideProgressBar();
        chatFamilyMemberAdapter.notifyDataSetChanged();
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

    private void initBloodGroup(){
        List<String> bloodGroups = Arrays.asList(getResources().getStringArray(R.array.blood_groups));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, bloodGroups);
        findViewById(R.id.blood_group);
        bloodGroup.setAdapter(arrayAdapter);

    }

    private void initRelationship(){
        relationshipModelList = AppConstant.getRelationship();
        relationshipAdapter = new ArrayAdapter<RelationshipModel>(context,
                android.R.layout.simple_dropdown_item_1line, relationshipModelList);

        relationShip.setAdapter(relationshipAdapter);
        relationShip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                relationShipIndex = i;
            }
        });
    }

    private boolean validate() {

        boolean valid = true;

        if (relationShipIndex == -1) {
            relationShip.setError("Please select relation with you");
            valid = false;
        }

        if (AppUtil.isEmpty(firstName)) {
            valid = false;
        }

        if (AppUtil.isEmpty(lastName)) {
            valid = false;
        }

        if (!AppUtil.isValidMobileNumber(mobileNumber)) {
            valid = false;
        }

        if (!AppUtil.isEmpty(email)) {
            if (!AppUtil.isValidEmail(email)) {
                valid = false;
            }
        }

        return valid;

    }

    private String getGender() {
        int radioButtonID = genderGroup.getCheckedRadioButtonId();
        switch (radioButtonID) {
            case R.id.rd_male:
                return "male";
            case R.id.rd_female:
                return "female";
        }
        return "male";
    }

    public void addFamilyMember(View view) {
        DataModel familyMember = new DataModel();
        familyMember.firstName = firstName.getText().toString().trim();
        familyMember.lastName = lastName.getText().toString().trim();
        familyMember.email = email.getText().toString().trim();
        familyMember.mobileNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + mobileNumber.getText().toString().trim();
        familyMember.bloodGroup = bloodGroup.getText().toString().trim();
        familyMember.gender = getGender();
        familyMember.relationship = relationshipModelList.get(relationShipIndex).getKey();

        addMemberPresenter.addNewMember(familyMember);

    }




}
