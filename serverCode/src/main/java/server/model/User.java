package server.model;

/**
 * Created by Alyx on 2/17/17.
 */

public class User {
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

    /** Unique Person ID assigned to this user’s generated Person object */
    private String personID;

    /** Initializes all variables and creates a User object */
    public User(){
        userName = "";
        password = "";
        email = "";
        firstName = "";
        lastName = "";
        gender = "";
        personID = "";
    }

    /** Initializes all variables and creates a User object */
    public User(String userName, String password, String email, String firstName, String lastName,
                String gender, String personID){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

}
