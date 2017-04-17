package com.example.alyx.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Model;
import com.example.alyx.model.Settings;
import com.example.alyx.server.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;



import server.model.Event;
import server.model.Person;
import server.proxy.ServerProxy;
import server.result.EventResult;
import server.result.PersonResult;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Event[] events;

    private Model model = Model.instanceOf();
    private Settings settings = Settings.instanceOf();

    // Event information displayed at the bottom
    private TextView mEventOwner;
    private TextView mEventInfo;

    private Person owner;
    private String ownerName;
    private String info;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //Change the ImageView image source depends on menu item click
                    case R.id.miSearch:
                        printToast("Search Clicked!");
//                        toSearchActivity();
                        return true;
                    case R.id.miFilter:
                        printToast("Filter Clicked!");
//                        toFilterActivity();
                        return true;
                    case R.id.miSettings:
                        printToast("Settings Clicked!");
                        toSettingsActivity();
                        return true;
                }
                //If above criteria does not meet then default is false;
                return false;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // Set up textviews below map!
        mEventOwner = (TextView) findViewById (R.id.eventOwnerName);
        mEventInfo = (TextView) findViewById (R.id.eventTypeAndLocationAndYear);
        mEventOwner.setText(getEventOwner());
        mEventInfo.setText(getEventInfo());


        mEventOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPersonActivity();
            }
        });
    }
//
//    private void sortEventsByPerson(){
//        // Sort events by person for use by person activity
//        events = model.getEvents();
//        for (Event e : events) {
//            // Get person associated with event
//            String personID = e.getPerson();
//            if (eventsByPerson.containsKey(personID)) {
//                eventsByPerson.get(personID).add(e);
//            } else {
//                eventsByPerson.put(personID, new TreeSet<Event>());
//                eventsByPerson.get(personID).add(e);
//            }
//        }
//    }
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
        addEventMarkers(this.events);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                new EventInfo().execute(marker.getTitle());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });


    }

    private void toFilterActivity(){
        Intent intent = new Intent(MapActivity.this, FilterActivity.class);
        startActivity(intent);
    }

    private void toSearchActivity(){
        Intent intent = new Intent(MapActivity.this, FilterActivity.class);
        startActivity(intent);
    }

    private void toSettingsActivity(){
        Intent intent = new Intent(MapActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void toPersonActivity() {
        if (owner != null && !owner.getPersonID().equals("")) {
            Intent intent = new Intent(MapActivity.this, PersonActivity.class);
            intent.putExtra("personID", owner.getPersonID());
            startActivity(intent);
        }
    }


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

    @Override
    public void onResume(){
        super.onResume();
        if(mMap != null){
            mMap.clear();
            setMapType();
            addEventMarkers(model.getEvents());
        }
    }

    public void setMapType(){
        if(settings.getMapType().equals("Normal")){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if(settings.getMapType().equals("Hybrid")){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if(settings.getMapType().equals("Satellite")){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if(settings.getMapType().equals("Terrain")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
    }

    public float setMarkerColor(String eventType){
        float color = 0;
        switch (eventType.toLowerCase()) {
            case "birth":
                color = BitmapDescriptorFactory.HUE_BLUE;
                break;
            case "death":
                color = BitmapDescriptorFactory.HUE_CYAN;
                break;
            case "marriage":
                color = BitmapDescriptorFactory.HUE_ROSE;
                break;
            case "census":
                color = BitmapDescriptorFactory.HUE_YELLOW;
                break;
            case "christening":
                color = BitmapDescriptorFactory.HUE_ORANGE;
                break;
            case "graduated, finally":
                color = BitmapDescriptorFactory.HUE_AZURE;
                break;
            case "finally started a job that isn't entry-level":
                color = BitmapDescriptorFactory.HUE_GREEN;
                break;
            case "started volunteer work because court required it":
                color = BitmapDescriptorFactory.HUE_RED;
                break;
            case "replaced futon with first real couch":
                color = BitmapDescriptorFactory.HUE_MAGENTA;
                break;
            case "kept desk plant alive for one month":
                color = BitmapDescriptorFactory.HUE_YELLOW;
                break;
            case "actually went to the dentist":
                color = BitmapDescriptorFactory.HUE_ORANGE;
                break;
            default:
                color = BitmapDescriptorFactory.HUE_VIOLET;
                break;

        }
        return color;
    }

    private void setEventInfo(Event e){
        info = e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")";
        mEventInfo.setText(info);
    }

    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
    private void addEventMarkers(Event[] events){
        for(Event e : events) {

            if(e.getEventType() == null || e.getEventType().equals("") || e.getEventType().length() <= 0){
                e.setEventType("birth");
            }

            // Add a marker and move the camera
            LatLng coordinates = new LatLng(Float.parseFloat(e.getLatitude()), Float.parseFloat(e.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(coordinates).title(e.getEventID()).icon(BitmapDescriptorFactory.defaultMarker(setMarkerColor(e.getEventType()))));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

            // If event is a marriage, and the marriage line is on, draw lines!
            if(e.getEventType().equals("marriage") && settings.isShowSpouseLines()){
                new GetSpouseBirthPlace().execute(e);
            }
        }
        model.setEvents(events);

    }

    private Event findSpouseBirthPlace(String spouseID){
        Event[] events = model.getEvents();
        for (Event e : events){
            if(e.getEventType().equals("birth") && e.getPerson().equals(spouseID)){
                return e;
            }
        }
        return null;
    }

    private void drawSpouseLine(LatLng marriageCoords,LatLng spouseBirthCoords){
        mMap.addPolyline(new PolylineOptions()
                .add(marriageCoords,spouseBirthCoords)
                .width(3)
                .color(settings.getSpouseLineColor()));
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
                if(event.getEventType() == null || event.getEventType().equals("") || event.getEventType().length() <= 0){
                    event.setEventType("birth");
                }
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

    public class GetSpouseBirthPlace extends AsyncTask<Event, Integer, String> {
        private PersonResult personResult = new PersonResult();
        private ServerProxy mProxy = ServerProxy.server();
        private Person person;
        private Event marriage;
        private String personID, spouseID;

        protected String doInBackground(Event... urls) {
            // Get person ID

            this.marriage = urls[0];

            this.personID = marriage.getPerson();

            // Get person BY ID
            personResult = mProxy.person(personID);

            // If no error message, continue.
            if(personResult.getMessage() == null || personResult.getMessage().equals("")){
                person = personResult.getPerson();

                spouseID = person.getSpouse();
                return spouseID;
            } else {  // if error message, return null
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            // progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(String success) {
            if(success != null){
                Event birth = findSpouseBirthPlace(spouseID);
                if(birth != null){
                    LatLng marriageCoords = new LatLng(Float.parseFloat(marriage.getLatitude()), Float.parseFloat(marriage.getLongitude()));
                    LatLng spouseBirth = new LatLng(Float.parseFloat(birth.getLatitude()), Float.parseFloat(birth.getLongitude()));
                    drawSpouseLine(marriageCoords,spouseBirth);
                }
            } else {
                printToast(personResult.getMessage());
            }
        }
    }
}
