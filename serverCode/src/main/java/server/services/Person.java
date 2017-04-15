package server.services;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.dataAccess.PersonDao;

/**
 * Created by Alyx on 2/17/17.
 */

public class Person {
    /** Unique “authorization token” string for the user */
    public String authToken;

    /**
     * Returns person from a given personID
     * @param personID unique personID corresponding to a person in the database
     * @return Person object (corresponding w/ personID)
     */
    public server.model.Person getPersonFromID(String personID){
        Database db = new Database();
        try {
            db.openTransaction();
            PersonDao pd = new PersonDao(db);
            server.model.Person p = pd.getPersonFromID(personID);
            db.closeTransaction(true);
            return p;
        } catch (DatabaseException e){
            System.out.println("Couldn't open connection when getting person from ID");
        }
        return null;
    }

    /**
     * From a username, returns all persons related to User
     * @param username Unique username of user
     * @return array of Person objects related to user
     */
    public server.model.Person[] getPersonsByUserName(String username){
        Database db = new Database();
        try{
            db.openTransaction();
            server.model.Person[] persons = db.persons.getAllPersonsof(username);
            db.closeTransaction(true);
            return persons;

        } catch (DatabaseException e){
            try {
                db.closeTransaction(false);
            } catch (DatabaseException m){
                System.out.println("Couldn't close transaction!");
            }

        }
        return null;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
