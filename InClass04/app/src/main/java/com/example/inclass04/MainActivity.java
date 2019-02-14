package com.example.inclass04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Group 9
//Rockford Stoller
//Ryan Swain

public class MainActivity extends AppCompatActivity {

    ProgressBar loadingBar;
    ImageView pictureView;
    Button asyncButton;

    //Part 2
    ExecutorService threadPool;
    Handler handler;
    Button threadButton;

    Bitmap getImageBitmap(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title_text);

        loadingBar = findViewById(R.id.loading_progressBar);
        pictureView = findViewById(R.id.picture_imageView);
        asyncButton = findViewById(R.id.async_button);

        asyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncLoadImage().execute("https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg");
            }
        });

        //Part 2
        threadPool = Executors.newFixedThreadPool(1);

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ThreadLoadImage.STATUS_START:
                        loadingBar.setVisibility(View.VISIBLE);
                        pictureView.setVisibility(View.INVISIBLE);
                        break;
                    case ThreadLoadImage.STATUS_DONE:
                        pictureView.setImageBitmap((Bitmap)msg.obj);
                        pictureView.setVisibility(View.VISIBLE);
                        loadingBar.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        threadButton = findViewById(R.id.thread_button);

        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new ThreadLoadImage("https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg");
                threadPool.execute(runnable);
            }
        });
    }

    class AsyncLoadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            pictureView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            pictureView.setImageBitmap(bitmap);
            pictureView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myBitmap = getImageBitmap(strings);

            return myBitmap;
        }
    }

    class ThreadLoadImage implements Runnable{

        static final int STATUS_START = 0x00;
        static final int STATUS_DONE = 0x01;

        String imageURL;

        public ThreadLoadImage(String string) {
            imageURL = string;
        }

        @Override
        public void run() {
            Message message1 = new Message();
            message1.what = STATUS_START;
            handler.sendMessage(message1);

            Bitmap myBitmap = getImageBitmap(imageURL);

            Message message2 = new Message();
            message2.what = STATUS_DONE;
            message2.obj = myBitmap;
            handler.sendMessage(message2);
        }
    }
}
