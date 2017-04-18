package server.model;

import java.util.ArrayList;

/**
 * Created by Alyx on 2/17/17.
 */

public class Person {
    /** Unique identifier for this person */
    private String personID;

    /** User to which this person belongs */
    private String descendant;

    /** Person’s first name (non-empty string) */
    private String firstName;

    /** Person’s last name (non-empty string) */
    private String lastName;

    /** Person’s gender (Male or Female) */
    private String gender;

    /** Person’s mother (possibly null) */
    private String mother;

    /** Person’s father (possibly null) */
    private String father;

    /** Person’s spouse (possibly null) */
    private String spouse;

//    public ArrayList<Event> getEvents() {
//        return events;
//    }
//
//    public void setEvents(ArrayList<Event> events) {
//        this.events = events;
//    }
//
//    private ArrayList<Event> events = new ArrayList<>();
//
//    public void addEvent(Event e){
//        events.add(e);
//    }

    /** Creates new Person object */
    public Person(){
        personID = "";
        descendant = "";
        firstName = "";
        lastName = "";
        gender = "";
        mother = null;
        father = null;
        spouse = null;
    }

    public Person(String personID, String descendant, String firstName, String lastName,
                String gender){
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.descendant = descendant;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}
