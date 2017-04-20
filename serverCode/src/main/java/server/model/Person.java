package server.model;

/**
 * Created by Alyx on 2/17/17.
 */

public class Person implements Searchable {
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

    @Override
    public boolean contains(String term) {
        // check first name
        if(this.getFirstName().toLowerCase().contains(term.toLowerCase())){
            return true;
        }
        // check last name
        if(this.getLastName().toLowerCase().contains(term.toLowerCase())){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o.getClass() != Person.class){
            return false;
        }
        if(o == this){
            return true;
        }
        Person person = (Person) o;

        // Compare all fields
        if(person.getPersonID().equals(this.getPersonID())){
            if(person.getDescendant().equals(this.getDescendant())){
                if(person.getMother().equals(this.getMother())){
                    if(person.getFather().equals(this.getFather())){
                        if(person.getSpouse().equals(this.getSpouse())){
                            if(person.getFirstName().equals(this.getFirstName())){
                                if(person.getLastName().equals(this.getLastName())){
                                    if(person.getGender().equals(this.getGender())){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
