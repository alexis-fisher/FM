package com.example.alyx.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.alyx.server.R;


public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment loginFragment = fm.findFragmentById(R.id.fragment_container);

        if(loginFragment == null){
            loginFragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container,loginFragment).commit();
        }



    }

    public void loginComplete() {
//        Fragment mapFragment = new MapsFragment();
//        fm.beginTransaction().add(R.id.fragment_container,mapFragment).commit();
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
