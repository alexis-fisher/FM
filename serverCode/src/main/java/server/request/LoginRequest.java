package server.request;


/**
 * Created by Alyx on 2/17/17.
 */

public class LoginRequest {
    /** Unique user name (non-empty string) */
    private String userName;

    /** User’s password (non-empty string) */
    private String password;

    /** Creates a new LoginRequest object */
    public LoginRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public LoginRequest(){
        this.userName = "";
        this.password = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
