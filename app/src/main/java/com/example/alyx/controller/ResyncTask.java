package com.example.alyx.controller;

import android.os.AsyncTask;

import com.example.alyx.model.Filter;
import com.example.alyx.model.Model;

import server.proxy.ClientException;

/**
 * Created by Alyx on 4/15/17.
 */

/**
 * Downloads the Event and Person data from the Database
 */
public class ResyncTask extends AsyncTask<Caller, Integer, Boolean> {
    /** Access the Model class */
    private Model model = Model.instanceOf();

    /** The success or failure message (string) */
    private String dataReceivedToast;

    /** Reference to the Activity/Fragment that called this */
    private Caller caller;


    protected Boolean doInBackground(Caller... urls) {
        // Get Activity/Fragment
        this.caller = urls[0];

        // Try to sync data
        try {
            if(!model.getPeopleFromServer()){
                return false;
            }
            if(!model.getEventsFromServer()){
                return false;
            }

            // If no errors, success!
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
        // If no error message, tell the activity/fragment that syce was successful.
        if(dataReceivedToast != null && !dataReceivedToast.equals("")) {
            caller.printToast(dataReceivedToast);
        } else {
            caller.printToast("Internal server error. Could not sync data at this time.");
        }
    }
}
