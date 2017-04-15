package server.result;

import server.model.Person;

/**
 * Created by Alyx on 2/17/17.
 */

public class PersonResult {
    public server.model.Person[] data;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public server.model.Person person;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public PersonResult(){
        person = null;
    }
}
