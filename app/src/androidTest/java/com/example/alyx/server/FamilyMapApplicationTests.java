package com.example.alyx.server;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.example.alyx.controller.LoginFragment;
import com.example.alyx.model.Model;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import server.proxy.ClientException;
import server.proxy.ServerProxy;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.EventResult;
import server.result.LoginResult;
import server.result.PersonResult;
import server.result.RegisterResult;
import server.model.Person;
import server.model.Event;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class FamilyMapApplicationTests extends AndroidTestCase {
    private Model model;
    private ServerProxy proxy;
    private Map<String, Set<Event>> events = new HashMap<String,Set<Event>>();


    /**
     * Tests login and data sync. Also is a prereq for the other
     * test cases.
     * @throws Exception
     */
    @Test
    public void setUp() throws Exception {
        model = Model.instanceOf();
        proxy = ServerProxy.server();

        // Log in
        LoginRequest loginRequest = new LoginRequest("username","password");
        LoginResult loginResult = new LoginResult();
        try {
            loginResult = proxy.login(loginRequest);
        } catch (ClientException e){
            throw new Exception(e.getMessage());
        }
        if(loginResult.getMessage() != null && !loginResult.getMessage().equals("")){
            // Login failed. Try to register instead.
            RegisterRequest registerRequest = new RegisterRequest("username", "password", "email",
                    "John", "Doe", "m");
            RegisterResult registerResult = new RegisterResult();
            try{
                registerResult = proxy.register(registerRequest);
            } catch (ClientException e){
                throw new Exception(e.getMessage());
            }

            if(registerResult.getMessage() != null && !registerResult.getMessage().equals("")) {
                throw new Exception(registerResult.getMessage());
            }
        }

        // Download data (person & event)
        try {
            assertTrue(model.getEventsFromServer());
            assertTrue(model.getPeopleFromServer());
        } catch (ClientException e){
            throw new Exception(e.getMessage());
        }

        // Check data from model with result
        EventResult eventResult = new EventResult();
        PersonResult personResult = new PersonResult();

        try {
            eventResult = proxy.event();
            personResult = proxy.person();
        } catch (ClientException e){
            throw new Exception(e.getMessage());
        }
        Event[] eventsFromServer = eventResult.data;
        Person[] peopleFromServer = personResult.data;

        Event[] eventsFromModel = model.getEvents();
        Person[] peopleFromModel = model.getPersons();

        assertTrue(eventsFromModel.length == eventsFromServer.length);
        assertTrue(peopleFromModel.length == peopleFromServer.length);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.alyx.server", appContext.getPackageName());
    }

    // Calculate family relationships (PersonActivity??)
    @Test
    public void testFamilyRelationshipCalculations(){

    }

    // Test filtering (Sort events by type)
    @Test
    public void testEventFiltering(){
        events = model.getEventsByType();
        for(Map.Entry<String,Set<Event>> entry: events.entrySet()){
            Set<Event> eventsTypeX = entry.getValue();
            for(Event e : eventsTypeX){
                assertTrue(e.getEventType().equals(entry.getKey()));
            }
        }
    }

    // Test chronologially ordering events, (Sort events, then check out the treeset)
    @Test
    public void testEventChronologicalOrdering(){
        events = model.getEventsByPerson();
        for(Map.Entry<String,Set<Event>> entry: events.entrySet()){
            Set<Event> thisPersonsEvents = entry.getValue();
            int previousYear = 0;
            for(Event e : thisPersonsEvents){
                assertTrue(Integer.parseInt(e.getYear()) > previousYear);
                previousYear = Integer.parseInt(e.getYear());
            }
        }
    }
}
