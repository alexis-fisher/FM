package com.example.alyx.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.example.alyx.server.R;

/**
 * Created by Alyx on 4/13/17.
 */

public class FilterActivity extends AppCompatActivity {
    // Switches
    private Switch mBirthSwitch;
    private Switch mDeathSwitch;
    private Switch mMarriageSwitch;
    private Switch mGraduationSwitch;
    private Switch mJobSwitch;
    private Switch mTaxSwitch;
    private Switch mBabySwitch;
    private Switch mCouchSwitch;
    private Switch mCandyCrushSwitch;
    private Switch mNetflixSwitch;
    private Switch mPlantSwitch;
    private Switch mDentistSwitch;
    private Switch mRetirementSwitch;
    private Switch mFoodSwitch;
    private Switch mVolunteerSwitch;

    // Model Variables
    private boolean showBirthEvents;
    private boolean showDeathEvents;
    private boolean showMarriageEvents;
    private boolean showGraduationEvents;
    private boolean showJobEvents;
    private boolean showTaxEvents;
    private boolean showBabyEvents;
    private boolean showCouchEvents;
    private boolean showCandyCrushEvents;
    private boolean showNetflixEvents;
    private boolean showPlantEvents;
    private boolean showDentistEvents;
    private boolean showRetirementEvents;
    private boolean showFoodEvents;
    private boolean showVolunteerEvents;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Set up buttons
        mBirthSwitch = (Switch) findViewById(R.id.birthSwitch);
        mBirthSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showBirthEvents){
                    showBirthEvents = false;
                }
                else {
                    showBirthEvents = true;
                }
            }
        });
        mDeathSwitch = (Switch) findViewById(R.id.deathSwitch);
        mDeathSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showDeathEvents){
                    showDeathEvents = false;
                }
                else {
                    showDeathEvents = true;
                }
            }
        });
        mMarriageSwitch = (Switch) findViewById(R.id.marriageSwitch);
        mMarriageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showMarriageEvents){
                    showMarriageEvents = false;
                }
                else {
                    showMarriageEvents = true;
                }
            }
        });
        mGraduationSwitch = (Switch) findViewById(R.id.graduationSwitch);
        mGraduationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showGraduationEvents){
                    showGraduationEvents = false;
                }
                else {
                    showGraduationEvents = true;
                }
            }
        });
        mJobSwitch = (Switch) findViewById(R.id.jobSwitch);
        mJobSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showJobEvents){
                    showJobEvents = false;
                }
                else {
                    showJobEvents = true;
                }
            }
        });
        mTaxSwitch = (Switch) findViewById(R.id.taxSwitch);
        mTaxSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showTaxEvents){
                    showTaxEvents = false;
                }
                else {
                    showTaxEvents = true;
                }
            }
        });
        mBabySwitch = (Switch) findViewById(R.id.babySwitch);
        mBabySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showBabyEvents){
                    showBabyEvents = false;
                }
                else {
                    showBabyEvents = true;
                }
            }
        });
        mCouchSwitch = (Switch) findViewById(R.id.couchSwitch);
        mCouchSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showCouchEvents){
                    showCouchEvents = false;
                }
                else {
                    showCouchEvents = true;
                }
            }
        });
        mCandyCrushSwitch = (Switch) findViewById(R.id.candyCrushSwitch);
        mCandyCrushSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showCandyCrushEvents){
                    showCandyCrushEvents = false;
                }
                else {
                    showCandyCrushEvents = true;
                }
            }
        });
        mNetflixSwitch = (Switch) findViewById(R.id.netflixSwitch);
        mNetflixSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showNetflixEvents){
                    showNetflixEvents = false;
                }
                else {
                    showNetflixEvents = true;
                }
            }
        });
        mPlantSwitch = (Switch) findViewById(R.id.plantSwitch);
        mPlantSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPlantEvents){
                    showPlantEvents = false;
                }
                else {
                    showPlantEvents = true;
                }
            }
        });
        mDentistSwitch = (Switch) findViewById(R.id.dentistSwitch);
        mDentistSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showDentistEvents){
                    showDentistEvents = false;
                }
                else {
                    showDentistEvents = true;
                }
            }
        });
        mRetirementSwitch = (Switch) findViewById(R.id.retirementSwitch);
        mRetirementSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showRetirementEvents){
                    showRetirementEvents = false;
                }
                else {
                    showRetirementEvents = true;
                }
            }
        });
        mFoodSwitch = (Switch) findViewById(R.id.foodSwitch);
        mFoodSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showFoodEvents){
                    showFoodEvents = false;
                }
                else {
                    showFoodEvents = true;
                }
            }
        });
        mVolunteerSwitch = (Switch) findViewById(R.id.volunteerSwitch);
        mVolunteerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showVolunteerEvents){
                    showVolunteerEvents = false;
                }
                else {
                    showVolunteerEvents = true;
                }
            }
        });
    }

}