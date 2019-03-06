package com.example.homework03;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

//Homework Assignment 03
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity implements GetTracksAsync.TrackInfoData {

    public static String TRACK_KEY = "TRACK";

    ArrayList<Track> data = new ArrayList<>();
    TrackAdapter adapter = null;
    EditText searchBar;
    SeekBar limitBar;
    TextView limitDisplayTextView;
    int numberOfResults;
    Switch sortSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.main_activity_title);

        searchBar = findViewById(R.id.search_bar_editText);
        limitBar = findViewById(R.id.limit_seekBar);
        limitDisplayTextView = findViewById(R.id.limit_display_textView);
        numberOfResults = 10;
        sortSwitch = findViewById(R.id.sort_switch);

        //limit seekbar
        //region
        limitBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberOfResults = progress;
                limitDisplayTextView.setText("Limit: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //endregion

        //search button
        //region
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove whitespace at beginning and end of searchbar entry
                searchBar.setText(searchBar.getText().toString().trim());

                if(isConnected() && searchBar.getText().length() > 0) {
                    RequestParams url;

                    //create the correct format of the search term with + between keywords
                    //region
                    String[] temp = searchBar.getText().toString().split(" ");
                    String searchTerm = temp[0];
                    for(int i = 1; i < temp.length; i++) {
                        searchTerm += "+" + temp[i];
                    }
                    //endregion

                    Log.d("demo", "Value being sent to RequestParams as term: " + searchTerm);

                    url = new RequestParams()
                            .addParameter("term", searchTerm)
                            .addParameter("limit", numberOfResults + "");
                    String urlStr = url.getEncodedUrl("https://itunes.apple.com/search");
                    new GetTracksAsync(MainActivity.this).execute(urlStr);

                } else if (searchBar.getText().length() <= 0) {
                    searchBar.setError("You must enter a keyword to search");
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //reset button
        //region
        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                limitBar.setProgress(10);
                //sortSwitch.setChecked(true);

                if(adapter != null) {
                    data.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        //endregion

        //sort switch
        //region
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(adapter != null) {
                    sortDataArrayList();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        //endregion
    }

    //is connected
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

    //sorting method
    //region
    public void sortDataArrayList() {
        if(sortSwitch.isChecked()) {
            data.sort(new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    if (o1.date.compareTo(o2.date) == 0) {
                        return 0;
                    } else if (o1.date.compareTo(o2.date) < 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        } else {
            data.sort(new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    if (o1.trackPrice.compareTo(o2.trackPrice) == 0) {
                        return 0;
                    } else if (o1.trackPrice.compareTo(o2.trackPrice) < 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
    }
    //endregion

    //GetTrackAsync return methods
    //region
    @Override
    public void handleTrackData(ArrayList<Track> tracks) {
        ListView listView = findViewById(R.id.results_listView);

        data.clear();
        data.addAll(tracks);

        sortDataArrayList();

        if(adapter == null) {
            adapter = new TrackAdapter(MainActivity.this, R.layout.track_item, data);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(MainActivity.this, "Clicked item " + position + ", color " + data.get(position) + " will be removed", Toast.LENGTH_LONG).show();
                Track track = adapter.getItem(position);
                //adapter.remove(track);
                //adapter.notifyDataSetChanged();

                Intent intent = new Intent(MainActivity.this, TrackDisplayActivity.class);

                intent.putExtra(TRACK_KEY, track);

                startActivity(intent);
            }
        });
    }

    @Override
    public void updateProgress(int progress) {

    }
    //endregion
}
