package com.example.homework04;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener, LoadingFragment.OnLoadingFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create expense app fragment with ArrayList<Expense> that is store in MainActivity as an argument
        //region
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainFragment, "main_fragment")
                .commit();
        //endregion
    }

    @Override
    public void goToLoadingFragment(String url) {

        //
        LoadingFragment loadingFragment = new LoadingFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loadingFragment, "loading_fragment")
                .addToBackStack(null)
                .commit();

        new GetRecipesAsync(loadingFragment).execute(url);

        return;
    }

    @Override
    public void goToShowRecipes(ArrayList<Recipe> recipes) {

        getSupportFragmentManager().popBackStack();

        //if recipes were found the returning array list will not be empty
        if(recipes.size() > 0) {
            //load the recipes fragment
            ShowRecipesFragment showRecipesFragment = new ShowRecipesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ShowRecipesFragment.RECIPES_LIST_KEY, recipes);
            showRecipesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, showRecipesFragment, "show_recipes_fragment")
                    .addToBackStack(null)
                    .commit();
        } else {
            //else no recipes were found
            Toast.makeText(MainActivity.this, "No Recipes Were Found", Toast.LENGTH_SHORT).show();
        }
    }

    //unnecessary
    //region
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    //endregion
}
