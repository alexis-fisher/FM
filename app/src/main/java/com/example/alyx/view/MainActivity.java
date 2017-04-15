package com.example.alyx.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.alyx.server.R;

import java.io.IOException;


public class MainActivity extends FragmentActivity {
    public interface Caller {}

    private boolean loginComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }

    }

    public void loginComplete() {
        this.loginComplete = true;
        assert(!loginComplete);
    }
}
