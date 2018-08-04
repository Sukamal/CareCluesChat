package careclues.careclueschat.feature.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.chat.chatmodel.ServerMessageModel;
import careclues.careclueschat.model.PhysicianModel;
import careclues.careclueschat.model.PhysicianResponseModel;
import careclues.careclueschat.model.QualificationModel;
import careclues.careclueschat.model.SpecializationModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.AppUtil;
import careclues.careclueschat.util.DateFormatter;
import careclues.careclueschat.views.ChatImageView;

/**
 * Created by SukamalD on 6/15/2018.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> implements DateFormatter.Formatter {
    private List<ChatMessageModel> messageList;
    private Activity activity;
    private String userId;
    private DateFormatter.Formatter dateFormatter;
    private InputTypeListner inputTypeListner;
    private RecyclerView recyclerView;
    public int lastVisibleItem, totalItemCount;

    public interface InputTypeListner {
        public void onInputType(ServerMessageModel messageModel);
    }

    public void setInputTypeListner(InputTypeListner inputTypeListner) {
        this.inputTypeListner = inputTypeListner;
    }

    public ChatMessageAdapter(Activity activity, List<ChatMessageModel> messageList, String userId, RecyclerView recyclerView) {
        this.activity = activity;
        this.messageList = messageList;
        this.userId = userId;
        dateFormatter = this;
        this.recyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.e("Pos", String.valueOf(totalItemCount) + " / " + String.valueOf(lastVisibleItem));
                if (lastVisibleItem >= 5) {
                    Log.e("REACHED", "LOADMORE");
                }
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_chat, parent, false);
        ChatMessageAdapter.MyViewHolder viewHolder = new ChatMessageAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatMessageModel chatMessageModel = messageList.get(position);

        if(chatMessageModel != null){
            Date previousDate = null;
            if (position > 0) {
                previousDate = messageList.get(position - 1).createdAt;
            }
            setTimeTextVisibility(chatMessageModel.createdAt, previousDate, holder.tvDate);

            if (chatMessageModel.userId != null && chatMessageModel.userId.equalsIgnoreCase(userId)) {
                holder.cvLeft.setVisibility(View.GONE);
                holder.cvRight.setVisibility(View.VISIBLE);
                holder.cardViewDrCard.setVisibility(View.GONE);

                if (chatMessageModel.messageModel != null && chatMessageModel.messageModel.type != null && chatMessageModel.messageModel.type.equals("image")) {
                    holder.llRightMessage.setVisibility(View.GONE);
                    holder.civRightChatImage.setVisibility(View.VISIBLE);
                    displayImage(holder.civRightChatImage, chatMessageModel);
                }else{
                    holder.llRightMessage.setVisibility(View.VISIBLE);
                    holder.civRightChatImage.setVisibility(View.GONE);
                    holder.tvRightMessageTime.setText(DateFormatter.format(chatMessageModel.createdAt, "h:mm a"));
                    holder.tvRight.setText(chatMessageModel.messageModel.getContent(activity));
                }

            } else {
                if (chatMessageModel.messageModel != null && chatMessageModel.messageModel.type != null && chatMessageModel.messageModel.type.equals("physicianCard")) {
                    //TODO check if physician exists
                    displayPhysicianCard(holder, chatMessageModel);
                }  else {
                    holder.cvLeft.setVisibility(View.VISIBLE);
                    holder.cvRight.setVisibility(View.GONE);
                    holder.cardViewDrCard.setVisibility(View.GONE);
                    if (chatMessageModel.messageModel.type != null && chatMessageModel.messageModel.type.equals("image")) {
                        holder.llLeftMessage.setVisibility(View.GONE);
                        holder.civLeftChatImage.setVisibility(View.VISIBLE);
                        displayImage(holder.civLeftChatImage, chatMessageModel);
                    }else{
                        holder.llLeftMessage.setVisibility(View.VISIBLE);
                        holder.civLeftChatImage.setVisibility(View.GONE);

                        holder.tvLeftMessageTime.setText(DateFormatter.format(chatMessageModel.createdAt, "h:mm a"));
                        if (!chatMessageModel.userId.equalsIgnoreCase("JvMNPmN3xo4G7v2qi")) {
                            holder.tvHeading.setVisibility(View.GONE);
                        } else {
                            holder.tvHeading.setVisibility(View.VISIBLE);
                        }
                        holder.tvLeft.setText(chatMessageModel.messageModel.getContent(activity));
                    }
                }
            }

            if (position == (messageList.size() - 1)) {
                if (inputTypeListner != null) {
                    inputTypeListner.onInputType(chatMessageModel.messageModel);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "Today";
        } else if (DateFormatter.isYesterday(date)) {
            return "Yesterday";
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    public void addMessage(List<ChatMessageModel> dataList) {
        if (messageList != null) {
            messageList.addAll(dataList);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout cvLeft;
        TextView tvLeft;
        RelativeLayout cvRight;
        TextView tvRight;
        TextView tvDate;
        CardView cardViewDrCard;
        ImageView iv_dr_image;
        TextView tv_dr_name;
        TextView tv_dr_details1;
        TextView tv_dr_details2;
        TextView tv_dr_details3;
        TextView tv_fees;
        Button btn_view;
        TextView tvHeading;
        TextView tvLeftMessageTime;
        TextView tvRightMessageTime;
        RatingBar rating;
        LinearLayout llLeftMessage;
        LinearLayout llRightMessage;
        ChatImageView civLeftChatImage;
        ChatImageView civRightChatImage;


        public MyViewHolder(View itemView) {
            super(itemView);

            cvLeft = (RelativeLayout) itemView.findViewById(R.id.card_view_left);
            tvLeft = (TextView) itemView.findViewById(R.id.tv_left);
            cvRight = (RelativeLayout) itemView.findViewById(R.id.card_view_right);
            tvRight = (TextView) itemView.findViewById(R.id.tv_right);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            cardViewDrCard = (CardView) itemView.findViewById(R.id.cardView_dr_card);
            iv_dr_image = (ImageView) itemView.findViewById(R.id.iv_dr_image);
            tv_dr_name = (TextView) itemView.findViewById(R.id.tv_dr_name);
            tv_dr_details1 = (TextView) itemView.findViewById(R.id.tv_dr_details1);
            tv_dr_details2 = (TextView) itemView.findViewById(R.id.tv_dr_details2);
            tv_dr_details3 = (TextView) itemView.findViewById(R.id.tv_dr_details3);
            tv_fees = (TextView) itemView.findViewById(R.id.tv_fees);
            btn_view = (Button) itemView.findViewById(R.id.btn_view);
            tvHeading = (TextView) itemView.findViewById(R.id.tv_heading);
            tvLeftMessageTime = (TextView) itemView.findViewById(R.id.tv_left_time);
            tvRightMessageTime = (TextView) itemView.findViewById(R.id.tv_right_time);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            llLeftMessage = (LinearLayout) itemView.findViewById(R.id.llLeftMessage);
            llRightMessage = (LinearLayout) itemView.findViewById(R.id.llRightMessage);
            civLeftChatImage = (ChatImageView) itemView.findViewById(R.id.civLeftChatImage);
            civRightChatImage = (ChatImageView) itemView.findViewById(R.id.civRightChatImage);




        }
    }

    private void setTimeTextVisibility(Date date1, Date date2, TextView timeText) {

        if (date2 == null) {
            timeText.setVisibility(View.VISIBLE);
            if (dateFormatter != null) {
                timeText.setText(dateFormatter.format(date1));
            } else {
                timeText.setText(DateFormatter.format(date1, DateFormatter.Template.STRING_DAY_MONTH));

            }

        } else {
            boolean isSameDay = DateFormatter.isSameDay(date1, date2);
            if (isSameDay) {
                timeText.setVisibility(View.GONE);
                timeText.setText("");
            } else {
                timeText.setVisibility(View.VISIBLE);
                if (dateFormatter != null) {
                    timeText.setText(dateFormatter.format(date1));
                } else {
                    timeText.setText(DateFormatter.format(date1, DateFormatter.Template.STRING_DAY_MONTH));
                }
            }
        }
    }

    private boolean isPreviousSameDate(int position, Date dateToCompare) {
        if (messageList.size() <= position) return false;
        Date previousPositionDate = ((ChatMessageModel) messageList.get(position)).createdAt;
        return DateFormatter.isSameDay(dateToCompare, previousPositionDate);
    }

    private void displayImage(View view, ChatMessageModel chatMessageModel){
        ((ChatImageView)view).setImage(activity,chatMessageModel.id, chatMessageModel.image_url);
    }

    private void displayPhysicianCard(MyViewHolder holder, ChatMessageModel chatMessageModel) {

        holder.cvLeft.setVisibility(View.GONE);
        holder.cvRight.setVisibility(View.GONE);
        holder.cardViewDrCard.setVisibility(View.VISIBLE);

        PhysicianModel physician = AppConstant.textConsultantModel.physician;

        if(physician != null){
            holder.tv_dr_name.setText((CharSequence) physician.getFullName());
            holder.tv_fees.setText(AppUtil.getFormattedFee(AppConstant.textConsultantModel.grossAmountWithoutDiscount));
            holder.tv_fees.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            Picasso.with(activity)
                    .load(physician.getLink("profile_photo"))
                    .into(holder.iv_dr_image);

            holder.rating.setRating(Float.parseFloat(physician.rating));

            StringBuffer qualification = new StringBuffer();
            for (QualificationModel qualificationModel : physician.qualifications) {
                qualification.append(qualificationModel.degree);
                if (qualificationModel.specialty != null && qualificationModel.specialty.length() > 0) {
                    qualification.append(" (");
                    qualification.append(qualificationModel.specialty);
                    qualification.append("), ");
                }
            }

            StringBuffer speciality = new StringBuffer();
            // ArrayList<String> specialtyList =new ArrayList<String>();
            for (SpecializationModel specializationModel : physician.specializations) {
                speciality.append(specializationModel.subspecialty).append(", ");
                // specialtyList.add(specializationModel.subspecialty);
            }

            holder.tv_dr_details1.setText(String.format(activity.getResources().getString(R.string.years_of_experience),physician.yearsOfExperience));
            holder.tv_dr_details2.setText(qualification.deleteCharAt(qualification.length() - 2).toString());
            holder.tv_dr_details3.setText(speciality.deleteCharAt(speciality.length() - 2).toString());

        }

    }

}
