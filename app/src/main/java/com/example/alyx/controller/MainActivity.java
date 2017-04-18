package com.example.alyx.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.alyx.server.R;


public class MainActivity extends FragmentActivity {
    private boolean loginComplete = false;
    private Toolbar toolbar;
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

        // Setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map");
        toolbar.setTitleTextColor(Color.WHITE);
        if(loginComplete) {
            toolbar.inflateMenu(R.menu.menu_main);
        }


    }

    public void loginComplete() {
        loginComplete = true;
//        Fragment mapFragment = new MapsFragment();
//        fm.beginTransaction().add(R.id.fragment_container,mapFragment).commit();
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);

        toolbar.inflateMenu(R.menu.menu_main);

    }
}
