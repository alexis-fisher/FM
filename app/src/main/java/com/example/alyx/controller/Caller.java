package com.example.alyx.controller;

import android.support.v4.app.FragmentManager;

/**
 * Created by Alyx on 4/15/17.
 */

public interface Caller {
    public void printToast(String toast);
    public void resync(boolean goToMapNext);
    public FragmentManager getThisFragmentManager();
}

