package careclues.careclueschat.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import careclues.careclueschat.model.RoomMemberModel;
import careclues.careclueschat.model.RoomUserModel;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;

public class ModelEntityTypeConverter {

    public static RoomMemberEntity modelToEntity(RoomMemberModel data) {
        if (data == null) {
            return null;
        }
        RoomMemberEntity memberEntity = new RoomMemberEntity();
        memberEntity.Id = data.id;
        memberEntity.userName = data.userName;
        memberEntity.name = data.name;
        memberEntity.status = data.status;
        memberEntity.utcOffset = data.utcOffset;


        return memberEntity;
    }

    public static RoomMemberModel entutyToModel(RoomMemberEntity data) {
        if(data != null){

            RoomMemberModel model = new RoomMemberModel();
            model.id = data.Id ;
            model.userName = data.userName ;
            model.name = data.name ;
            model.status = data.status;
            model.utcOffset = data.utcOffset;
            return model;
        }else{
            return null;
        }

    }

}
