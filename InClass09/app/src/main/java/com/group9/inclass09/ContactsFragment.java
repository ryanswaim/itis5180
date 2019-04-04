package com.group9.inclass09;


import android.content.Context;
import android.os.Bundle;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//InClass09
//Group 9
//Rockford Stoller
//Ryan Swaim

public class ContactsFragment extends Fragment {

    public static String CONTACT_LIST_KEY = "contact_list_key";

    OnContactsFragmentInteractionListener mListener;

    private ContactAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        getActivity().setTitle("Contacts");

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.contacts_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new ContactAdapter((ArrayList<Contact>) this.getArguments().getSerializable(CONTACT_LIST_KEY));
        recyclerView.setAdapter(adapter);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //clear the array list and repopulate it
                adapter.myData.clear();

                if(dataSnapshot.exists()) {
                    //if the data snapshot exists. meaning the location has a least one expense at start, after removal, or after adding an expense
                    for (DataSnapshot expenseSnap : dataSnapshot.getChildren()) {
                        Contact contact = expenseSnap.getValue(Contact.class);

                        adapter.myData.add(contact);

                        Log.d("demo", contact.toString());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("demo", "onCancelled: databaseError "  + databaseError);
            }
        });

        view.findViewById(R.id.new_contact_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToCreateContactFragment();
            }
        });

        view.findViewById(R.id.logout_imageView_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logoutAttempt();
            }
        });

        return view;
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactsFragment.OnContactsFragmentInteractionListener) {
            mListener = (ContactsFragment.OnContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnContactsFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToCreateContactFragment();
        void logoutAttempt();
    }
}
