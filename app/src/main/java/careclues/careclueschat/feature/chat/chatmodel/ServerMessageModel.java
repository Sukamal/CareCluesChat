package careclues.careclueschat.feature.chat.chatmodel;

import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import careclues.careclueschat.model.SymptomModel;

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

}
