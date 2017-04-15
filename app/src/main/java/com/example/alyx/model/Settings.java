package com.example.alyx.model;

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

    // Colors
    final String red = "RED";
    final String green = "GREEN";
    final String blue = "BLUE";

    // Options for Settings
    private boolean showLifeLines;
    private boolean showFamilyTreeLines;
    private boolean showSpouseLines;
    private String lifeLineColor;
    private String familyTreeLineColor;
    private String spouseLineColor;
    private String mapType;


    public void onOffLifeLines(){
        if(showLifeLines){
            showLifeLines = false;
        }
        else {
            showLifeLines = true;
        }
     //   model.
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
}
