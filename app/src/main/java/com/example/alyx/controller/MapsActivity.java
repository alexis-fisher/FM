package com.example.alyx.controller;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import server.model.Event;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Model model = Model.instanceOf();
    private Event[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(mapFragment == null){
            mapFragment = new MapFragment();
            fm.beginTransaction().add(R.id.map,mapFragment).commit();
        }
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

        events = model.getEvents();
        for(Event e : events){
            addMarker(e);
        }

    }

    private void addMarker(Event e){
        LatLng eventCoordinates = new LatLng(Integer.parseInt(e.getLatitude()), Integer.parseInt(e.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(eventCoordinates).title(e.getEventType()));

        // Move camera to new marker
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(eventCoordinates));
    }
}
