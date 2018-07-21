package careclues.careclueschat.feature.addnewmember;


import java.io.File;
import java.util.List;

import careclues.careclueschat.feature.chat.chatmodel.ChatMessageModel;
import careclues.careclueschat.feature.common.CommonViewInterface;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.HealthTopicModel;
import careclues.careclueschat.model.SymptomModel;

public interface AddMemberContract {

    interface view extends CommonViewInterface {

        public void displayNewMember(DataModel familyMember);
    }

    interface presenter {
        public void addNewMember(DataModel familyMember);
    }
}
