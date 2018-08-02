package careclues.careclueschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class QualificationModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("degree")
    @Expose
    public String degree;
    @SerializedName("specialty")
    @Expose
    public String specialty;
    @SerializedName("college")
    @Expose
    public String college;
    @SerializedName("year_of_passing")
    @Expose
    public Integer yearOfPassing;
    @SerializedName("additional_information")
    @Expose
    public String additionalInformation;
    @SerializedName("links")
    @Expose
    public List<LinkModel> links = null;
//    private Integer id;
//    @SerializedName("degree")
//    @Expose
//    private String degree;
//    @SerializedName("specialty")
//    @Expose
//    private String specialty;
//    @SerializedName("college")
//    @Expose
//    private String college;
//    @SerializedName("year_of_passing")
//    @Expose
//    private Integer yearOfPassing;
//    @SerializedName("additional_information")
//    @Expose
//    private String additionalInformation;
//    @SerializedName("links")
//    @Expose
//    private List<LinkModel> links = null;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getDegree() {
//        return degree;
//    }
//
//    public void setDegree(String degree) {
//        this.degree = degree;
//    }
//
//    public String getSpecialty() {
//        return specialty;
//    }
//
//    public void setSpecialty(String specialty) {
//        this.specialty = specialty;
//    }
//
//    public String getCollege() {
//        return college;
//    }
//
//    public void setCollege(String college) {
//        this.college = college;
//    }
//
//    public Integer getYearOfPassing() {
//        return yearOfPassing;
//    }
//
//    public void setYearOfPassing(Integer yearOfPassing) {
//        this.yearOfPassing = yearOfPassing;
//    }
//
//    public String getAdditionalInformation() {
//        return additionalInformation;
//    }
//
//    public void setAdditionalInformation(String additionalInformation) {
//        this.additionalInformation = additionalInformation;
//    }
//
//    public List<LinkModel> getLinks() {
//        return links;
//    }
//
//    public void setLinks(List<LinkModel> links) {
//        this.links = links;
//    }
//
//    @Override
//    public String toString() {
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append(getDegree());
//        if (getSpecialty() != null) {
//            stringBuffer.append(" (" + getSpecialty() + ")");
//        }
//
//        return stringBuffer.toString();
//    }
}
