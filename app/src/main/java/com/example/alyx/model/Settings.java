package com.example.alyx.model;

import android.graphics.Color;

/**
 * Created by Alyx on 4/14/17.
 */

public class Settings {

    // Private Constructor
    private Settings(){}

    // Getter for Singleton
    public static Settings instanceOf(){
        if(_instance == null){
            _instance = new Settings();
        }
        return _instance;
    }

    // Variable that holds the ONE instance of this class
    private static Settings _instance;

    // Variable that accesses the model
    private Model model = Model.instanceOf();


    public boolean isShowLifeLines() {
        return showLifeLines;
    }

    public void setShowLifeLines(boolean showLifeLines) {
        this.showLifeLines = showLifeLines;
    }

    public boolean isShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    // Options for Settings
    private boolean showLifeLines = false;
    private boolean showFamilyTreeLines = false;
    private boolean showSpouseLines = true;
    private int lifeLineColor = Color.DKGRAY;
    private int familyTreeLineColor = Color.GREEN;
    private int spouseLineColor = Color.BLACK;
    private String mapType;


    public void onOffLifeLines(){
        if(showLifeLines){
            showLifeLines = false;
        }
        else {
            showLifeLines = true;
        }
    }

    public void onOffFamilyTreeLines(){
        if(showFamilyTreeLines){
            showFamilyTreeLines = false;
        }
        else {
            showFamilyTreeLines = true;
        }
    }

    public void onOffSpouseLines(){
        if(showSpouseLines){
            showSpouseLines = false;
        }
        else {
            showSpouseLines = true;
        }
    }

    public int getLifeLineColor() {
        return lifeLineColor;
    }

    public void setLifeLineColor(int lifeLineColor) {
        this.lifeLineColor = lifeLineColor;
    }

    public int getFamilyTreeLineColor() {
        return familyTreeLineColor;
    }

    public void setFamilyTreeLineColor(int familyTreeLineColor) {
        this.familyTreeLineColor = familyTreeLineColor;
    }

    public int getSpouseLineColor() {
        return spouseLineColor;
    }

    public void setSpouseLineColor(int spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
