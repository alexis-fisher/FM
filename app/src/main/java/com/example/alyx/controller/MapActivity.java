package com.example.alyx.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import server.model.Event;
import server.model.Person;
import server.proxy.ServerProxy;
import server.result.EventResult;
import server.result.PersonResult;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Event[] events;
    private Model model = Model.instanceOf();

    private TextView mEventOwner;
    private TextView mEventInfo;

    private Person owner;
    private String ownerName;
    private String info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mEventOwner = (TextView) findViewById (R.id.eventOwnerName);
        mEventInfo = (TextView) findViewById (R.id.eventTypeAndLocationAndYear);
        mEventOwner.setText(getEventOwner());
        mEventInfo.setText(getEventInfo());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.events = model.getEvents();
        for (Event e : events) {
            addEventMarker(e);
        }
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                new EventInfo().execute(marker.getTitle());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });

//        mEventOwner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toPersonActivity();
//            }
//        });
    }

//    private void toPersonActivity(){
//        Intent intent = new Intent(MapActivity.this, PersonActivity.class);
//        intent.putExtra("personID",owner.getPersonID());
//        startActivity(intent);
//    }

    private String getEventOwner(){
        if(ownerName == null || ownerName.equals("")){
            ownerName = "";
        }
        return ownerName;
    }
    private String getEventInfo(){
        if(info == null || info.equals("")){
            info = "";
        }
        return info;
    }

    private void setPerson(Person p){
        owner = p;
        ownerName = owner.getFirstName() + " " + owner.getLastName();
        mEventOwner.setText(ownerName);

    }

    private void setEventInfo(Event e){
        info = e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")";
        mEventInfo.setText(info);
    }

    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
    private void addEventMarker(Event e){
        // Add a marker in Sydney and move the camera
        LatLng coordinates = new LatLng(Float.parseFloat(e.getLatitude()), Float.parseFloat(e.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(coordinates).title(e.getEventID()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

    }

    public class EventInfo extends AsyncTask<String, Integer, Person> {
        private EventResult eventResult = new EventResult();
        private PersonResult personResult = new PersonResult();
        private ServerProxy mProxy = ServerProxy.server();
        private Event event;
        private String eventID;
        private boolean eventGotten = false;

        protected Person doInBackground(String... urls) {
            // Get event ID
            this.eventID = urls[0];

            // Get event BY ID
            eventResult = mProxy.event(eventID);

            // If no error message, continue.
            if(eventResult.getMessage() == null || eventResult.getMessage().equals("")){
                eventGotten = true;
                event = eventResult.getEvent();

                // Get person associated with event
                personResult = mProxy.person(event.getPerson());

                // If no error message, return the person!
                if(personResult.getMessage() == null || personResult.getMessage().equals("")) {
                    return personResult.getPerson();
                }

                // if error message, return null
                else {
                    return null;
                }
            } else {  // if error message, return null
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            // progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Person success) {
            if(success != null){
                setPerson(success);
                setEventInfo(event);
            } else {
                if(eventGotten) {
                    printToast(personResult.getMessage());
                } else {
                    printToast(eventResult.getMessage());
                }
            }
        }
    }
}
