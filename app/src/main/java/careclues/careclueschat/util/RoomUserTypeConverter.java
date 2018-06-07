package careclues.careclueschat.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import careclues.careclueschat.model.BaseUserModel;
import careclues.careclueschat.model.RoomUserModel;

public class RoomUserTypeConverter {

    private static Gson gson;

    @TypeConverter
    public static List<BaseUserModel> stringToBaseUserModelList(String data) {
        gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<RoomUserModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String baseUserModelListToString(List<BaseUserModel> someObjects) {
        if(someObjects != null){
            gson = new Gson();
            return gson.toJson(someObjects);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static List<RoomUserModel> stringToRoomUserModelList(String data) {
        gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<RoomUserModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String roomUserModelListToString(List<RoomUserModel> someObjects) {
        if(someObjects != null){
            gson = new Gson();
            return gson.toJson(someObjects);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static RoomUserModel stringToRoomUserModel(String data) {
        gson = new Gson();
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<RoomUserModel>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String roomUserModelToString(RoomUserModel someObjects) {
        if(someObjects != null){
            gson = new Gson();
            return gson.toJson(someObjects);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }

}
