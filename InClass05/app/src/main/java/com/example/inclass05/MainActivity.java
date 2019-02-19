package com.example.inclass05;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

//In-Class Assignment 05
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity implements GetImageFromURL.BitmapImageData {

    String[] keywords;
    ArrayList<String> imageURLs = new ArrayList<>();
    ImageView pictureImageView;
    ImageView nextButtonImageView;
    ImageView prevButtonImageView;
    int currentImage = 0;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title_text);

        nextButtonImageView = findViewById(R.id.next_imageView);
        prevButtonImageView = findViewById(R.id.prev_imageView);
        nextButtonImageView.setEnabled(false);
        prevButtonImageView.setEnabled(false);

        progressBar = findViewById(R.id.loading_next_progressBar);

        new GetKeywords().execute("http://dev.theappsdr.com/apis/photos/keywords.php");

        final TextView searchKeywordTextView = findViewById(R.id.search_keyword_textView);
        pictureImageView = findViewById(R.id.picture_imageView);

        //go button
        //region
        findViewById(R.id.go_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected() && keywords != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle(R.string.choose_a_keyword_text)
                            .setItems(keywords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestParams url;
                            searchKeywordTextView.setText(keywords[which]);
                            url = new RequestParams()
                                    .addParameter("keyword", keywords[which]);
                            String urlStr = url.getEncodedUrl("http://dev.theappsdr.com/apis/photos/index.php");
                            new GetImageURLsAsync().execute(urlStr);

                        }
                    });

                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "Is not connected", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //endregion

        //next button
        //region
        nextButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImage == imageURLs.size() - 1) {
                    currentImage = 0;
                } else {
                    currentImage++;
                }

                new GetImageFromURL(MainActivity.this).execute(imageURLs.get(currentImage));
                //new GetImageAsync(pictureImageView).execute(imageURLs.get(currentImage));
            }
        });
        //endregion

        //previous button
        //region
        prevButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImage == 0) {
                    currentImage = imageURLs.size() - 1;
                } else {
                    currentImage--;
                }

                new GetImageFromURL(MainActivity.this).execute(imageURLs.get(currentImage));
                //new GetImageAsync(pictureImageView).execute(imageURLs.get(currentImage));
            }
        });
        //endregion
    }

    //is connected function
    //region
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
    //endregion

    //get keywords Async
    //region
    class GetKeywords extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                keywords = s.split(";");
                //Log.d("demo", keywords[0]);
                //Log.d("demo", keywords[keywords.length - 1]);
                //Log.d("demo", keywords.length + "");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values[0] == -1) {
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                }
            } catch (SocketTimeoutException e) {
                publishProgress(-1);
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.d("demo", "Error 1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("demo", "Error 2");
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
    }
    //endregion

    //get image urls Async
    //region
    class GetImageURLsAsync extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            currentImage = 0;
            imageURLs.clear();
            if(s.size() > 0) {
                imageURLs.addAll(s);
                new GetImageFromURL(MainActivity.this).execute(imageURLs.get(currentImage));
                //new GetImageAsync(pictureImageView).execute(imageURLs.get(currentImage));

                if(imageURLs.size() > 1) {
                    nextButtonImageView.setEnabled(true);
                    prevButtonImageView.setEnabled(true);
                } else {
                    nextButtonImageView.setEnabled(false);
                    prevButtonImageView.setEnabled(false);
                }
            } else {
                Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
                //pictureImageView.setImageDrawable(getDrawable(R.drawable.ic_launcher_foreground));
                pictureImageView.setImageBitmap(null);
                nextButtonImageView.setEnabled(false);
                prevButtonImageView.setEnabled(false);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values[0] == -1) {
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        //stringBuilder.append(line);
                        result.add(line);
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
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
    }
    //endregion

    //BitmapImageData functions for recieving bitmap image data from the GetImageFromURL java file/AsyncTask
    //region
    @Override
    public void handleBitmapData(Bitmap bitmap) {
        if(bitmap != null) {
            pictureImageView.setImageBitmap(bitmap);

        } else {
            //pictureImageView.setImageDrawable(getDrawable(R.drawable.ic_launcher_foreground));
            pictureImageView.setImageBitmap(null);
            Toast.makeText(MainActivity.this,"No Images Found At URL", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateProgress(int progress) {
        if(progress == -1) {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            pictureImageView.setImageBitmap(null);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    //endregion
}
