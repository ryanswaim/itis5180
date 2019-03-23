package com.example.homework04;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class LoadingFragment extends Fragment implements GetRecipesAsync.RecipeInfoData {

    private ProgressBar loadingBar;
    private OnLoadingFragmentInteractionListener mListener;

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Recipes");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        //get the loading progress bar as global variable
        loadingBar = view.findViewById(R.id.loading_progressBar);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.OnMainFragmentInteractionListener) {
            mListener = (LoadingFragment.OnLoadingFragmentInteractionListener) context;
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

    @Override
    public void handleRecipeData(ArrayList<Recipe> recipes) {
        //receive the recipes list from GetRecipesAsync and pass it on to the MainActivity
        mListener.goToShowRecipes(recipes);
    }

    @Override
    public void updateProgress(int progress) {
        //update the progress of the loading progress bar
        loadingBar.setProgress(progress);
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        //set the max progress for the loading progress bar
        loadingBar.setMax(maxProgress);
    }

    public interface OnLoadingFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToShowRecipes(ArrayList<Recipe> recipes);
    }
}
