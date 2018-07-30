package careclues.careclueschat.feature.addnewmember;

import android.app.Application;
import android.util.Log;

import java.util.List;

import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.model.DataModel;
import careclues.careclueschat.model.UserProfileResponseModel;
import careclues.careclueschat.network.NetworkError;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.network.ServiceCallBack;
import careclues.careclueschat.util.AppConstant;

/**
 * Created by SukamalD on 7/21/2018.
 */

public class AddMemberPresenter implements AddMemberContract.presenter{

    private  AddMemberContract.view view;
    private Application application;
    private String roomId;
    private RestApiExecuter apiExecuter;
    private UserProfileResponseModel userProfileModel;


    public AddMemberPresenter(AddMemberContract.view view,String roomId, Application application){
        this.view = view;
        this.roomId = roomId;
        this.application = application;
        if(AppConstant.userProfile != null){
            userProfileModel = AppConstant.userProfile;
        }
    }

    @Override
    public void addNewMember(DataModel familyMember) {
        addFamilyMember(familyMember);

    }

    private void addFamilyMember(DataModel familyMember) {
        if (userProfileModel != null) {
            String url = userProfileModel.data.getLink("dependants");
            if (apiExecuter == null)
                apiExecuter = RestApiExecuter.getInstance();

            apiExecuter.addFamilyMember(url, familyMember, new ServiceCallBack<DataModel>(DataModel.class) {
                @Override
                public void onSuccess(DataModel response) {
                    view.displayNewMember(response);
                }

                @Override
                public void onFailure(List<NetworkError> errorList) {

                }
            });
        }
    }

}
