package com.example.alyx.model;

/**
 * Created by Alyx on 4/14/17.
 */

public class Filter {
    private Filter(){}

    public static Filter instanceOf(){
        if(_instance == null){
            _instance = new Filter();
        }
        return _instance;
    }

    private static Filter _instance;
}
