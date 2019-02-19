package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

//In-Class Assignment 05
//Group 9
//Rockford Stoller
//Ryan Swaim

public class GetImageFromURL extends AsyncTask<String, Integer, Bitmap> {

    BitmapImageData bitmapImageData;
    Bitmap bitmap;

    public GetImageFromURL(BitmapImageData bitmapImageData) {
        this.bitmapImageData = bitmapImageData;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        bitmapImageData.updateProgress(values[0]);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection = null;
        bitmap = null;

        try {

            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                publishProgress(1);
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
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
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //naive approach
        //activity.handleData(strings);

        bitmapImageData.handleBitmapData(bitmap);
    }

    public static interface BitmapImageData {
        public void handleBitmapData(Bitmap bitmap);
        public void updateProgress(int progress);
    }
}
