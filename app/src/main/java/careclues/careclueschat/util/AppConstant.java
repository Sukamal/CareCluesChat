package careclues.careclueschat.util;

import java.util.ArrayList;

import careclues.careclueschat.feature.chat.chatmodel.ServerMessageModel;
import careclues.careclueschat.model.RelationshipModel;
import careclues.careclueschat.model.TextConsultantModel;
import careclues.careclueschat.model.TextConsultantResponseModel;
import careclues.careclueschat.model.UserProfileResponseModel;

/**
 * Created by SukamalD on 29-01-2018.
 */

public class AppConstant {

    public static final String CHAT_BASE_URL = "https://ticklechat.careclues.com/api/v1/";
    public static final String API_BASE_URL = "https://tickleapi.careclues.com/api/v1/";
    public static final String SOCKET_URL = "wss://ticklechat.careclues.com/websocket";
    public static final String DATABASE_NAME = "com.careClues.chat.db";
    public static final int DATABASE_VERSION = 1;
    public static final String PREFERENCE_NAME = "com.careClues.chat.pref";

    public static final String USER_NAME = "sukamal-1035";
    public static final String PASSWORD = "EskSolYUXYobtDhLPEvTwxd";

//    public static final String USER_NAME = "sachu-985";
//    public static final String PASSWORD = "XVQuexlHYvphcWYgtyLZLtf";
//    public static final String USER_NAME = "dipa-1291";
//    public static final String PASSWORD = "SYcgsbNRSUHaRJWbmhhFYzc";

    public static long ADD_DISPLAY_THRESHOLD = 60000 * 1;


    public static UserProfileResponseModel userProfile;
//    public static TextConsultantResponseModel textConsultantResponseModel;
    public static TextConsultantModel textConsultantModel;
    public static ServerMessageModel messageModel;
    public static boolean isReturnfromPayment = false;



    public static String getUserId(){
        String[] user = AppConstant.USER_NAME.split("-");
        return  user[1];
    }

    public enum Preferences {
        IS_LOADED,
        LOOGED_IN_USER_ID,
        LAST_ROOM_UPDATED_ON,
        IS_FIRST_TIME_LOAD
    }

    public enum ExtraTag {
        TAG_VALUE_1
    }

    public interface RequestTag {

        int PERMISSION_REQUEST_CODE_CAMERA_REQUEST = 101;
        int PERMISSION_REQUEST_CODE_STORAGE_REQUEST = 102;

        int PICK_GALARRY_REQUEST = 103;
        int PICK_CAMERA_REQUEST = 104;
        int PICK_DOCUMENT_REQUEST = 105;


    }

    public static ArrayList<RelationshipModel> getRelationship(){

        ArrayList<RelationshipModel> relationshipList = new ArrayList<>();

        relationshipList.add(new RelationshipModel("great_grand_parent", "Great Grand Parent"));
        relationshipList.add(new RelationshipModel("mother", "Mother"));

        return relationshipList;

    }

}
