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
    ServerProxy server = ServerProxy.server();

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

    private Person[] persons;
    private Event[] events;

    private Model(){}

    public static Model instanceOf(){
        if(_instance == null){
            _instance = new Model();
        }
        return _instance;
    }

    private static Model _instance;

    public boolean getPeopleFromServer() throws ClientException {
        PersonResult peopleFromServer = this.server.person();
        if(peopleFromServer.getMessage() == null || peopleFromServer.getMessage().equals("")){
            this.persons = peopleFromServer.data;
            return true;
        } else {
            return false;
        }
    }

    public boolean getEventsFromServer() throws ClientException {
        EventResult eventsFromServer = this.server.event();
        if(eventsFromServer.getMessage() == null || eventsFromServer.getMessage().equals("")){
            this.events = eventsFromServer.data;
            return true;
        } else {
            return false;
        }
    }
}
