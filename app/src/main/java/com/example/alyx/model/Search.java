package com.example.alyx.model;

/**
 * Created by Alyx on 4/14/17.
 */

public class Search {
    private Search(){}

    public static Search instanceOf(){
        if(_instance == null){
            _instance = new Search();
        }
        return _instance;
    }

    private static Search _instance;
}
