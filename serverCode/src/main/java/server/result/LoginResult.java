package server.result;

/**
 * Created by Alyx on 2/17/17.
 */

public class LoginResult {
    /** Unique “authorization token” string for the user */
    private String authToken;

    /** Unique user name (non-empty string) */
    private String userName;

    /** Unique Person ID assigned to this user’s generated Person object */
    private String personID;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    /** Creates a new LoginResult object */
    public LoginResult(){
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
