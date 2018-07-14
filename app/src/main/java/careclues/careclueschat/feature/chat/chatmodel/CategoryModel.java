package careclues.careclueschat.feature.chat.chatmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SukamalD on 7/14/2018.
 */

public class CategoryModel {

    @SerializedName("name")
    public String name;
    @SerializedName("link")
    public String link;
    @SerializedName("alternate_medicine")
    public Boolean alternate_medicine;
}
