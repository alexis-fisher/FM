package com.example.alyx.controller;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Settings;
import com.example.alyx.server.R;


public class SettingsActivity extends AppCompatActivity implements Caller {


    public void resync(boolean toMapNext){
        new ResyncTask().execute(this);

    }
    @Override
    public FragmentManager getThisFragmentManager(){
        return getThisFragmentManager();
    }

    // Access the settings for the app
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

    // Colors
    private final String red = "RED";
    private final String green = "GREEN";
    private final String blue = "BLUE";

    // Logout & Resync buttons
    private TextView mResync;
    private TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up buttons
        mLifeLinesSwitch = (Switch) findViewById(R.id.lifeLinesSwitch);
        mLifeLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.onOffLifeLines();
            }
        });
        mFamilyTreeLinesSwitch = (Switch) findViewById(R.id.familyLinesSwitch);
        mFamilyTreeLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.onOffFamilyTreeLines();

            }
        });
        mSpouseLinesSwitch = (Switch) findViewById(R.id.spouseLinesSwitch);
        mSpouseLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.onOffSpouseLines();
            }
        });

        // WIRE THESE UP WITH THE RIGHT VALUES!
        mLifeLineSpinner = (Spinner) findViewById(R.id.ddMenuLife);
        mFamilyTreeLineSpinner = (Spinner) findViewById(R.id.ddMenuFam);
        mSpouseLineSpinner = (Spinner) findViewById(R.id.ddMenuSpouse);
        mMapSpinner = (Spinner) findViewById(R.id.ddMenuMap);

        mResync = (TextView) findViewById(R.id.reSyncDataText);
        mResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResyncTask().execute(SettingsActivity.this);
            }
        });

        mLogout = (TextView) findViewById(R.id.logoutText);
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
    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    private void toLoginScreen(){
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
