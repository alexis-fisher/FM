package com.example.alyx.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;


public class MainActivity extends FragmentActivity {
    /** Login complete flag - set to true when login is complete... */
    private boolean loginComplete = false;

    /** Toolbar (on top of screen) */
    private Toolbar toolbar;

    /** Access model class */
    private Model model = Model.instanceOf();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set UI to the MainActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model.setInflate(false);

        // Set up fragment manager
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

    /**
     * Sets login complete flag, activates toolbar and transitions to map
     */
    public void loginComplete() {
        // Set loginComplete flag to TRUE!
        loginComplete = true;

        model.setInflate(true);

        // Go to map
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);

        // Add in toolbar
        toolbar.inflateMenu(R.menu.menu_main);

    }
}
