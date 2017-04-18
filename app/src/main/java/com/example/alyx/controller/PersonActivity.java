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
    private String personID;
    private Person person = null;
    private Person mother;
    private Person father;
    private Person spouse;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;

    private Model model = Model.instanceOf();

    private TextView eventListPlaceHolder;
    private TextView personListPlaceHolder;
    private TextView personListPlaceHolder1;
    private TextView personListPlaceHolder2;

    private Set<Event> events = new TreeSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        personID = getIntent().getStringExtra("personID");


        mFirstName = (TextView) findViewById (R.id.firstNameVariable);
        mLastName = (TextView) findViewById (R.id.lastNameVariable);
        mGender = (TextView) findViewById (R.id.genderVariable);
        mFirstName.setText("");
        mLastName.setText("");
        mGender.setText("");


        eventListPlaceHolder = (TextView) findViewById(R.id.eventsPersonList);
        personListPlaceHolder = (TextView) findViewById(R.id.relationsPersonList);
        personListPlaceHolder1 = (TextView) findViewById(R.id.relationsPersonList1);
        personListPlaceHolder2 = (TextView) findViewById(R.id.relationsPersonList2);

        if(model.getEventsOf(personID) != null){
            events = model.getEventsOf(personID);
        }

        TreeSet<Event> eventsExample = (TreeSet) events;
        eventListPlaceHolder.setText(eventsExample.first().getEventType());

        new PersonInfo().execute(personID);

    }


    private void setPerson(Person p){
        if(person == null) {
            person = p;
            mFirstName.setText(person.getFirstName());
            mLastName.setText(person.getLastName());
            if (person.getGender().equals("m")) {
                mGender.setText("Male");
            } else if (person.getGender().equals("f")) {
                mGender.setText("Female");
            } else {
                mGender.setText(person.getGender());
            }
            new PersonInfo().execute(person.getMother());
            new PersonInfo().execute(person.getFather());
            new PersonInfo().execute(person.getSpouse());

        } else {
            if(person.getMother().equals(p.getPersonID())){
                mother = p;
                personListPlaceHolder.setText("Mother: " + mother.getFirstName());
                // display mother in list
            } else if (person.getFather().equals(p.getPersonID())){
                father = p;
                // display father in list
                personListPlaceHolder1.setText("Father: " + father.getFirstName());

            } else if(person.getSpouse().equals(p.getPersonID())){
                spouse = p;
                personListPlaceHolder2.setText("Spouse: " + spouse.getFirstName());

                // display spouse in list
            }
        }

    }

    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }


    public class PersonInfo extends AsyncTask<String, Integer, Person> {
        private PersonResult personResult = new PersonResult();
        private ServerProxy mProxy = ServerProxy.server();
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