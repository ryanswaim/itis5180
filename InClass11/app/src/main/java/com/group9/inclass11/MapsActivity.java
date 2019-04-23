package com.group9.inclass11;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//In Class Assignment 11
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location location;
    private ArrayList<LatLng> list_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String json = null;
        try {
            InputStream is = getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            //Log.d("demo", json + "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();

        location = gson.fromJson(json, Location.class);

        if(location != null) {
            for (LocPoint locPoint : location.getPoints()) {
                Log.d("demo", "Location lat: " + locPoint.getLatitude() + " Location lng: " + locPoint.getLongitude());
            }
        }

        //get all LatLng points
        list_points = new ArrayList<>();
        for(LocPoint point : location.getPoints()) {
            list_points.add(new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude())));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //add starting marker
        LocPoint startPoint = location.getPoints().get(0);
        LatLng startLatLng = new LatLng(Double.parseDouble(startPoint.getLatitude()), Double.parseDouble(startPoint.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(startLatLng).title("Start Location"));

        //add end marker
        LocPoint endPoint = location.getPoints().get(location.getPoints().size() - 1);
        LatLng endLatLng = new LatLng(Double.parseDouble(endPoint.getLatitude()), Double.parseDouble(endPoint.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(endLatLng).title("End Location"));

        //put points of polyline in polyline and add it to map and store it in a polyline object
        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(list_points));

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //set all points in the bounds for the camera
                for(LatLng p : list_points) {
                    builder.include(p);
                }

                //build the bounds for the camera
                LatLngBounds bounds = builder.build();

                //position the map's camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });
    }
}
