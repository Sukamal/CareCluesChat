package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaModel {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("page_size")
    @Expose
    private String pageSize;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("page_no")
    @Expose
    private String pageNo;

}
