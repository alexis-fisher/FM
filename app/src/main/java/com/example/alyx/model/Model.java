package com.example.alyx.model;

import server.model.Person;
import server.model.Event;
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

    // All person and event data from the database
    private Person[] persons;
    private Event[] events;

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

    // Pull all people info from the server
    public boolean getPeopleFromServer() throws ClientException {
        PersonResult peopleFromServer = this.server.person();
        if(peopleFromServer.getMessage() == null || peopleFromServer.getMessage().equals("")){
            this.persons = peopleFromServer.data;
            return true;
        } else {
            return false;
        }
    }

    // Pull all event info from the server
    public boolean getEventsFromServer() throws ClientException {
        EventResult eventsFromServer = this.server.event();
        if(eventsFromServer.getMessage() == null || eventsFromServer.getMessage().equals("")){
            this.events = eventsFromServer.data;
            return true;
        } else {
            return false;
        }
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
