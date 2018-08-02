package careclues.careclueschat.feature.chat;

import java.util.List;

import careclues.careclueschat.feature.chat.chatmodel.ChatAnsModel;
import careclues.careclueschat.feature.chat.chatmodel.PatientModel;
import careclues.careclueschat.model.FeeRangeModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.SymptomModel;

public interface AnswerSelectionListner {
    public void onPatientSelected(PatientModel patientModel);
    public void onHealthTopicSelected(HealthTopicModel healthTopicModel);
    public void onFeeRangeSelect(FeeRangeModel feeRangeModel);
    public void onSymptomSelected(SymptomModel symptomModel);
    public void onOptionSelected(String option);
    public void onEndConsultationOptionSelected(String option);
    public void onSimpleTextSelected(String msg);
    public void onLanguageSelected(String option);
    public void onPayButtonClick();
    public void onAgeSelected(String option);
    public void onWriteAReview();
    public void onEndConsultation();
}

