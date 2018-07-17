package careclues.careclueschat.feature.chat;

import java.util.List;

import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.chat.chatmodel.PatientModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.SymptomModel;

public interface AnswerSelectionListner {
    public void onPatientSelected(PatientModel patientModel);
    public void onHealthTopicSelected(HealthTopicModel healthTopicModel);
    public void onSymptomSelected(SymptomModel symptomModel);
}

