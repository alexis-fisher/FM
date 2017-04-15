package com.example.alyx.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.alyx.server.R;


public class MainActivity extends FragmentActivity {

    private boolean loginComplete = false;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        Fragment loginFragment = fm.findFragmentById(R.id.fragment_container);

        if(loginFragment == null){
            loginFragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container,loginFragment).commit();
        }



    }

    public void loginComplete() {
        this.loginComplete = true;
        Fragment mapFragment = new MapFragment();
        fm.beginTransaction().add(R.id.fragment_container,mapFragment).commit();
    }
}
