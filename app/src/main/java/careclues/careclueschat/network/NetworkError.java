package careclues.careclueschat.network;

public class NetworkError {

    protected String errorCode;
    protected String errorMessage;

    public String getErrorCode() {
        return this.errorCode;
    }
    public void setErrorCode(String value) {
        this.errorCode = value;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

}
