package server.services;


import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.dataAccess.EventDao;
import server.dataAccess.PersonDao;
import server.dataAccess.UserDao;
import server.model.*;


/**
 * Created by Alyx on 2/17/17.
 */

public class Load {
    Database db = new Database();

    public User[] user;
    public server.model.Event[] events;
    public server.model.Person[] persons;


    /**
     *  Loads arrays of users, persons, and events into the database
     * @param users array of user objects
     * @param persons array of persons object
     * @param events array of event objects
     * @return a String with a success or failure message
     * @throws DatabaseException
     */
    public String loadAll(User[] users, server.model.Person[] persons, server.model.Event[] events) throws DatabaseException {
        try {
            db.openTransaction();

            if (users != null) {
                // load users
                for (int i = 0; i < users.length; i++) {
                    loadUser(users[i]);
                }
            }
            if (persons != null) {
                // load persons
                for (int i = 0; i < persons.length; i++) {
                    loadPersons(persons[i]);
                }
            }
            if (events != null) {
                // load events
                for (int i = 0; i < events.length; i++) {
                    loadEvent(events[i]);
                }
            }
            db.closeTransaction(true);
        } catch (DatabaseException e){
            db.closeTransaction(false);
            throw new DatabaseException(e.getMessage());
        }
        String message = "Successfully added " + users.length + " users, " +
                          persons.length + " persons, and " + events.length +
                          " events";
        return message;
    }

    /** Creates a user and add it to to the database
     *
     * @param u the user created
     */
    public void loadUser(User u) throws DatabaseException {
        try {
            db.users.createUser(u.getUserName(), u.getPassword(), u.getEmail(),
                               u.getFirstName(), u.getLastName(), u.getGender(), u.getPersonID());
        } catch (DatabaseException e){
            throw new DatabaseException("Couldn't load user");
        }
    }

    /** Creates family information (a Person) for the Users
     *
     * @param p the person created
     */
    public void loadPersons(server.model.Person p) throws DatabaseException {
        try {
            db.persons.createPerson(p.getPersonID(),p.getDescendant(),p.getFirstName(),p.getLastName(),
                                   p.getGender(),p.getFather(),p.getMother(),p.getSpouse());
        } catch (DatabaseException e){
            throw new DatabaseException("Couldn't load person");
        }
    }

    /** Creates family information (an Event) for the Users
     *
     * @param e the event created
     */
    public void loadEvent(server.model.Event e) throws DatabaseException {
        try {
            db.events.createEvent(e.getEventID(),e.getDescendant(),e.getPerson(),e.getLatitude(),
                                 e.getLongitude(),e.getCountry(),e.getCity(),e.getEventType(),
                                 e.getYear());
        } catch (DatabaseException m){
            throw new DatabaseException("Couldn't load event");
        }
    }

}
