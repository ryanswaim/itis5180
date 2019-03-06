package com.example.homework03;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//Homework Assignment 03
//Group 9
//Rockford Stoller
//Ryan Swaim

public class TrackAdapter extends ArrayAdapter<Track> {
    public TrackAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Track> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Track track = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewTrackTitle = convertView.findViewById(R.id.track_title_textView);
            viewHolder.textViewTrackPrice = convertView.findViewById(R.id.track_price_textView);
            viewHolder.textViewTrackArtist = convertView.findViewById(R.id.track_artist_textView);
            viewHolder.textViewTrackDate = convertView.findViewById(R.id.track_date_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewTrackTitle.setText("Track: " + track.title);
        viewHolder.textViewTrackPrice.setText("Price: " + track.trackPrice + " $");
        viewHolder.textViewTrackArtist.setText("Artist: " + track.artist);
        viewHolder.textViewTrackDate.setText("Date: " + track.date.substring(5, 10) + "-" + track.date.substring(0, 4));

        return convertView;
    }

    private static class ViewHolder{
        TextView textViewTrackTitle;
        TextView textViewTrackPrice;
        TextView textViewTrackArtist;
        TextView textViewTrackDate;
    }
}