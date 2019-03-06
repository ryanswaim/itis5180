package com.example.homework03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

//Homework Assignment 03
//Group 9
//Rockford Stoller
//Ryan Swaim

public class TrackDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_display);

        setTitle(R.string.main_activity_title);

        if(getIntent() != null && getIntent().getExtras() != null) {

            ImageView artworkImageView = findViewById(R.id.artwork_imageView);
            TextView trackTitleTextView = findViewById(R.id.track_title_textView);
            TextView trackGenreTextView = findViewById(R.id.track_genre_textView);
            TextView trackArtistTextView = findViewById(R.id.track_artist_textView);
            TextView albumTitleTextView = findViewById(R.id.album_title_textView);
            TextView trackPriceTextView = findViewById(R.id.track_price_textView);
            TextView albumPriceTextView = findViewById(R.id.album_price_textView);

            Track track = (Track) getIntent().getExtras().getSerializable(MainActivity.TRACK_KEY);

            Picasso.get().load(track.artworkURL).placeholder(R.drawable.loading).into(artworkImageView);
            trackTitleTextView.setText("Track: " + track.title);
            trackGenreTextView.setText("Genre: " + track.genre);
            trackArtistTextView.setText("Artist: " + track.artist);
            albumTitleTextView.setText("Album: " + track.album);
            trackPriceTextView.setText("Track Price: " + track.trackPrice + " $");
            albumPriceTextView.setText("Album Price: " + track.albumPrice + " $");
        }

        findViewById(R.id.finish_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
