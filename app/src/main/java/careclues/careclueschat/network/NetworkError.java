package careclues.careclueschat.network;

public class NetworkError {

    protected String errorCode;
    protected String errorMessage;

    public NetworkError(String errorCode,String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
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
