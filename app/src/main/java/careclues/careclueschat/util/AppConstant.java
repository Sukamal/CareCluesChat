package careclues.careclueschat.util;

import java.util.ArrayList;

import careclues.careclueschat.model.RelationshipModel;

/**
 * Created by SukamalD on 29-01-2018.
 */

public class AppConstant {

    public static final String DATABASE_NAME = "com.careClues.chat.db";
    public static final int DATABASE_VERSION = 1;
    public static final String PREFERENCE_NAME = "com.careClues.chat.pref";

//    public static final String USER_NAME = "sukamal-1035";
//    public static final String PASSWORD = "EskSolYUXYobtDhLPEvTwxd";
//    public static final String USER_NAME = "sachu-985";
//    public static final String PASSWORD = "XVQuexlHYvphcWYgtyLZLtf";
    public static final String USER_NAME = "dipa-1291";
    public static final String PASSWORD = "SYcgsbNRSUHaRJWbmhhFYzc";

    public static final String SOCKET_URL = "wss://ticklechat.careclues.com/websocket";
    public static long ADD_DISPLAY_THRESHOLD = 60000 * 1;

    public static String getUserId(){
        String[] user = AppConstant.USER_NAME.split("-");
        return  user[1];
    }

    public enum Preferences {
        IS_LOADED,
        LOOGED_IN_USER_ID,
        LAST_ROOM_UPDATED_ON
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
        //Add countries

        relationshipList.add(new RelationshipModel("great_grand_parent", "Great Grand Parent"));
        relationshipList.add(new RelationshipModel("mother", "Mother"));

        return relationshipList;

    }

}
