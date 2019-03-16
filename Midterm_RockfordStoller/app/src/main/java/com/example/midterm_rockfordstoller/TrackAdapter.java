package com.example.midterm_rockfordstoller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

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
            viewHolder.textViewTrackAlbum = convertView.findViewById(R.id.track_album_textView);
            viewHolder.textViewTrackArtist = convertView.findViewById(R.id.track_artist_textView);
            viewHolder.textViewTrackDate = convertView.findViewById(R.id.track_date_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(track != null) {
            viewHolder.textViewTrackTitle.setText("Track: " + track.trackName);
            viewHolder.textViewTrackAlbum.setText("Album: " + track.albumName);
            viewHolder.textViewTrackArtist.setText("Artist: " + track.artistName);
            viewHolder.textViewTrackDate.setText("Date: " + track.updateTime.substring(5, 10) + "-" + track.updateTime.substring(0, 4));
        }

        return convertView;
    }

    private static class ViewHolder{
        TextView textViewTrackTitle;
        TextView textViewTrackAlbum;
        TextView textViewTrackArtist;
        TextView textViewTrackDate;
    }
}