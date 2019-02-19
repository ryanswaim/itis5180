package com.example.homework02;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Homework Assignment 02
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity {

    //create a thread pool
    ExecutorService threadPool;

    //create a handler
    Handler handler;

    //create progress dialog/BAR
    ProgressDialog progressDialog;

    //create 2 arrays fpr the thread passwords and the async passwords
    ArrayList<String> threadPasswords = new ArrayList<>();
    ArrayList<String> asyncPasswords = new ArrayList<>();

    //keys for the array list to be received by the generated passwords activity
    public static String THREAD_KEY = "THREAD_PASSWORDS";
    public static String ASYNC_KEY = "ASYNC_PASSWORDS";

    int numThreadPasswords;
    int numAsyncPasswords;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        findViewById(R.id.select_count_thread_seekBar).setEnabled(true);
        findViewById(R.id.select_length_thread_seekBar).setEnabled(true);
        findViewById(R.id.select_count_async_seekBar).setEnabled(true);
        findViewById(R.id.select_length_async_seekBar).setEnabled(true);
        findViewById(R.id.generate_passwords_button).setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title_text);

        numThreadPasswords = -1;
        numAsyncPasswords = -1;

        //initialize the thread pool to 4 threads max
        threadPool = Executors.newFixedThreadPool(2);

        //initialize the handler
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {
                    case GeneratePassword.STATUS_STOP:
                        progressDialog.setProgress(progressDialog.getProgress() + 1);

                        threadPasswords.add((String) msg.obj);

                        Log.d("demo", "Thread Passwords Size: " + threadPasswords.size());
                        Log.d("demo", "Num Thread Passwords: " + numAsyncPasswords);

                        if(progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();

                            if(asyncPasswords.size() == numAsyncPasswords) {
                                Intent intent = new Intent(MainActivity.this, GeneratedPasswordsActivity.class);
                                intent.putExtra(THREAD_KEY, threadPasswords);
                                intent.putExtra(ASYNC_KEY, asyncPasswords);
                                startActivity(intent);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        //on seek bar changes
        //region
        final SeekBar countSeekbarThread = findViewById(R.id.select_count_thread_seekBar);
        final TextView countTextViewThread = findViewById(R.id.show_count_thread_textView);

        countSeekbarThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                countTextViewThread.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final SeekBar lengthSeekbarThread = findViewById(R.id.select_length_thread_seekBar);
        final TextView lengthTextViewThread = findViewById(R.id.show_length_thread_textView);

        lengthSeekbarThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lengthTextViewThread.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final SeekBar countSeekbarAsync = findViewById(R.id.select_count_async_seekBar);
        final TextView countTextViewAsync = findViewById(R.id.show_count_async_textView);

        countSeekbarAsync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                countTextViewAsync.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final SeekBar lengthSeekbarAsync = findViewById(R.id.select_length_async_seekBar);
        final TextView lengthTextViewAsync = findViewById(R.id.show_length_async_textView);

        lengthSeekbarAsync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lengthTextViewAsync.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //endregion

        //generate passwords button
        //region
        findViewById(R.id.generate_passwords_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numThreadPasswords = countSeekbarThread.getProgress();
                int pLengthThread = lengthSeekbarThread.getProgress();

                numAsyncPasswords = countSeekbarAsync.getProgress();
                int pLengthAsync = lengthSeekbarAsync.getProgress();

                threadPasswords = new ArrayList<>();
                asyncPasswords = new ArrayList<>();

                //initialize progress bar(dialog)
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Generating Passwords");
                progressDialog.setMax(numThreadPasswords);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();

                for(int i = 0; i < numThreadPasswords; i++) {
                    threadPool.execute(new GeneratePassword(pLengthThread));
                    //Log.d("demo", "In For loop executing GeneratePassword.");
                }

                for(int i = 0; i < numAsyncPasswords; i++) {
                    new GeneratePasswordsAsync().execute(pLengthAsync);
                }

                //disable buttons on click
                countSeekbarThread.setEnabled(false);
                lengthSeekbarThread.setEnabled(false);
                countSeekbarAsync.setEnabled(false);
                lengthSeekbarAsync.setEnabled(false);
                findViewById(R.id.generate_passwords_button).setEnabled(false);
            }
        });
        //endregion
    }

    //async code
    //region
    class GeneratePasswordsAsync extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPostExecute(String aString) {
            asyncPasswords.add(aString);
            Log.d("demo", "Async Passwords Size: " + asyncPasswords.size());
            Log.d("demo", "Num Async Passwords: " + numAsyncPasswords);

            if(threadPasswords.size() == numThreadPasswords && asyncPasswords.size() == numAsyncPasswords) {
                Intent intent = new Intent(MainActivity.this, GeneratedPasswordsActivity.class);
                intent.putExtra(THREAD_KEY, threadPasswords);
                intent.putExtra(ASYNC_KEY, asyncPasswords);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {
            return Util.getPassword(integers[0]);
        }
    }
    //endregion

    //the runnable for the threads
    //region
    class GeneratePassword implements Runnable{

        private int length;

        public GeneratePassword(int length) {
            this.length = length;
        }

        static final int STATUS_STOP = 0x00;

        @Override
        public void run() {
            //creating a stop message with just a what
            Message stopMessage = new Message();
            stopMessage.what = STATUS_STOP;
            stopMessage.obj = Util.getPassword(length);
            handler.sendMessage(stopMessage);
        }
    }
    //endregion
}
