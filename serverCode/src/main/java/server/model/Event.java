package server.model;

/**
 * Created by Alyx on 2/17/17.
 */

public class Event {
    /** Unique identifier for this event */
    private String eventID;

    /** User to which this person belongs */
    private String descendant;

    /** Person to which this event belongs */
    private String person;

    /** Latitude of event’s location */
    private String latitude;

    /** Longitude of event’s location */
    private String longitude;

    /** Country in which event occurred */
    private String country;

    /** City in which event occurred */
    private String city;

    /** Type of event (birth, baptism, christening, marriage, death, etc.) */
    private String eventType;

    /** Year in which event occurred */
    private String year;

    /** Creates a new Event object */
    public Event(){
        eventID = "";
        descendant = "";
        person = "";
        latitude = "";
        longitude = "";
        country = "";
        city = "";
        eventType = "";
        year = "";
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() { return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() { return year; }

    public void setYear(String year) {
        this.year = year;
    }
}
