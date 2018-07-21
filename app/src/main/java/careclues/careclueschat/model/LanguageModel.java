package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageModel extends ServerResponseBaseModel{
    @SerializedName("name")
    @Expose
    public String name;
}
