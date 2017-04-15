package com.example.alyx.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.alyx.model.Settings;
import com.example.alyx.server.R;

public class SettingsActivity extends AppCompatActivity {
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

    // Model Variables
    private String lifeLineColor;
    private String familyTreeLineColor;
    private String spouseLineColor;
    private String mapType;

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


    }
}
