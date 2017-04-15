package server.services;

import java.util.UUID;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.model.AuthToken;
import server.model.User;
import server.request.LoginRequest;
import server.result.LoginResult;

/**
 * Created by Alyx on 2/17/17.
 */

public class Login {

    private String authToken;
    private String userName;
    private String personID;

    public Login(){
        authToken = "";
        userName = "";
        personID = "";
    }

    /** Logs a user in with the provided username and password and generates an auth token
     *
     * @param userName name of the User to authenticate
     * @param password password of the User to authenticate
     * @throws DatabaseException if the User doesn't exist, or if the username-password combo isn't recognized
     */
    public void login(String userName, String password) throws DatabaseException{
        Database db = new Database();
        User u = new User();
        try {
            db.openTransaction();
            u = db.users.getUserFromName(userName);
            db.closeTransaction(true);

        } catch (DatabaseException e){
            throw new DatabaseException(e.getMessage());
        }
        if(!u.getUserName().equals("")) {
            if (u.getPassword().equals(password)) {

                // authenticate (for the JSON, since we return this object)
                this.userName = u.getUserName();
                this.personID = u.getPersonID();
                this.authToken = UUID.randomUUID().toString();

                // authenticate (in the actual database)
                try {
                    db.openTransaction();
                    db.tokens.createToken(this.authToken, this.userName, this.personID);
                    db.closeTransaction(true);
                } catch (DatabaseException e){
                    throw new DatabaseException(e.getMessage());
                }
            } else {
                throw new DatabaseException("Username & password don't match!");
            }
        } else {
            throw new DatabaseException("No user with that name!");
        }
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
