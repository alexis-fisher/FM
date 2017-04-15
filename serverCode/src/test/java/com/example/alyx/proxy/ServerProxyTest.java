package com.example.alyx.proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.model.Person;
import server.model.User;
import server.model.Event;
import server.request.FillRequest;
import server.request.LoadRequest;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.ClearResult;
import server.result.EventResult;
import server.result.FillResult;
import server.result.LoadResult;
import server.result.LoginResult;
import server.result.PersonResult;
import server.result.RegisterResult;

import static org.junit.Assert.assertTrue;


/**
 * Created by Alyx on 3/18/17.
 */
public class ServerProxyTest {
    ServerProxy sp = new ServerProxy();

    @Before
    public void setUp() throws Exception {
        // Ensure database is cleared!
        testClear();
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testRegister() throws Exception {
        // Make a request with artificial data
        RegisterRequest r = new RegisterRequest();
        r.setUserName("username");
        r.setPassword("password");
        r.setEmail("email");
        r.setFirstName("firstName");
        r.setLastName("lastName");
        r.setGender("gender");

        // Send the request
        RegisterResult result = sp.register(r);

        // Assert that the username was returned
        // If it failed, the userName will not be returned.
        assertTrue(result.getUserName().equals("username"));
    }



    @Test
    public void testLogin() throws Exception {
        // Make a request with artificial data
        LoginRequest l = new LoginRequest();
        l.setUserName("username");
        l.setPassword("password");

        // Send the request
        LoginResult result = sp.login(l);

        // No user registered, so should fail!
        assertTrue(result.getMessage().equals("No user with that name!"));

        // Register User so that this time the login SHOULD succeed.
        testRegister();

        // Send the request
        result = sp.login(l);

        // Assert that the username was returned
        // If it failed, the userName will not be returned.
        assertTrue(result.getUserName().equals("username"));

    }

    @Test
    public void testClear() throws Exception {
        // Send a clear request
        ClearResult c = sp.clear();

        // Assert that we received the success response
        assertTrue(c.getResponseMessage().equals("Clear succeeded."));
    }

    @Test
    public void testFill() throws Exception {

        // Test random number of generations

        FillRequest f = new FillRequest();
        f.setUserName("username");
        f.setGenerations(1);

        FillResult result = sp.fill(f);

        // Assert that we received the success response
        assertTrue(result.getResponseMessage().equals("Successfully added 3 persons and 11 events to the database."));

        // Test automatic number of generations
        f.setGenerations(0);
        result = sp.fill(f);

        assertTrue(result.getResponseMessage().equals("Successfully added 31 persons and 123 events to the database."));

    }

    @Test
    public void testLoad() throws Exception {
        LoadRequest l = new LoadRequest();

        // Add one user
        User u = new User();
        u.setUsername("sheila");
        u.setPassword("parker");
        u.setEmail("sheila@parker.com");
        u.setFirstName("Sheila");
        u.setLastName("Parker");
        u.setGender("f");
        u.setPersonID("Sheila_Parker");

        User[] users = new User[1];
        users[0] = u;

        // Add 2 persons
        Person p1 = new Person("personID", "descendant",
                "firstName", "lastName", "gender");
        Person p2 = new Person("personID1", "descendant",
                "firstName", "lastName", "gender");
        Person[] persons = new Person[2];
        persons[0] = p1;
        persons[1] = p2;


        // Add 3 events
        server.model.Event e = new server.model.Event();
        e.setEventID("eventID");
        e.setDescendant("descendant");
        e.setYear("year");
        e.setLongitude("longitude");
        e.setLatitude("latitude");
        e.setCountry("country");
        e.setCity("city");
        e.setPerson("personID");
        e.setEventType("eventType");

        server.model.Event e1 = new server.model.Event();
        e1.setEventID("eventID1");
        e1.setDescendant("descendant");
        e1.setYear("year");
        e1.setLongitude("longitude");
        e1.setLatitude("latitude");
        e1.setCountry("country");
        e1.setCity("city");
        e1.setPerson("personID");
        e1.setEventType("eventType");

        server.model.Event e2 = new server.model.Event();
        e2.setEventID("eventID2");
        e2.setDescendant("descendant");
        e2.setYear("year");
        e2.setLongitude("longitude");
        e2.setLatitude("latitude");
        e2.setCountry("country");
        e2.setCity("city");
        e2.setPerson("personID");
        e2.setEventType("eventType");

        server.model.Event[] events = new server.model.Event[3];
        events[0] = e;
        events[1] = e1;
        events[2] = e2;

        l.setUsers(users);
        l.setPersons(persons);
        l.setEvents(events);

        LoadResult result = sp.load(l);

        // Assert that the response message is consistent with the data loaded
        assertTrue(result.getResponseMessage().equals("Successfully added 1 users, 2 persons, and 3 events"));
    }

    @Test
    public void testPerson() throws Exception {
        // Register a user, so there are persons
        testRegister();

        // Test get all persons
        PersonResult result = sp.person();
        assertTrue(result.data != null && result.data.length != 0);

        //Test get one person
        Person person = result.data[0];
        result = sp.person(person.getPersonID());

        // Verify that you pulled the person you searched for
        assertTrue(result.person.getFirstName().equals(person.getFirstName()));

    }


    @Test
    public void testEvent() throws Exception {
        // Register a user, so there are events
        testRegister();

        // Test get all persons
        EventResult result = sp.event();
        assertTrue(result.data != null && result.data.length != 0);

        //Test get one person
        Event event = result.data[0];
        result = sp.event(event.getEventID());

        // Verify that you pulled the person you searched for
        assertTrue(result.event.getCity().equals(event.getCity()));

    }


}