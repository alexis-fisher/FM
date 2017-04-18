package server.services;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.handler.Converter;
import server.model.Event;
import server.model.Person;
import server.model.User;

/**
 * Created by Alyx on 2/17/17.
 */

public class Fill {
    // These "Events" are only half full - they only hold location data.
    private Event[] eventBank;

    // Counters to see how many people & events are created
    private int peopleCreated;
    private int eventsCreated;

    // Database & descendant (user)
    public Database db = new Database();
    public User descendant;

    // Random number generator
    private Random rand = new Random();

    // Name banks
    private String[] fnames;
    private String[] mnames;
    private String[] snames;

    // Random event bank
    private String[] randomEvents = { "Graduated, finally",
            "Finally started a job that isn't entry-level",
            "Started volunteer work because court required it",
            "Replaced futon with first real couch",
            "Kept desk plant alive for one month",
            "Actually went to the dentist"
    };

    // Year & generation standard numbers
    private static final int ARBITRARY_START_YEAR = 2000;
    private static final int DEFAULT_GENS = 4;
    private static final int GEN_YEAR_INCREASE = 22;

    // Hard coded gender values
    private static final String FEMALE = "f";
    private static final String MALE = "m";

    // Event types
    private static final String BIRTH = "birth";
    private static final String DEATH = "death";
    private static final String MARRIAGE = "marriage";


    /**
     * Fill constructor. Imports names & location data as well as initializing a few variables.
     * @throws DatabaseException
     */
    public Fill() throws DatabaseException {
        peopleCreated = 0;
        eventsCreated = 0;

        // Import fNames & mNames & sNames
        importNames();
        // import events
        importEvents();
    }

