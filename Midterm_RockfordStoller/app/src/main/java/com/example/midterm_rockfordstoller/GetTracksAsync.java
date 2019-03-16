package com.example.midterm_rockfordstoller;

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

                tracks = new ArrayList<>();

                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                //Log.d("demo", json);

                JSONObject root = new JSONObject(json);
                JSONArray tracksArray = root.getJSONObject("message").getJSONObject("body").getJSONArray("track_list");

                Log.d("demo", tracksArray.length() + "");

                for(int i = 0; i < tracksArray.length(); i++) {
                    JSONObject trackJson = tracksArray.getJSONObject(i).getJSONObject("track");
                    //Log.d("demo", trackJson.toString());
                    Track track = new Track();


                    track.trackName = trackJson.getString("track_name");
                    track.albumName = trackJson.getString("album_name");
                    track.artistName = trackJson.getString("artist_name");
                    track.updateTime = trackJson.getString("updated_time");
                    track.trackShareURL = trackJson.getString("track_share_url");
                    //Log.d("demo","track: "+track.toString());

                    tracks.add(track);
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

        return tracks;
    }

    @Override
    protected void onPostExecute(ArrayList<Track> tracks) {

        Log.d("demo", "onPostExecute: " + tracks.size());

        trackInfoData.handleTrackData(tracks);
    }

    public interface TrackInfoData {
        void handleTrackData(ArrayList<Track> tracks);
        void updateProgress(int progress);
    }
}
