package com.example.homework02;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //create a thread pool
    ExecutorService threadPool;

    //create a handler
    Handler handler;

    //create progress dialog/BAR
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title_text);

        //initialize the thread pool to 4 threads max
        threadPool = Executors.newFixedThreadPool(2);

        //initialize the handler
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {
                    case GeneratePassword.STATUS_STOP:
                        progressDialog.setProgress(progressDialog.getProgress() + 1);
                        //Toast.makeText(MainActivity.this, "" + msg.obj, Toast.LENGTH_SHORT).show();
                        if(progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();
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
                int pCount = countSeekbarThread.getProgress();
                int pLength = lengthSeekbarThread.getProgress();

                //initialize progress bar(dialog)
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Generating Passwords");
                progressDialog.setMax(pCount);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();

                for(int i = 0; i < pCount; i++) {
                    threadPool.execute(new GeneratePassword(pLength));
                    //Log.d("demo", "In For loop executing GeneratePassword.");
                }
            }
        });
        //endregion
    }

    //async code
    //region
    class DoWorkAsync extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String aString) {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
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
