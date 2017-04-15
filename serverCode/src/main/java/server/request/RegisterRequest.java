package server.request;

/**
 * Created by Alyx on 2/17/17.
 */

public class RegisterRequest {

    /**  Unique user name (non-empty string) */
    private String userName;

    /** User’s password (non-empty string) */
    private String password;

    /** User’s email address (non-empty string) */
    private String email;

    /** User’s first name (non-empty string) */
    private String firstName;

    /** User’s last name (non-empty string) */
    private String lastName;

    /** User’s gender (Male or Female) */
    private String gender;


    /** Creates new RegisterRequest object
     *
     * @param userName Unique user name (non-empty string)
     * @param password User’s password (non-empty string)
     * @param email User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName User’s last name (non-empty string)
     * @param gender User’s gender (Male or Female)
     */
     public RegisterRequest(String userName, String password,
                           String email, String firstName,
                           String lastName, String gender){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public RegisterRequest(){
        this.userName = "";
        this.password = "";
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
