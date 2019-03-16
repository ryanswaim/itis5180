package com.example.midterm_rockfordstoller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTracksAsync.TrackInfoData {

    ArrayList<Track> data = new ArrayList<>();
    TrackAdapter adapter = null;
    EditText searchBar;
    RadioGroup sortingRadioGroup;
    SeekBar limitBar;
    TextView limitDisplayTextView;
    int numberOfResults;
    String sortResultsBy;
    String searchTerm;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.main_activity_title);

        searchBar = findViewById(R.id.searchBar_editText);
        sortingRadioGroup = findViewById(R.id.sorting_radioGroup);
        limitBar = findViewById(R.id.limit_seekBar);
        limitDisplayTextView = findViewById(R.id.limit_display_textView);
        numberOfResults = 10;
        sortResultsBy = "s_track_rating";

        progressBar = findViewById(R.id.loading_progressBar);

        //sorting radio group
        //region
        sortingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String temp[] = radioButton.getText().toString().split(" ");
                sortResultsBy = "s_" + temp[0].toLowerCase() + "_" + temp[1].toLowerCase();

                if(data.size() > 0) {
                    //remove whitespace at beginning and end of searchbar entry
                    searchBar.setText(searchBar.getText().toString().trim());

                    if(isConnected() && searchBar.getText().length() > 0) {
                        RequestParams url;

                        //create the correct format of the search term with + between keywords
                        //region
                        String[] tempStrings1 = searchBar.getText().toString().split(" ");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(tempStrings1[0]);
                        for(int i = 1; i < tempStrings1.length; i++) {
                            stringBuilder.append("+");
                            stringBuilder.append(tempStrings1[i]);
                        }

                        searchTerm = stringBuilder.toString();
                        //endregion

                        Log.d("demo", "Value being sent to RequestParams as term: " + searchTerm);

                        url = new RequestParams()
                                .addParameter("apikey", "bdccf7f1fcb46e3912ef67f211d85143")
                                .addParameter("q", searchTerm)
                                .addParameter(sortResultsBy, "desc")
                                .addParameter("page_size", numberOfResults + "");
                        String urlStr = url.getEncodedUrl("https://api.musixmatch.com/ws/1.1/track.search");
                        new GetTracksAsync(MainActivity.this).execute(urlStr);
                        progressBar.setVisibility(View.VISIBLE);

                    } else if (searchBar.getText().length() <= 0) {
                        searchBar.setError("You must enter a keyword to search");
                    } else {
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                //Log.d("demo", sortResultsBy);
            }
        });
        //endregion

        //limit bar adjusting
        //region
        limitBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String limitDisplayText = "Limit: " + progress;
                limitDisplayTextView.setText(limitDisplayText);
                numberOfResults = progress;
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
                    String[] tempStrings2 = searchBar.getText().toString().split(" ");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(tempStrings2[0]);
                    for(int i = 1; i < tempStrings2.length; i++) {
                        stringBuilder.append("+");
                        stringBuilder.append(tempStrings2[i]);
                    }

                    searchTerm = stringBuilder.toString();
                    //endregion

                    Log.d("demo", "Value being sent to RequestParams as term: " + searchTerm);

                    url = new RequestParams()
                            .addParameter("apikey", "bdccf7f1fcb46e3912ef67f211d85143")
                            .addParameter("q", searchTerm)
                            .addParameter(sortResultsBy, "desc")
                            .addParameter("page_size", numberOfResults + "");
                    String urlStr = url.getEncodedUrl("https://api.musixmatch.com/ws/1.1/track.search");
                    new GetTracksAsync(MainActivity.this).execute(urlStr);
                    progressBar.setVisibility(View.VISIBLE);

                } else if (searchBar.getText().length() <= 0) {
                    searchBar.setError("You must enter a keyword to search");
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    //GetTrackAsync return methods
    //region
    @Override
    public void handleTrackData(ArrayList<Track> tracks) {
        ListView listView = findViewById(R.id.results_listView);

        data.clear();
        data.addAll(tracks);

        if(adapter == null) {
            adapter = new TrackAdapter(MainActivity.this, R.layout.track_item, data);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Track track = adapter.getItem(position);

                if(track != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(track.trackShareURL));
                    startActivity(intent);
                }
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateProgress(int progress) {
    }
    //endregion
}
