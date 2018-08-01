package careclues.careclueschat.feature.chat.chatmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.SymptomModel;

public class ReplyMessageModel {
    @SerializedName("replyToQuestionId")
    @Expose
    public String replyToQuestionId;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("patient")
    @Expose
    public PatientModel patient;
    @SerializedName("category")
    @Expose
    public CategoryModel categoryModel;
    @SerializedName("symptom")
    @Expose
    public SymptomModel symptomModel;

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("imageURL")
    @Expose
    public String imageURL;


}
