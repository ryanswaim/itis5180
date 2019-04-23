package com.group9.inclass12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TripsListFragment.OnTripsListFragmentInteractionListener, AddTripFragment.OnAddTripFragmentInteractionListener,
        EditTripFragment.OnEditTripFragmentInteractionListener {

    ArrayList<Trip> trips = new ArrayList<>();

    TripsListFragment tripsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tripsListFragment = new TripsListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TripsListFragment.TRIP_LIST_KEY, trips);
        tripsListFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tripsListFragment, "trips_list_fragment")
                .commit();
    }

    public void goToEditTripFragment(int index) {

        EditTripFragment editTripFragment = new EditTripFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EditTripFragment.LOCATION_LIST_KEY, trips.get(index).locations);
        editTripFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, editTripFragment, "edit_trip_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToAddTripFragment() {

        AddTripFragment addTripFragment = new AddTripFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, addTripFragment, "add_trip_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cityFound(Location location, String tripName) {
        Trip trip = new Trip();
        trip.tripName = tripName;
        trip.tripDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        trip.destinationCity = location;

        trips.add(trip);
        tripsListFragment.notifyAdapter();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToAddLocationFragment() {
        //ADD LATER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
}
