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
    private boolean showSpouseLines = false;
    private int lifeLineColor = Color.DKGRAY;
    private int familyTreeLineColor = Color.GREEN;
    private int spouseLineColor = Color.BLACK;
    private String mapType = "Normal";


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

    public void setLifeLineColor(String lifeLineColor) {
        if(lifeLineColor.equals("Gray")){
            this.lifeLineColor = Color.GRAY;
        } else if (lifeLineColor.equals("Green")) {
            this.lifeLineColor = Color.GREEN;
        } else {
            this.lifeLineColor = Color.BLACK;
        }
    }

    public int getFamilyTreeLineColor() {
        return familyTreeLineColor;
    }

    public void setFamilyTreeLineColor(String familyTreeLineColor) {
        if(familyTreeLineColor.equals("Gray")){
            this.familyTreeLineColor = Color.GRAY;
        } else if (familyTreeLineColor.equals("Green")) {
            this.familyTreeLineColor = Color.GREEN;
        } else {
            this.familyTreeLineColor = Color.BLACK;
        }
    }

    public int getSpouseLineColor() {
        return spouseLineColor;
    }

    public String getColorAsWord(int color){
        String word;
        switch(color){
            case Color.DKGRAY:
                word = "Gray";
                break;
            case Color.BLACK:
                word = "Black";
                break;
            case Color.GREEN:
                word = "Green";
                break;
            default:
                word = "Black";
        }
        return word;
    }

    public void setSpouseLineColor(String spouseLineColor) {
        if(spouseLineColor.equals("Gray")){
            this.spouseLineColor = Color.GRAY;
        } else if (spouseLineColor.equals("Green")) {
            this.spouseLineColor = Color.GREEN;
        } else {
            this.spouseLineColor = Color.BLACK;
        }
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
