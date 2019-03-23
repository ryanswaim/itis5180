package com.example.homework04;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class ShowRecipesFragment extends Fragment {

    public static String RECIPES_LIST_KEY = "recipes_array_list";
    private ArrayList<Recipe> displayRecipesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    Button finishButton;

    public ShowRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Recipes");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_recipes, container, false);

        //find the recycler view and set fixed size
        recyclerView = view.findViewById(R.id.recipe_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) myLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new RecipeAdapter((ArrayList<Recipe>) this.getArguments().getSerializable(RECIPES_LIST_KEY));
        recyclerView.setAdapter(adapter);

        //set up DividerItemDecoration to add borders to the right and bottom of the recycler view
        //region

        //endregion

        //create the finish button
        finishButton = view.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
