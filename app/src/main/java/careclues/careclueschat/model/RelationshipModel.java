package careclues.careclueschat.model;

public class RelationshipModel {

    private String key;
    private String Value;

    public RelationshipModel(String key, String value) {
        this.key = key;
        Value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return Value;
    }

}
