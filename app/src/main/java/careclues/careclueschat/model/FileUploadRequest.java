package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 7/27/2018.
 */

public class FileUploadRequest {

    @SerializedName("file ")
    @Expose
    public byte[] file;
}