    /**
     * Drops (deletes) all events corresponding to username
     * @param username unique identifier of current user
     * @throws DatabaseException
     */
    public void dropUsersEvents(String username) throws DatabaseException {
        try {
            db.openTransaction();
            db.events.deleteEventsOfUser(username);
            db.closeTransaction(true);
        } catch (DatabaseException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Drops (deletes) all persons corresponding to username
     * @param username unique identifier of current user
     * @throws DatabaseException
     */
    public void dropUsersPersons(String username) throws DatabaseException {
        try {
            db.openTransaction();
            db.persons.dropPersonsbyUser(username);
            db.closeTransaction(true);
        } catch (DatabaseException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    /** Populates the server's database with four generation's worth of generated data for the specified user name
     *
     * @param userName A user already registered with the server
     */
    public void fillFourGenerations(String userName) throws DatabaseException {

        // Get user from name (for personID)
        db.openTransaction();
        descendant = db.users.getUserFromName(userName);
        Person descendantPersonObject = new Person(descendant.getPersonID(), descendant.getUserName(),
                                        descendant.getFirstName(), descendant.getLastName(), descendant.getGender());
        db.closeTransaction(true);

        int baseYear = ARBITRARY_START_YEAR;

        // auto-set generations to 4
        int generations = DEFAULT_GENS;
        peopleCreated++;

        // Add events for user
        createEvents(descendantPersonObject, baseYear);

        // Fill generations
            // tree structure. 1 -> 11 -> 11,11 -> 11,11,11,11; etc
            // GEN0 - Get user & make an event; DONE
            // GEN1 - Make user's parents + events,
            // GEN2 - Make parents of the user's parents + events, etc
            // Etc.
        fillPersons(descendantPersonObject, generations, baseYear);

    }

    /** Populates the server's database with a given number of generation's worth of generated data for the specified user name
     *
     * @param userName A user already registered with the server
     */
    public void fillThisManyGenerations(String userName, int generations) throws DatabaseException {
        // Get user from name (for personID)
        db.openTransaction();
        descendant = db.users.getUserFromName(userName);
        Person descendantPersonObject = db.persons.getPersonFromID(descendant.getPersonID());
        db.closeTransaction(true);

        int baseYear = ARBITRARY_START_YEAR;
        peopleCreated++;

        // Add events for user
        createEvents(descendantPersonObject, baseYear);

        // Fill generations
            // tree structure. 1 -> 11 -> 11,11 -> 11,11,11,11; etc
            // GEN0 - Get user & make an event; DONE
            // GEN1 - Make user's parents,
            // GEN2 - Make parents of the user's parents, etc
            // Etc.
        fillPersons(descendantPersonObject, generations, baseYear);
    }

    /**
     * Creates a birth event, death event, and another random event for a given Person.
     * @param person Person who is getting the events
     * @param baseYear year person is born.
     * @throws DatabaseException
     */
    public void createEvents(Person person, int baseYear) throws DatabaseException {
        // Create birth
        Event birth = anonymousEvent();
        birth.setYear(Integer.toString(baseYear));
        birth.setPerson(person.getPersonID());
        birth.setEventType(BIRTH);

        // Set their lifespan
        int lifeSpan = generateRandomNumberBetween(60,100);

        // Set death
        Event death = anonymousEvent();
        death.setYear(Integer.toString(baseYear + lifeSpan));
        death.setPerson(person.getPersonID());
        birth.setEventType(DEATH);

        // Set a random event (these usually work best from 20-59)
        int year = generateRandomNumberBetween(20,59);
        int randomEventIndex = generateRandomNumberBetween(0,randomEvents.length - 1);
        Event random = anonymousEvent();
        random.setYear(Integer.toString(year));
        random.setPerson(person.getPersonID());
        random.setEventType(randomEvents[randomEventIndex]);

//        ArrayList<Event>events = new ArrayList<>();
//        events.add(birth);
//        events.add(death);
//        events.add(random);
//
//        person.setEvents(events);
//        db.openTransaction();
//        db.persons.updatePerson(person.getPersonID(), person);
//        db.closeTransaction(true);


        addEventToDB(birth);
        addEventToDB(death);
        addEventToDB(random);

    }

    /**
     * Pulls a random location out of the eventBank and gives it an eventID
     * and sets the descendant to the current user, then returns it for
     * assignment to a Person.
     * @return an Event, ready to be assigned to a Person
     */
    public Event anonymousEvent(){
        // Pull random event locations from eventBank
        int index = generateRandomNumberBetween(0, (eventBank.length - 1));
        Event locationData = eventBank[index];

        // Put location data into this new event
        Event thisEvent = new Event();
        thisEvent.setLatitude(locationData.getLatitude());
        thisEvent.setLongitude(locationData.getLongitude());
        thisEvent.setCity(locationData.getCity());
        thisEvent.setCountry(locationData.getCountry());

        // Generate random eventID
        thisEvent.setEventID(UUID.randomUUID().toString());

        // Set descendant to user
        thisEvent.setDescendant(descendant.getUserName());

        return thisEvent;
    }

    /**
     * Marries two people, updating their marital status in the database, as well as
     * the wife's last name and creating a marriage event for each spouse.
     * @param wife Person object corresponding to one marital partner
     * @param husb Person object corresponding to one marital partner
     * @param baseYear Year they were born (so they don't get married too early/late)
     * @throws DatabaseException
     */
    public void addMarriage(Person wife, Person husb, int baseYear) throws DatabaseException {

        // First, set their spouse to each other.
        wife.setSpouse(husb.getPersonID());
        husb.setSpouse(wife.getPersonID());

        // Pick a marriage year based on BYU's average... hahah
        int marriageYear = baseYear + generateRandomNumberBetween(18,24);

        // Pull random location from eventBank & create wife's marriage
        Event wifeMarriage = anonymousEvent();
        wifeMarriage.setPerson(wife.getPersonID());
        wifeMarriage.setYear(Integer.toString(marriageYear));
        wifeMarriage.setEventType(MARRIAGE);

        // Change wife's last name to match husbands - we're traditional in this app
        wife.setLastName(husb.getLastName());


        // Use location from wife to create husb's marriage
        Event husbMarriage = new Event();
            // personal info
            husbMarriage.setPerson(husb.getPersonID());
            husbMarriage.setEventID(UUID.randomUUID().toString());
            // shared
            husbMarriage.setEventType(MARRIAGE);
            husbMarriage.setDescendant(wifeMarriage.getDescendant());
            husbMarriage.setYear(wifeMarriage.getYear());
            husbMarriage.setCity(wifeMarriage.getCity());
            husbMarriage.setCountry(wifeMarriage.getCountry());
            husbMarriage.setLatitude(wifeMarriage.getLatitude());
            husbMarriage.setLongitude(wifeMarriage.getLongitude());

        // Add events to the database!
        addEventToDB(wifeMarriage);
        addEventToDB(husbMarriage);

//        // Add events to the people!
//        wife.addEvent(wifeMarriage);
//        husb.addEvent(husbMarriage);

        // Commit person changes to the database
        db.openTransaction();
        db.persons.updatePerson(wife.getPersonID(), wife);
        db.persons.updatePerson(husb.getPersonID(), husb);
        db.closeTransaction(true);

    }

    /**
     * Imports event location data from a JSON file.
     * @throws DatabaseException
     */
    public void importEvents() throws DatabaseException{
        // Get JSON from locations.json file
        // Add them as instances of the class Event
        // and put those in eventBank array
        String eventFile = "data" + File.separator + "json" + File.separator + "locations.json";

        fillArray(eventFile, "events");

    }

    /**
     * Imports name data from a JSON file.
     * @throws DatabaseException
     */
    public void importNames() throws DatabaseException {
        // Get JSON from fNames.json, mNames.json and sNames.json files
        // Add them as instances of the class Event
        // and put those in eventBank array
        String fNames = "data" + File.separator + "json" + File.separator + "fnames.json";
        String mNames = "data" + File.separator + "json" + File.separator + "mnames.json";
        String sNames = "data" + File.separator + "json" + File.separator + "snames.json";

        fillArray(fNames, "fnames");
        fillArray(mNames, "mnames");
        fillArray(sNames, "snames");



    }

    /**
     * Given a file name, populates an array with the JSON data inside.
     * @param file file name & relative path
     * @param whichArray string telling function which array to fill.
     * @throws DatabaseException
     */
    public void fillArray(String file, String whichArray) throws DatabaseException {
        Gson g = new Gson();
        Converter c = new Converter();
        try {
            // Get file
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder request = new StringBuilder();
            String nextLine;
            while ((nextLine = r.readLine()) != null) {
                request.append(nextLine);
            }

            // Convert to JSON array
            String requestBody = request.toString();
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(requestBody);
            JsonArray jsonArray = jsonObject.has("data") ? jsonObject.getAsJsonArray("data") : new JsonArray();

            // if it's the event file, do this
            if(whichArray.equals("events")){

                // Get all events in a list
                ArrayList<Event> eventList = new ArrayList<>();
                for (JsonElement e : jsonArray) {
                    Event ev = g.fromJson(e, Event.class);
                    eventList.add(ev);
                }

                // Convert list to the proper array
                this.eventBank = new Event[eventList.size()];
                int index = 0;
                for (Event e : eventList) {
                    this.eventBank[index] = e;
                    index++;
                }

            }
            // if it's a name file, do this
            else {
                // Get all names in an Arraylist
                ArrayList<String> nameList = new ArrayList<>();
                for (JsonElement e : jsonArray) {
                    String name = g.fromJson(e, String.class);
                    nameList.add(name);
                }

                // Convert array list to the proper name array
                if (whichArray.equals("fnames")) {
                    this.fnames = new String[nameList.size()];
                    int index = 0;
                    for (String n : nameList) {
                        this.fnames[index] = n;
                        index++;
                    }
                } else if (whichArray.equals("mnames")) {
                    this.mnames = new String[nameList.size()];
                    int index = 0;
                    for (String n : nameList) {
                        this.mnames[index] = n;
                        index++;
                    }
                } else if (whichArray.equals("snames")) {
                    this.snames = new String[nameList.size()];
                    int index = 0;
                    for (String n : nameList) {
                        this.snames[index] = n;
                        index++;
                    }
                }
            }

        }  catch (IOException e) {
            throw new DatabaseException("Couldn't get files to fill names.");
        }
    }

    /**
     * Fills the specified number of generations with generated people, updates the database,
     * and generates events.
     * @param child Child of parents about to be created
     * @param gensLeft number of generations left to create
     * @param baseYear year that child was born
     * @throws DatabaseException
     */
    public void fillPersons(Person child, int gensLeft, int baseYear) throws DatabaseException {

        // Ensure mom & dad are 22-30 yrs older than child
        int baseYearMom = baseYear + GEN_YEAR_INCREASE + generateRandomNumberBetween(0,8);
        int baseYearDad = baseYear +  GEN_YEAR_INCREASE + generateRandomNumberBetween(0,8);

        /* NOTE: CHILDREN CAN BE BORN OUT OF WEDLOCK
                 .... not a bug. In the real world,
                 this happens
                 However, unlike reality, their
                 parents always get married by age
                 24, so at the worst they'd be unmarried
                 only 2 yrs... hahaha so they'll be fine
        */

        // add mom, this also generates events for them
        Person mom = makePerson(child, FEMALE, baseYearMom);

        // add dad, this also generates events for them
        Person dad = makePerson(child, MALE, baseYearDad);

        // Marry the parents
        addMarriage(mom, dad, baseYear);

        // If only one generation requested, done! else, continue...
        if(gensLeft <= 1){
            return;
        }

        gensLeft--;

        // set next base year to the smaller gap between mom & dad
        int nextGen = baseYearMom > baseYearDad ? baseYearDad : baseYearMom;

        // Add parents for the parents
        fillPersons(mom, gensLeft, nextGen);
        fillPersons(dad, gensLeft, nextGen);

        return;
    }

    /**
     * Creates a person (parent to child), and updates child's parent information in database
     * @param child child of parent that is created by this function
     * @param gender male or female
     * @param baseYear Year that parent should be born
     * @return person object (parent) that is created
     * @throws DatabaseException
     */
    public Person makePerson(Person child, String gender, int baseYear) throws DatabaseException {
        Person madePerson = new Person();
        // Generate index for their random last name
        int randIndexSNAMES = generateRandomNumberBetween(0, snames.length - 1);
        madePerson.setLastName(snames[randIndexSNAMES]);


        // Generate other info...
        madePerson.setPersonID(UUID.randomUUID().toString());
        madePerson.setDescendant(descendant.getUserName());
        madePerson.setGender(gender);
        if(gender.equals(FEMALE)){
            int randIndexFirstName = generateRandomNumberBetween(0, fnames.length - 1);
            madePerson.setFirstName(fnames[randIndexFirstName]);
            child.setMother(madePerson.getPersonID());
        } else{
            int randIndexFirstName = generateRandomNumberBetween(0, mnames.length - 1);
            madePerson.setFirstName(mnames[randIndexFirstName]);
            child.setFather(madePerson.getPersonID());
        }

        // Generate events
        createEvents(madePerson, baseYear);

        // Add the person to the database...
        addPersonToDB(madePerson);

        // Commit changes to the child!
        db.openTransaction();
        db.persons.updatePerson(child.getPersonID(),child);
        db.closeTransaction(true);

        return madePerson;
    }

    /**
     * Adds a person to the database
     * @param p Person object to add to the database
     * @throws DatabaseException
     */
    public void addPersonToDB(Person p) throws DatabaseException {
        // Add to database
        this.db.openTransaction();
        this.db.persons.createPerson(p.getPersonID(), p.getDescendant(),
                p.getFirstName(),p.getLastName(),p.getGender(),p.getFather(),
                p.getMother(),p.getSpouse());
        this.db.closeTransaction(true);

        // Increment people created counter
        peopleCreated++;
    }

    /**
     * Adds an event to the database
     * @param e Event object to add to the database
     * @throws DatabaseException
     */
    public void addEventToDB(Event e) throws DatabaseException {
        // Add to database
        this.db.openTransaction();
        this.db.events.createEvent(e.getEventID(), e.getDescendant(),
                e.getPerson(), e.getLatitude(),
                e.getLongitude(), e.getCountry(),
                e.getCity(), e.getEventType(), e.getYear());
        this.db.closeTransaction(true);

        // Increment events created counter
        eventsCreated++;
    }

    /**
     * Generates a random number in between the given 2 parameters
     * @param min the lower bound
     * @param max the upper bound
     * @return the random number that was generated
     */
    public int generateRandomNumberBetween(int min, int max){
        return rand.nextInt(max - min + 1) + min;
    }

    public int getPeopleCreated() {
        return peopleCreated;
    }

    public void setPeopleCreated(int peopleCreated) {
        this.peopleCreated = peopleCreated;
    }

    public int getEventsCreated() {
        return eventsCreated;
    }

    public void setEventsCreated(int eventsCreated) {
        this.eventsCreated = eventsCreated;
    }
}
