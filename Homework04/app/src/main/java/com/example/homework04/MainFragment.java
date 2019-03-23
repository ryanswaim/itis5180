package com.example.homework04;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainFragment extends Fragment {

    private ArrayList<String> ingredients_list = new ArrayList<>();
    private OnMainFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    Button searchButton;
    EditText dishEditText;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Recipe Puppy");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //find the recycler view and set fixed size
        recyclerView = view.findViewById(R.id.ingredients_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new IngredientAdapter(ingredients_list);
        recyclerView.setAdapter(adapter);

        //if ingredients_list is empty
        //*this is to stop extras from being added when the fragment
        //*is re-inflated when it returns from the ShowRecipesFragment
        if(ingredients_list.size() == 0) {
            //set initial item in recycler view and notify the adapter
            ingredients_list.add("");
            adapter.notifyDataSetChanged();
        }

        //find the dish edit text and search button
        dishEditText = view.findViewById(R.id.dish_editText);
        searchButton = view.findViewById(R.id.search_button);

        //setup the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the user has input a dish
                if(dishEditText.getText().length() > 0) {
                    //create a url creator
                    RequestParams url = new RequestParams();

                    //use string builder
                    StringBuilder stringBuilder = new StringBuilder();

                    //if the first item in the ingredients list has input
                    if(ingredients_list.get(0).length() > 0) {
                        //append the ingredient
                        stringBuilder.append(ingredients_list.get(0));
                    }
                    //then for all the remaining ingredients they need a ',' between them
                    for(int i = 1; i < ingredients_list.size(); i++) {
                        //if the item has input
                        if(ingredients_list.get(i).length() > 0) {
                            //append ',' and then the item
                            stringBuilder.append("," + ingredients_list.get(i));
                        }
                    }

                    //add the dish parameter to the url
                    url.addParameter("q", dishEditText.getText().toString());
                    //add the ingredients parameter to the url
                    url.addParameter("i", stringBuilder.toString());

                    //create the full url and store it in a new string
                    String urlStr = url.getEncodedUrl("http://www.recipepuppy.com/api/");
                    //Log.d("demo", "url: " + urlStr);

                    //trigger the loading fragment within the MainActivity
                    mListener.goToLoadingFragment(urlStr);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpenseAppFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMainFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToLoadingFragment(String url);
    }
}
