package server.model;

import java.util.UUID;

/**
 * Created by Alyx on 2/17/17.
 */

public class AuthToken {
    /** Name of the user the authToken belongs to */
    private String userName;

    /** The actual authorization token */
    private String authToken;

    private String personID;

    /** Creates an authorization token object
     *
     */
    public AuthToken(){
        this.userName = "";
        this.authToken = "";
        this.personID = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public String getPersonID(){
        return personID;
    }

    public String getToken() {
        return authToken;
    }
    public void setToken(String authToken) { this.authToken = authToken; }

}
