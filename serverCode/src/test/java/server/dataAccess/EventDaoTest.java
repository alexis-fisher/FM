package server.dataAccess;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.model.Event;

import static org.junit.Assert.assertTrue;


/**
 * Created by Alyx on 3/18/17.
 */
public class EventDaoTest {
    Database db = new Database();
    EventDao ed = new EventDao(db);

    @Before
    public void setUp() throws Exception {
        // Open transaction
        db.openTransaction();

        // Ensure event table is empty
        ed.deleteAllEvents();

    }

    @After
    public void tearDown() throws Exception {
        // Close transaction
        db.closeTransaction(true);

    }

    @Test
    public void testCreateEvent() throws Exception {
        // Create the event 
        ed.createEvent("id_placeholder", "descendant_placeholder", "personID_placeholder",
                "latitude_placeholder", "longitude_placeholder", "country_placeholder",
                "city_placeholder", "eventType_placeholder", "year_placeholder");

        // Make sure the event is in the DB
        Event[] events = ed.read();
        for (Event p : events) {
            assertTrue(p.getEventID().equals("id_placeholder"));
        }
    }

    @Test
    public void testRead() throws Exception {
        // Create event 
        ed.createEvent("id_placeholder", "descendant_placeholder", "personID_placeholder",
                "latitude_placeholder", "longitude_placeholder", "country_placeholder",
                "city_placeholder", "eventType_placeholder", "year_placeholder");
        ed.createEvent("id_placeholder1", "descendant_placeholder", "personID_placeholder",
                "latitude_placeholder", "longitude_placeholder", "country_placeholder",
                "city_placeholder", "eventType_placeholder", "year_placeholder");
        // Read events from DB and assert that they are what was created (they were read correctly)
        Event[] events = ed.read();
        assertTrue (events != null);

        // Since we don't know what order the DB will read them out in, we just have to ensure that both were entered
        // The DB will not take duplicates, so this is a valid way to test that both were entered & read.
        assertTrue(events[0].getEventID().equals("id_placeholder") || events[0].getEventID().equals("id_placeholder1") );
        assertTrue(events[1].getEventID().equals("id_placeholder1") || events[1].getEventID().equals("id_placeholder") );


    }

    @Test
    public void testDeleteEvent() throws Exception {
        // put someone in the events table
        ed.createEvent("id_placeholder", "descendant_placeholder", "personID_placeholder",
                "latitude_placeholder", "longitude_placeholder", "country_placeholder",
                "city_placeholder", "eventType_placeholder", "year_placeholder");
        // read data out of events table - there should be something there
        Event[] events = ed.read();

        // ensure that create added something
        assertTrue(events.length != 0);

        // delete it from the table
        ed.deleteEvent("id_placeholder");

        // read data out of the events table - there should be nothing there now
        Event[] events1 = ed.read();

        // if anything in events table, throw exception
        assertTrue(events1.length == 0 || events1 == null);
    }

    @Test
    public void testDeleteAllEvents() throws Exception {
        // put stuff in the events table
        ed.createEvent("id_placeholder", "descendant_placeholder", "personID_placeholder",
                "latitude_placeholder", "longitude_placeholder", "country_placeholder",
                "city_placeholder", "eventType_placeholder", "year_placeholder");
        ed.createEvent("id_placeholder1", "descendant_placeholder", "personID_placeholder",
                "latitude_placeholder", "longitude_placeholder", "country_placeholder",
                "city_placeholder", "eventType_placeholder", "year_placeholder");

        // read data out of events table - there should be something there
        Event[] events = ed.read();

        // ensure that create added something
        assertTrue(events.length != 0);

        // clear the events table
        ed.deleteAllEvents();

        // read data out of the events table - there should be nothing there now
        Event[] events1 = ed.read();

        // if anything in events table, throw exception
        assertTrue(events1.length == 0 || events1 == null);
    }

}