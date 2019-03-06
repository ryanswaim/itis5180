package com.example.homework03;

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

//Homework Assignment 03
//Group 9
//Rockford Stoller
//Ryan Swaim

public class GetTracksAsync extends AsyncTask<String, Integer, ArrayList<Track>> {

    TrackInfoData trackInfoData;
    ArrayList<Track> tracks = new ArrayList<>();

    public GetTracksAsync(TrackInfoData trackInfoData) {
        this.trackInfoData = trackInfoData;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        trackInfoData.updateProgress(values[0]);
    }

    @Override
    protected ArrayList<Track> doInBackground(String... strings) {
        HttpURLConnection connection = null;

        Log.d("demo","url"+strings[0]);

        try {
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                publishProgress(1);

                tracks = new ArrayList<>();

                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                JSONObject root = new JSONObject(json);
                JSONArray tracksArray = root.getJSONArray("results");

                for(int i = 0; i < tracksArray.length(); i++) {
                    JSONObject trackJson = tracksArray.getJSONObject(i);
                    Track track = new Track();


                    track.title = trackJson.getString("trackName");
                    track.trackPrice = trackJson.getString("trackPrice");
                    track.genre = trackJson.getString("primaryGenreName");
                    track.artist = trackJson.getString("artistName");
                    track.album = trackJson.getString("collectionName");
                    track.albumPrice = trackJson.getString("collectionPrice");
                    //String tempDate = trackJson.getString("releaseDate");
                    //track.date = tempDate.substring(5, 10) + "-" + tempDate.substring(0, 4);
                    track.date = trackJson.getString("releaseDate").substring(0, 10);
                    track.artworkURL = trackJson.getString("artworkUrl100");
                    Log.d("demo","article"+track.toString());

                    tracks.add(track);
                }
            }

        } catch (SocketTimeoutException e) {
            publishProgress(-1);
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

        return tracks;
    }

    @Override
    protected void onPostExecute(ArrayList<Track> tracks) {

        Log.d("demo", "onPostExecute: " + tracks.size());

        trackInfoData.handleTrackData(tracks);
    }

    public static interface TrackInfoData {
        public void handleTrackData(ArrayList<Track> tracks);
        public void updateProgress(int progress);
    }
}
