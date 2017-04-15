package server.request;

import server.model.Event;
import server.model.Person;
import server.model.User;

/**
 * Created by Alyx on 2/17/17.
 */

public class LoadRequest {
    /** An array of users to be created */
    private User[] users;

    /** "Persons" family history information for these users */
    private Person[] persons;

    /** "Events" family history information for these users */
    private Event[] events;

    /** Creates a new LoadRequest object */
    public LoadRequest(User[] users, Person[] persons, Event[] events){
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public LoadRequest(){}

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
