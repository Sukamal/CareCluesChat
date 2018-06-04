package careclues.careclueschat.model;

import careclues.careclueschat.network.RestApiExecuter;

/**
 * Created by SukamalD on 6/2/2018.
 */

public class AuthToken {

    private String userId;
    private String token;
    private static AuthToken authToken;

    private AuthToken(){

    }

    public static AuthToken getInstance(){
        if(authToken == null){
            synchronized (AuthToken.class){
                if(authToken == null){
                    authToken = new AuthToken();
                }
            }
        }
        return authToken;
    }

    public void saveToken(String userId,String token){
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
