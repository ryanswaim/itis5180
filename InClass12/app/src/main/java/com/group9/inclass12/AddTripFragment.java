package com.group9.inclass12;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class AddTripFragment extends Fragment {

    OnAddTripFragmentInteractionListener mListener;

    String tripName = "";

    public AddTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_search, container, false);

        getActivity().setTitle("Add Trip");

        final EditText tripNameEditText = view.findViewById(R.id.trip_name_editText);
        final EditText citySearchEditText = view.findViewById(R.id.city_search_editText);
        Button searchButton = view.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripNameEditText.length() > 0 && citySearchEditText.length() > 0) {

                    tripName = tripNameEditText.getText().toString();

                    //https://maps.googleapis.com/maps/api/place/findplacefromtext/json?
                    RequestParams urlString = new RequestParams();
                    urlString.addParameter("input", citySearchEditText.getText().toString());
                    urlString.addParameter("inputtype", "textquery");
                    urlString.addParameter("fields", "name,geometry");
                    urlString.addParameter("key", "AIzaSyCrQ9wifukgByzMZDYgA4y27DvFMDaRzdE");
                    urlString.addParameter("type", "city_hall");

                    new GetDestinationCity().execute(urlString.getEncodedUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json"));
                }
            }
        });

        return view;
    }

    class GetDestinationCity extends AsyncTask<String, Integer, Location> {

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            mListener.cityFound(location, tripName);
            //trip.destinationCity = location;
            //Log.d("demo", "onPostExecute: " + trip.destinationCity.name);
        }

        @Override
        protected Location doInBackground(String... strings) {

            HttpURLConnection connection = null;
            Location result = null;
            try {
                Log.d("demo", "doInBackground: " + strings[0]);
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = null;
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    JSONObject root = new JSONObject(json);
                    JSONArray candidatesArray = root.getJSONArray("candidates");

                    Log.d("demo", candidatesArray.length() + "");

                    for(int i = 0; i < candidatesArray.length(); i++) {
                        Location location = new Location();

                        JSONObject locationJson = candidatesArray.getJSONObject(i);

                        location.name = locationJson.getString("name");
                        Log.d("demo", "name: " + location.name);

                        locationJson = candidatesArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");

                        location.lat = locationJson.getString("lat");
                        location.lng = locationJson.getString("lng");
                        Log.d("demo", "lat: " + location.lat + " lng: " + location.lng);

                        result = location;
                    }
                }
            } catch (SocketTimeoutException e) {
                publishProgress(-1);
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.d("demo", "Error 3");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("demo", "Error 4");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddTripFragmentInteractionListener) {
            mListener = (OnAddTripFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddTripFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddTripFragmentInteractionListener {
        // TODO: Update argument type and name
        void cityFound(Location location, String tripName);
    }
}
