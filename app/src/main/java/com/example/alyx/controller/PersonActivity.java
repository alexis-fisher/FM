package com.example.alyx.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.server.R;

import server.model.Person;
import server.proxy.ServerProxy;
import server.result.PersonResult;

/**
 * Created by Alyx on 4/13/17.
 */

public class PersonActivity extends AppCompatActivity {
    private String personID;
    private Person person;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;




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


        new PersonInfo().execute(personID);

    }


    private void setPerson(Person p){
        person = p;
        mFirstName.setText(person.getFirstName());
        mLastName.setText(person.getLastName());
        mGender.setText(person.getGender());
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