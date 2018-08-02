package careclues.careclueschat.feature.chat.chatmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import careclues.careclueschat.R;
import careclues.careclueschat.model.SymptomModel;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.DateFormatter;

public class ServerMessageModel {

    @SerializedName("id")
    public String id;
    @SerializedName("content")
    public String content;
    @SerializedName("type")
    public String type;
    @SerializedName("control")
    public String control;
    @SerializedName("patient")
    public PatientModel patientModel;
    @SerializedName("category")
    public CategoryModel categoryModel;
    @SerializedName("symptom")
    public SymptomModel symptomModel;
    @SerializedName("options")
    public List<String> options;
    @SerializedName("health_topic")
    public CategoryModel health_topic;
    @SerializedName("text_consultation_link")
    public String textConsultationLink;
    @SerializedName("physician_link")
    public String physicianLink;
    @SerializedName("fee")
    public double fee;
    @SerializedName("physician_name")
    public String physicianName;
    @SerializedName("date")
    public Date date;

    @Override
    public String toString() {
        return "ServerMessageModel{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", control='" + control + '\'' +
                ", patientModel=" + patientModel +
                ", categoryModel=" + categoryModel +
                ", symptomModel=" + symptomModel +
                ", options=" + options +
                ", health_topic=" + health_topic +
                ", textConsultationLink='" + textConsultationLink + '\'' +
                ", physicianLink='" + physicianLink + '\'' +
                ", fee=" + fee +
                ", physicianName='" + physicianName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public StringBuilder getContent(Context context) {

        StringBuilder sb = new StringBuilder("");

        if(AppConstant.textConsultantModel!=null && AppConstant.textConsultantModel.physician != null){
            physicianName = AppConstant.textConsultantModel.physician.getFullName();
        }

        if (id != null && id.equals("paymentSuccess")) {
            sb.append(String.format(context.getResources().getString(R.string.tc_payment_success), physicianName));
        } else if (type.equals("suggest_followup")) {
            sb.append(String.format(context.getResources().getString(R.string.tc_suggest_followup), DateFormatter.format(date, "EEE, d MMM")));
            if (content != "") {
                sb.append("\n" + content);
            }
        } else if (type != null && type.equals("unconfirmed_expiry")) {
            sb.append(context.getResources().getString(R.string.tc_msg_inactivity_expired));
        } else if (type != null && type.equals("unpaid_expiry")) {
            sb.append(context.getResources().getString(R.string.tc_msg_payment_expired));
        } else if (type != null && type.equals("completed")) {
            sb.append(String.format(context.getResources().getString(R.string.tc_msg_completed), physicianName));
        } else if (type != null && type.equals("completed_by_physician")) {
            sb.append(String.format(context.getResources().getString(R.string.completed_by_physician), physicianName, physicianName));
        } else if (type != null && type.equals("rejected_by_physician")) {
            sb.append(context.getResources().getString(R.string.rejected_by_physician));
        } else if (content != null) {
            sb.append(content);
//            if (typeof parsedJSONContent.content == = 'string'){
//                formattedContent = parsedJSONContent.content.replace( / < br\/>/g, '\n');
//            } else{
//                formattedContent = parsedJSONContent.content;
//            }
        }

        return sb;
    }

}
