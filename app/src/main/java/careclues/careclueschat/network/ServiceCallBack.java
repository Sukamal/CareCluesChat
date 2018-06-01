package careclues.careclueschat.network;

import java.util.List;

public abstract class ServiceCallBack<T> {

    private final Class<T> classType;

    public ServiceCallBack(Class<T> classType){
        this.classType = classType;
    }

    public Class<T> getClassType(){
        return classType;
    }


    /** Called when response is received */
    public abstract void onSuccess(T response);

    /**A response from the server with a message*/
    public abstract void onFailure(List<NetworkError> errorList);

}
