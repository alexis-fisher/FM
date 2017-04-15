package server.dataAccess;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import server.model.Event;
import server.model.Person;
import server.model.User;


/**
 * Created by Alyx on 2/17/17.
 */

public class EventDao {

    Database db;

    public EventDao(Database db){
        this.db = db;
    }

    /** Creates an event in the database with given data
     *
     * @param eventID Unique identifier for this event
     * @param descendantID UserID to which this person belongs
     * @param personID PersonID to which this event belongs
     * @param latitude  Latitude of event’s location
     * @param longitude Longitude of event’s location
     * @param country Country in which event occurred
     * @param city  City in which event occurred
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year Year in which event occurred
     */
    public void createEvent(String eventID, String descendantID,
                            String personID,
                            String latitude, String longitude,
                            String country, String city,
                            String eventType, String year) throws DatabaseException {
        if(eventID == null) {
            eventID = UUID.randomUUID().toString();
        }

        try {
            PreparedStatement stmt = null;
            try {

                String statement = "insert into events (descendant, eventID, personID, latitude, " +
                        "longitude, country, city, eventType, year) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

                stmt = db.conn.prepareStatement(statement);

                stmt.setString(1, descendantID);
                stmt.setString(2, eventID);
                stmt.setString(3, personID);
                stmt.setString(4, latitude);
                stmt.setString(5, longitude);
                stmt.setString(6, country);
                stmt.setString(7, city);
                stmt.setString(8, eventType);
                stmt.setString(9, year);

                int result = stmt.executeUpdate();
                if (result != 1) {
                    throw new DatabaseException("CreateEvent failed: Could not insert event");
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("CreateEvent failed", e);
        }
        // THIS IS THE SQL STATEMENT TO CREATE AN EVENT
    }

    /** Retrieves all events in database
     *
     * @return collection of events
     */
    public Event[] read() throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select * from events";
                stmt = db.conn.prepareStatement(sql);

                Set<Event> events = new HashSet<>();
                rs = stmt.executeQuery();
                int index = 0;
                while (rs.next()) {
                    Event ev = new Event();
                    ev.setDescendant(rs.getString(1));
                    ev.setEventID(rs.getString(2));
                    ev.setPerson(rs.getString(3));
                    ev.setLatitude(rs.getString(4));
                    ev.setLongitude(rs.getString(5));
                    ev.setCountry(rs.getString(6));
                    ev.setCity(rs.getString(7));
                    ev.setEventType(rs.getString(8));
                    ev.setYear(rs.getString(9));
                    events.add(ev);
                    index++;
                }
                Event[] eventsInDatabase = new Event[index];
                index = 0;
                for(Event e : events){
                    eventsInDatabase[index] = e;
                    index++;
                }

                return eventsInDatabase;
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("readEvents failed", e);
        }
    }

    public Event getEvent(String eventID) throws DatabaseException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String getEv = "select * from events where eventID = ?";
        try {
            statement = db.conn.prepareStatement(getEv);
            statement.setString(1, eventID);
            rs = statement.executeQuery();

            Event ev = new Event();

            while (rs.next()) {
                ev.setDescendant(rs.getString(1));
                ev.setEventID(rs.getString(2));
                ev.setPerson(rs.getString(3));
                ev.setLatitude(rs.getString(4));
                ev.setLongitude(rs.getString(5));
                ev.setCountry(rs.getString(6));
                ev.setCity(rs.getString(7));
                ev.setEventType(rs.getString(8));
                ev.setYear(rs.getString(9));
            }
            return ev;

        } catch (SQLException e) {
            throw new DatabaseException("Couldn't get event");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement");
            }
        }
    }

    public Event[] getAllEventsof(String userName) throws DatabaseException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String getEv = "select * from events where descendant = ?";
        try {
            statement = db.conn.prepareStatement(getEv);
            statement.setString(1, userName);
            rs = statement.executeQuery();


            Set<Event> events = new HashSet<>();
            int index = 0;
            while (rs.next()) {
                Event ev = new Event();
                ev.setDescendant(rs.getString(1));
                ev.setEventID(rs.getString(2));
                ev.setPerson(rs.getString(3));
                ev.setLatitude(rs.getString(4));
                ev.setLongitude(rs.getString(5));
                ev.setCountry(rs.getString(6));
                ev.setCity(rs.getString(7));
                ev.setEventType(rs.getString(8));
                ev.setYear(rs.getString(9));
                events.add(ev);
                index++;
            }
            Event[] eventsInDatabase = new Event[index];
            index = 0;
            for(Event e : events){
                eventsInDatabase[index] = e;
                index++;
            }

            return eventsInDatabase;

        } catch (SQLException e) {
            throw new DatabaseException("Couldn't find event by user!");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement!");
            }
        }
    }

    /** Removes an event from the database
     *
     * @param eventID ID of event to be removed
     */
    public void deleteEvent(String eventID) throws DatabaseException {
        PreparedStatement statement = null;
        String deleteEvent = "delete from events where eventID = ?";
        try {
            statement = db.conn.prepareStatement(deleteEvent);
            statement.setString(1, eventID);
            statement.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Couldn't delete event");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE AN EVENT

    }

    /** Removes a events from the database by given userName
     *
     * @param userName of events to be removed
     */
    public void deleteEventsOfUser(String userName) throws DatabaseException {
        PreparedStatement statement = null;
        String deleteEvent = "delete from events where descendant = ?";
        try {
            statement = db.conn.prepareStatement(deleteEvent);
            statement.setString(1, userName);
            statement.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Couldn't delete events of user");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE AN EVENT

    }


    /** Removes all events from the database
     *
     */
    public void deleteAllEvents() throws DatabaseException {
        String deleteEvents = "delete from events";
        PreparedStatement statement = null;
        try {
            statement = db.conn.prepareStatement(deleteEvents);
            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't delete all events");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't execute finally block in delete all events");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE ALL EVENTS

    }
}
