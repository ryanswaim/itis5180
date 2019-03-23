package com.example.homework04;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

//Homework 04
//Group 9
//Rockford Stoller
//Ryan Swaim

public class GetRecipesAsync extends AsyncTask<String, Integer, ArrayList<Recipe>> {

    RecipeInfoData recipeInfoData;
    ArrayList<Recipe> recipes = new ArrayList<>();

    public GetRecipesAsync(RecipeInfoData recipeInfoData) {
        this.recipeInfoData = recipeInfoData;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        recipeInfoData.updateProgress(values[0]);
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... strings) {
        HttpURLConnection connection = null;

        //log url
        Log.d("demo", "url: " + strings[0]);

        try {
            //create URL with url string received
            URL url = new URL(strings[0]);

            //create connection
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //if connection is ok
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //initialize recipe array list
                recipes = new ArrayList<>();

                //collect json stream
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                //Log.d("demo", json);

                //create root json object and get the array of recipes (results)
                JSONObject root = new JSONObject(json);
                JSONArray recipesArray = root.getJSONArray("results");

                //Log.d("demo", "JSONArray length: " + recipesArray.length());

                //set the max progress for the loading bar in the loading fragment
                recipeInfoData.setMaxProgress(recipesArray.length());

                //for each recipe in the json stream
                for (int i = 0; i < recipesArray.length(); i++) {
                    //get the recipe json object and create a new recipe object
                    JSONObject trackJson = recipesArray.getJSONObject(i);
                    Recipe recipe = new Recipe();
                    //Log.d("demo", trackJson.toString());

                    //assign all correct information from json object to the recipe object
                    recipe.title = trackJson.getString("title");
                    recipe.image = trackJson.getString("thumbnail");
                    recipe.ingredients = trackJson.getString("ingredients");
                    recipe.url = trackJson.getString("href");
                    //Log.d("demo","recipe: " + recipe.toString());

                    //add the recipe object to the recipe array list
                    recipes.add(recipe);
                    //publish the progress made to the loading fragment
                    publishProgress(i + 1);
                }
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            //Log.d("demo", "ERORR1: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            //Log.d("demo", "ERORR2: " + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            //Log.d("demo", "ERORR2: " + e.toString());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {

        //log size of recipes
        Log.d("demo", "onPostExecute recipes.size(): " + recipes.size());

        //pass recipe list back to loading fragment
        //*(which then passes it to the MainActivity to start ShowRecipesFragment)
        recipeInfoData.handleRecipeData(recipes);
    }

    public interface RecipeInfoData {
        void handleRecipeData(ArrayList<Recipe> recipes);

        void updateProgress(int progress);

        void setMaxProgress(int maxProgress);
    }
}