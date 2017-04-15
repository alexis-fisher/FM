package server.services;

import java.sql.SQLException;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;

/**
 * Created by Alyx on 2/17/17.
 */

public class Event {
    /** Unique “authorization token” string for the user */
    public String authToken;

    /** Gets an event object based on @param eventID
     *
     * @param eventID The ID of the event object
     * @return the event object
     */
    public server.model.Event getEventByID(String eventID) throws DatabaseException {
        Database db = new Database();
        try {
            db.openTransaction();
            server.model.Event event = db.events.getEvent(eventID);
            db.closeTransaction(true);
            return event;
        } catch (DatabaseException e) {
            db.closeTransaction(false);
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Gets all events for the current user
     * @return an array of events based on the authtoken of the user
     */
    public server.model.Event[] getEventByUserName(String username){
        Database db = new Database();
        server.model.Event[] events = null;
        try{
            db.openTransaction();
            events = db.events.getAllEventsof(username);
            db.closeTransaction(true);
            return events;

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
