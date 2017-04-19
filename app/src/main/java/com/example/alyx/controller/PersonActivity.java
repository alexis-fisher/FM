package com.example.alyx.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;


import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import server.model.Person;
import server.model.Event;
import server.proxy.ServerProxy;
import server.result.PersonResult;

/**
 * Created by Alyx on 4/13/17.
 */

public class PersonActivity extends AppCompatActivity {

    // Person data (id, family members)
    private String personID;
    private Person person = null;
    private Person mother;
    private Person father;
    private Person spouse;

    // UI Name info
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;

    /** Access the model */
    private Model model = Model.instanceOf();

    private TextView eventListPlaceHolder;
    private TextView personListPlaceHolder;
    private TextView personListPlaceHolder1;
    private TextView personListPlaceHolder2;

    /** Holds the events that belong to THIS PERSON */
    private Set<Event> events = new TreeSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Get personID
        personID = getIntent().getStringExtra("personID");

        // Get this person's events
        if(model.getEventsOf(personID) != null){
            events = model.getEventsOf(personID);
        }

        // Get rid of this. filler.
        eventListPlaceHolder = (TextView) findViewById(R.id.eventsPersonList);
        personListPlaceHolder = (TextView) findViewById(R.id.relationsPersonList);
        personListPlaceHolder1 = (TextView) findViewById(R.id.relationsPersonList1);
        personListPlaceHolder2 = (TextView) findViewById(R.id.relationsPersonList2);
        TreeSet<Event> eventsExample = (TreeSet) events;
        eventListPlaceHolder.setText(eventsExample.first().getEventType());


        // Get person from the personID
        new PersonInfo().execute(personID);

        // Hook up UI text views and set the name values to this person's!
        mFirstName = (TextView) findViewById (R.id.firstNameVariable);
        mLastName = (TextView) findViewById (R.id.lastNameVariable);
        mGender = (TextView) findViewById (R.id.genderVariable);
        mFirstName.setText("");
        mLastName.setText("");
        mGender.setText("");

    }

    /**
     * Customizes this activity to the given person, and adds parent and spouse info.
     * @param p person for Activity, or spouse/mother/father
     */
    private void setPerson(Person p){
        // If the person for this activity hasn't been set... set it to this person!
        if(person == null) {
            person = p;

            // Set name
            mFirstName.setText(person.getFirstName());
            mLastName.setText(person.getLastName());

            // Set gender
            if (person.getGender().equals("m")) {
                mGender.setText("Male");
            } else if (person.getGender().equals("f")) {
                mGender.setText("Female");
            } else {
                mGender.setText(person.getGender());
            }

            // Get their family members
            if(person.getSpouse() != null){
                new PersonInfo().execute(person.getSpouse());
            }
            if(person.getMother() != null){
                new PersonInfo().execute(person.getMother());
            }
            if(person.getFather() != null){
                new PersonInfo().execute(person.getFather());
            }


        } else {
            // If we've got the initial person, this must be one of the family members....

            // If the ID belongs to the mother, this must be the mother! Set it up.
            if(person.getMother() != null && person.getMother().equals(p.getPersonID())){
                mother = p;
                personListPlaceHolder.setText("Mother: " + mother.getFirstName());

            // If the ID belongs to the father, this must be the father! Set it up.
            } else if (person.getFather() != null && person.getFather().equals(p.getPersonID())){
                father = p;
                personListPlaceHolder1.setText("Father: " + father.getFirstName());

            // If the ID belongs to the spouse, this must be the spouse! Set it up.
            } else if(person.getSpouse() != null && person.getSpouse().equals(p.getPersonID())){
                spouse = p;
                personListPlaceHolder2.setText("Spouse: " + spouse.getFirstName());
            }
        }

    }

    /**
     * Prints toast.
     * @param toast the message to print
     */
    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieves the person from the personID given
     */
    public class PersonInfo extends AsyncTask<String, Integer, Person> {
        /** Person result to store info from the database */
        private PersonResult personResult = new PersonResult();

        /** Database accessor */
        private ServerProxy mProxy = ServerProxy.server();

        /** PersonID (person to find) */
        private String personID;

        protected Person doInBackground(String... urls) {
            // Get event ID
            this.personID = urls[0];


            // Get person by id
            personResult = mProxy.person(personID);

            // If no error message, return the person!
            if(personResult.getMessage() == null || personResult.getMessage().equals("")) {
                return personResult.getPerson();
            }

            // if error message, return null
            else {
                return null;
            }

        }

        protected void onProgressUpdate(Integer... progress) {
            // progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Person success) {
            if(success != null){
                setPerson(success);
            } else {
                printToast(personResult.getMessage());
            }
        }
    }
}