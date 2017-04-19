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
    

    // Options for Settings
    private boolean showLifeLines = false;
    private boolean showFamilyTreeLines = false;
    private boolean showSpouseLines = false;
    private int lifeLineColor = Color.DKGRAY;
    private int familyTreeLineColor = Color.GREEN;
    private int spouseLineColor = Color.BLACK;
    private String mapType = "Normal";


    /**
     * When called, turns the life lines either on or off.
     */
    public void toggleLifeLines(){
        if(showLifeLines){
            showLifeLines = false;
        }
        else {
            showLifeLines = true;
        }
    }

    /**
     * When called, turns the family tree lines either on or off.
     */
    public void toggleFamilyTreeLines(){
        if(showFamilyTreeLines){
            showFamilyTreeLines = false;
        }
        else {
            showFamilyTreeLines = true;
        }
    }

    /**
     * When called, turns the spouse lines either on or off.
     */
    public void toggleSpouseLines(){
        if(showSpouseLines){
            showSpouseLines = false;
        }
        else {
            showSpouseLines = true;
        }
    }

    /**
     * Changes the color of the life lines
     * @param lifeLineColor English word setting the color (from Spinner)
     */
    public void setLifeLineColor(String lifeLineColor) {
        if(lifeLineColor.equals("Gray")){
            this.lifeLineColor = Color.GRAY;
        } else if (lifeLineColor.equals("Green")) {
            this.lifeLineColor = Color.GREEN;
        } else {
            this.lifeLineColor = Color.BLACK;
        }
    }


    /**
     * Changes the color of the family tree lines
     * @param familyTreeLineColor English word setting the color (from Spinner)
     */
    public void setFamilyTreeLineColor(String familyTreeLineColor) {
        if(familyTreeLineColor.equals("Gray")){
            this.familyTreeLineColor = Color.GRAY;
        } else if (familyTreeLineColor.equals("Green")) {
            this.familyTreeLineColor = Color.GREEN;
        } else {
            this.familyTreeLineColor = Color.BLACK;
        }
    }

    /**
     * Changes the color of the spouse lines
     * @param spouseLineColor English word setting the color (from Spinner)
     */
    public void setSpouseLineColor(String spouseLineColor) {
        if(spouseLineColor.equals("Gray")){
            this.spouseLineColor = Color.GRAY;
        } else if (spouseLineColor.equals("Green")) {
            this.spouseLineColor = Color.GREEN;
        } else {
            this.spouseLineColor = Color.BLACK;
        }
    }

    /**
     * Converts the color (int) into the color (word) needed for the Spinner & English-reading user.
     * @param color int of the color
     * @return English word (String) of the color
     */
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


    public int getSpouseLineColor() {
        return spouseLineColor;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
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
    public int getFamilyTreeLineColor() {
        return familyTreeLineColor;
    }
    
    public int getLifeLineColor() {
        return lifeLineColor;
    }

}
