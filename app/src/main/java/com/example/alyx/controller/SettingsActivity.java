package com.example.alyx.controller;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Settings;
import com.example.alyx.server.R;


public class SettingsActivity extends AppCompatActivity implements Caller {

    @Override
    public FragmentManager getThisFragmentManager(){
        return getThisFragmentManager();
    }

    /** Access the settings for the app */
    private Settings settings = Settings.instanceOf();

    // Switches
    private Switch mLifeLinesSwitch;
    private Switch mFamilyTreeLinesSwitch;
    private Switch mSpouseLinesSwitch;

    // Spinners
    private Spinner mLifeLineSpinner;
    private Spinner mFamilyTreeLineSpinner;
    private Spinner mSpouseLineSpinner;
    private Spinner mMapSpinner;

    // Logout & Resync buttons
    private TextView mResync;
    private TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Hook up UI
        mLifeLinesSwitch = (Switch) findViewById(R.id.lifeLinesSwitch);
        mSpouseLinesSwitch = (Switch) findViewById(R.id.spouseLinesSwitch);
        mFamilyTreeLinesSwitch = (Switch) findViewById(R.id.familyLinesSwitch);
        mLifeLineSpinner = (Spinner) findViewById(R.id.ddMenuLife);
        mFamilyTreeLineSpinner = (Spinner) findViewById(R.id.ddMenuFam);
        mSpouseLineSpinner = (Spinner) findViewById(R.id.ddMenuSpouse);
        mMapSpinner = (Spinner) findViewById(R.id.ddMenuMap);
        mResync = (TextView) findViewById(R.id.reSyncDataText);
        mLogout = (TextView) findViewById(R.id.logoutText);


        // "Initialize visual fields"
        if (savedInstanceState != null) {
            mFamilyTreeLineSpinner.setSelection(savedInstanceState.getInt("familyLineSpinner", 0));
            mSpouseLineSpinner.setSelection(savedInstanceState.getInt("spouseLineSpinner", 0));
            mLifeLineSpinner.setSelection(savedInstanceState.getInt("lifeLineSpinner", 0));
            // do this for each of your text views

            mLifeLinesSwitch.setChecked(settings.isShowLifeLines());
            mFamilyTreeLinesSwitch.setChecked(settings.isShowFamilyTreeLines());
            mSpouseLinesSwitch.setChecked(settings.isShowSpouseLines());
        }

        // Set up buttons
        mLifeLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.toggleLifeLines();
            }
        });
        mFamilyTreeLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.toggleFamilyTreeLines();

            }
        });
        mSpouseLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.toggleSpouseLines();
            }
        });


        mLifeLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               settings.setLifeLineColor(parent.getItemAtPosition(position).toString());
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
        });

        mFamilyTreeLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setFamilyTreeLineColor(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpouseLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setSpouseLineColor(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setMapType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResyncTask().execute(SettingsActivity.this);
                backToMap();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go into login thing
                printToast("Should be going to login screen now!");
                toLoginScreen();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    /**
     * Navigates back to the LoginScreen
     */
    private void toLoginScreen(){
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * Navigates back to the Map
     */
    private void backToMap(){
        Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lifeLineSpinner", mLifeLineSpinner.getSelectedItemPosition());
        outState.putInt("spouseLineSpinner", mSpouseLineSpinner.getSelectedItemPosition());
        outState.putInt("familyTreeLineSpinner", mFamilyTreeLineSpinner.getSelectedItemPosition());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Resyncs data (person & event data), getting data from the database
     * @param toMapNext boolean of whether or not to go to the map next.
     */
    public void resync(boolean toMapNext){
        new ResyncTask().execute(this);
    }
}
