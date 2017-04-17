package com.example.alyx.controller;

import android.os.AsyncTask;

import com.example.alyx.model.Model;

import server.proxy.ClientException;

/**
 * Created by Alyx on 4/15/17.
 */

public class ResyncTask extends AsyncTask<Caller, Integer, Boolean> {
    private Model model = Model.instanceOf();
    private String dataReceivedToast;
    private Caller caller;

    protected Boolean doInBackground(Caller... urls) {
        this.caller = urls[0];
        try {
            if(!model.getPeopleFromServer()){
                return false;
            }
            if(!model.getEventsFromServer()){
                return false;
            }
            dataReceivedToast = "Resync complete";
        } catch (ClientException e){
            dataReceivedToast = e.getMessage();
        }
        return true;
    }

    protected void onProgressUpdate(Integer... progress) {
        // progressBar.setProgress(progress[0]);
    }

    protected void onPostExecute(Boolean success) {
        if(dataReceivedToast != null && !dataReceivedToast.equals("")) {
            caller.printToast(dataReceivedToast);
        } else {
            caller.printToast("Internal server error. Could not sync data at this time.");
        }
    }
}
