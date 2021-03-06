package com.example.alyx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import server.model.Person;
import server.model.Event;
import server.model.Searchable;
import server.proxy.ClientException;
import server.proxy.ServerProxy;
import server.result.EventResult;
import server.result.PersonResult;

/**
 * Created by Alyx on 3/27/17.
 */

public class Model {
    // Connection to the server
    private ServerProxy server = ServerProxy.server();

    // Working list of events (the ones shown)
    private Event[] eventsVisible;

    // Partitions of Events by side of family
    private Event[] mothersSideEvents;
    private Event[] fathersSideEvents;

    // Partitions of the People
    private Person[] fathersSide;
    private Person[] mothersSide;

    // Map of Events, PersonID is the key.
    private Map<String, Set<Event>> eventsByPerson= new HashMap<String, Set<Event>>();

    public Map<String, Set<Event>> getEventsByType() {
        return eventsByType;
    }

    public void setEventsByType(Map<String, Set<Event>> eventsByType) {
        this.eventsByType = eventsByType;
    }

    // Map of Events, EventType is the key.
    private Map<String, Set<Event>> eventsByType = new HashMap<String,Set<Event>>();

    public boolean isInflate() {
        return inflate;
    }

    public void setInflate(boolean inflate) {
        this.inflate = inflate;
    }

    // Inflate menu or not
    private boolean inflate = true;


    // All person and event data from the database
    private Person[] persons;
    private Event[] events;

    // Event that is selected on the maps screen
    private Event eventSelected;

    // Descendant ID to sort info in the database
    private String currentUser;

    // Private constructor to make this a singleton
    private Model(){}
    private static Model _instance;

    // Get an the instance of the class
    public static Model instanceOf(){
        if(_instance == null){
            _instance = new Model();
        }
        return _instance;
    }

    /**
     *  Pulls all people info from the server
     * @return true if succeeded, false if failed.
     * @throws ClientException if there was an internal server error.
     */
    public boolean getPeopleFromServer() throws ClientException {
        // Get people
        PersonResult peopleFromServer = this.server.person();
        // Check for error message
        if(peopleFromServer.getMessage() == null || peopleFromServer.getMessage().equals("")){
            // If no error, save the people!
            this.persons = peopleFromServer.data;
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Pull all event info from the server
     * @return true if succeeded, false if failed.
     * @throws ClientException if there was an internal server error.
     */
    public boolean getEventsFromServer() throws ClientException {
        // Get events
        EventResult eventsFromServer = this.server.event();

        // Check for error message
        if(eventsFromServer.getMessage() == null || eventsFromServer.getMessage().equals("")){
            // If no error, save the events!
            this.events = eventsFromServer.data;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Searches the Persons and Events for the given term
     * @param term Search term.
     * @return the list of Persons & Events containing the term.
     */
    public List<Searchable> search(String term){
        List<Searchable>searchResults = new ArrayList<>();

        // Look in persons array
        for (Person p : persons){
            // If person contains the term, add it to the matches list!
            if(p.contains(term)){
                searchResults.add(p);
            }
        }

        // Look in events array
        for (Event e : events){
            // If event contains the term, add it to the matches list!
            if(e.contains(term)){
                searchResults.add(e);
            }
        }
        return searchResults;
    }

    /**
     * Gets event associated with the given personID
     * @param personID a valid ID of a person with events.
     * @return null if invalid personID or no events associated, else the Set of events.
     */
    public Set<Event> getEventsOf(String personID){
        Set<Event> eventsOf = null;
        if(eventsByPerson != null && eventsByPerson.size() > 0){
            eventsOf = eventsByPerson.get(personID);
        }
        return eventsOf;
    }

    public void addToEventMap(String eventType, Event event){
        // Sort events by person for use by filter activity
        if (eventType != null) {
            if (eventsByType.containsKey(eventType)) {
                eventsByType.get(eventType).add(event);
            } else {
                eventsByType.put(eventType, new TreeSet<Event>());
                eventsByType.get(eventType).add(event);
            }
        }

    }

    public void sortEventsByPerson(){
        // Sort events by person for use by person activity
        if(events != null) {
            for (Event e : events) {
                // Get person associated with event
                String personID = e.getPerson();
                if (eventsByPerson.containsKey(personID)) {
                    eventsByPerson.get(personID).add(e);
                } else {
                    eventsByPerson.put(personID, new TreeSet<Event>());
                    eventsByPerson.get(personID).add(e);
                }
            }
        }
    }

    public Event getEventSelected() {
        return eventSelected;
    }

    public void setEventSelected(Event eventSelected) {
        this.eventSelected = eventSelected;
    }

    public Map<String, Set<Event>> getEventsByPerson() {
        return eventsByPerson;
    }

    public void setEventsByPerson(Map<String, Set<Event>> eventsByPerson) {
        this.eventsByPerson = eventsByPerson;
    }

    public String getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    public Person[] getPersons() {
        return this.persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return this.events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
