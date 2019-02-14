package com.example.inclass03;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectAvatarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);

        setTitle("Select Avatar");

        //if the first avatar is chosen
        //on click
        findViewById(R.id.avatar1_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.VALUE_KEY, 1);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });

        //if the second avatar is chosen
        //on click
        findViewById(R.id.avatar2_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.VALUE_KEY, 2);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });

        //if the third avatar is chosen
        //on click
        findViewById(R.id.avatar3_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.VALUE_KEY, 3);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });

        //if the fourth avatar is chosen
        //on click
        findViewById(R.id.avatar4_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.VALUE_KEY, 4);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });

        //if the fifth avatar is chosen
        //on click
        findViewById(R.id.avatar5_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.VALUE_KEY, 5);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });

        //if the sixth avatar is chosen
        //on click
        findViewById(R.id.avatar6_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.VALUE_KEY, 6);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });
    }
}
