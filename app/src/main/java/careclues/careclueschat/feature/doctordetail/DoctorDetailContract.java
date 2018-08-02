package careclues.careclueschat.feature.doctordetail;

import java.io.File;

import careclues.careclueschat.feature.chat.chatmodel.ReplyMessageModel;
import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.AchievementResponse;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.FacilityResponse;
import careclues.careclueschat.model.LanguageResponse;
import careclues.careclueschat.model.QualificationResponse;
import careclues.careclueschat.model.ReviewResponse;
import careclues.careclueschat.model.ServiceResponse;
import careclues.careclueschat.model.SpecializationResponse;

public interface DoctorDetailContract {


    interface view extends CommonViewInterface {

        //public void displayNewMember();

        void showDoctorDetail();

        void showFacilities(FacilityResponse facilityResponse);

        void showReviewRatings(ReviewResponse reviewResponse);

        void showEducations(QualificationResponse qualificationResponse);

        void showServices(ServiceResponse serviceResponse);

        void showSpecializations(SpecializationResponse specializationResponse);

        void showAchievements(AchievementResponse achievementResponse);

        void showLanguages(LanguageResponse languageResponse);


    }

    interface presenter {

        public void getFacilities();

        public void getReviewRatings();

        public void getEducations();

        public void getServices();

        public void getSpecializations();

        public void getAchievements();

        public void getLanguges();

    }


}
