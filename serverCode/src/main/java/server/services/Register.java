package server.services;

import java.util.UUID;

import javax.jws.soap.SOAPBinding;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.model.User;
import server.request.RegisterRequest;
import server.result.RegisterResult;

/**
 * Created by Alyx on 2/17/17.
 */

public class Register {
    public User u = new User();
    public Database db = new Database();

    /** Holds 4 generations of Person objects in an array */
    private Person[] ancestors;

    /**
     * Adds user to the database
     * @param userName unique username of the User (string)
     * @param password password of User (string)
     * @param email email of User (string)
     * @param firstName firstName of User (string)
     * @param lastName lastName of User (string)
     * @param gender male or female (m/f) (string)
     * @return Success or fail message (string)
     * @throws DatabaseException
     */
    public String addUser(String userName, String password, String email,
                          String firstName, String lastName, String gender) throws DatabaseException {
        // get fields, and set them in a new User object
        u.setUsername(userName);
        u.setPassword(password);
        u.setEmail(email);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setGender(gender);
        u.setPersonID(UUID.randomUUID().toString());

        try {
            // check for duplicate, if duplicate, throw exception
            db.openTransaction();
            User example = db.users.getUserFromName(userName);
            if(example != null && example.getUserName().equals("")){
                // if no duplicate, put them in the database
                db.users.createUser(u.getUserName(), u.getPassword(), u.getEmail(),
                        u.getFirstName(), u.getLastName(), u.getGender(), u.getPersonID());
                db.closeTransaction(true);
                return "Registration complete!";

            }
            else{
                db.closeTransaction(true);
                throw new DatabaseException("That user is already registered!");
            }
        } catch (DatabaseException e){
            throw new DatabaseException(e.getMessage());
        }
    }

}
