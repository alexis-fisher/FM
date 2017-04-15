package server.result;

import server.request.RegisterRequest;
import server.services.Register;

/**
 * Created by Alyx on 2/17/17.
 */

public class RegisterResult {
    /** Unique “authorization token” string for the user */
    private String authToken;

    /** Unique user name (non-empty string) */
    private String userName;

    /** Non-empty string containing the Person ID of the user’s generated Person object */
    private String personID;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    /** Creates a new RegisterResult object */
    public RegisterResult(){
        authToken = "";
        userName = "";
        personID = "";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
