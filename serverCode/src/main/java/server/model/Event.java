package server.model;

/**
 * Created by Alyx on 2/17/17.
 */

public class Event implements Comparable, Searchable {
    /** Unique identifier for this event */
    private String eventID;

    /** User to which this personID belongs */
    private String descendant;

    /** Person to which this event belongs */
    private String personID;

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
        personID = "";
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
        return personID;
    }

    public void setPerson(String personID) {
        this.personID = personID;
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

    /**
     * Compares events to each other. Criteria: Ordered chronologically, then by the first letter,
     * then the event being compared (the param) is higher if it gets to that point.
     * @param o Event to compare
     * @return 1 if param is greater, -1 if param is lesser, 0 if equal or unable to compare.
     */
    @Override
    public int compareTo(Object o) {
        if(o == null){
            return 0;
        }
        if(o.getClass() != Event.class){
            return 0;
        }
        if(o == this){
            return 0;
        }
        Event ev = (Event) o;
        if(Integer.parseInt(ev.getYear()) > Integer.parseInt(this.getYear())){
            return 1;
        } else if(Integer.parseInt(ev.getYear()) < Integer.parseInt(this.getYear())){
            return -1;
        } else {
            if(ev.getEventType().equals("")){
                ev.setEventType("birth");
            }
            if(ev.getEventType().toLowerCase().charAt(0) > this.getEventType().toLowerCase().charAt(0)){
                return 1;
            } else if (ev.getEventType().toLowerCase().charAt(0) < this.getEventType().toLowerCase().charAt(0)){
                return -1;
            } else { // close enough to alphabetic order for this... ha.
                return 1;
            }
        }
    }

    @Override
    public boolean contains(String term) {
        // check event type
        if(this.getEventType().toLowerCase().contains(term.toLowerCase())){
            return true;
        }

        // check year
        if(this.getYear().contains(term)){
            return true;
        }

        // check city
        if(this.getCity().toLowerCase().contains(term.toLowerCase())){
            return true;
        }

        // check country
        if(this.getCountry().toLowerCase().contains(term.toLowerCase())){
            return true;
        }
        return false;
    }
}
