package com.example.homework02;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

//Homework Assignment 02
//Group 9
//Rockford Stoller
//Ryan Swaim

public class GeneratedPasswordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_passwords);

        setTitle(R.string.title_text2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent() != null && getIntent().getExtras() != null) {

            ArrayList<String> threadPasswords = getIntent().getExtras().getStringArrayList(MainActivity.THREAD_KEY);
            ArrayList<String> asyncPasswords = getIntent().getExtras().getStringArrayList(MainActivity.ASYNC_KEY);

            for(String password : threadPasswords) {
                //call the main layout from xml
                LinearLayout parentLayout = findViewById(R.id.thread_scroll_layout);

                //create a view to inflate the layout_item (the xml with the textView created before)
                View view = getLayoutInflater().inflate(R.layout.text_view, parentLayout,false);

                TextView textView = (TextView) view.findViewById(R.id.text_id);

                textView.setText(password);

                //add the view to the main layout
                parentLayout.addView(textView);
            }

            for(String password : asyncPasswords) {
                //call the main layout from xml
                LinearLayout parentLayout = findViewById(R.id.async_scroll_layout);

                //create a view to inflate the layout_item (the xml with the textView created before)
                View view = getLayoutInflater().inflate(R.layout.text_view, parentLayout,false);

                TextView textView = (TextView) view.findViewById(R.id.text_id);

                textView.setText(password);

                //add the view to the main layout
                parentLayout.addView(textView);
            }

        }
    }
}
