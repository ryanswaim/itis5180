package com.group9.inclass12;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class EditTripFragment extends Fragment {

    public static String LOCATION_LIST_KEY = "location_list_key";

    private OnEditTripFragmentInteractionListener mListener;

    private LocationAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private MainActivity mainActivity = null;

    public EditTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_trip, container, false);

        getActivity().setTitle("Locations");

        mainActivity = (MainActivity) getActivity();

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.locations_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new LocationAdapter((ArrayList<Location>) this.getArguments().getSerializable(LOCATION_LIST_KEY));
        recyclerView.setAdapter(adapter);

        //add note button
        //region
        view.findViewById(R.id.goTo_add_location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to add note fragment
                //mListener.goToAddTripFragment();
            }
        });
        //endregion

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditTripFragmentInteractionListener) {
            mListener = (OnEditTripFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditTripFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEditTripFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddLocationFragment();
    }
}
