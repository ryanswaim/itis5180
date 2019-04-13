package com.group9.inclass10;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

//In-Class10
//Group 9
//Rockford Stoller

public class NotesFragment extends Fragment {

    public static String NOTE_LIST_KEY = "note_list_key";

    OnContactsFragmentInteractionListener mListener;

    private NoteAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private TextView fullNameTextView;
    private MainActivity mainActivity = null;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notes, container, false);

        getActivity().setTitle("Notes");

        mainActivity = (MainActivity) getActivity();

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.notes_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new NoteAdapter((ArrayList<Note>) this.getArguments().getSerializable(NOTE_LIST_KEY));
        recyclerView.setAdapter(adapter);

        fullNameTextView = view.findViewById(R.id.full_name_in_notes_textView);
        fullNameTextView.setText(null);

        //get name async
        //region
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me")
                .header("x-access-token", mainActivity.userToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.d("demo", "onFailure: get name failed");
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String json = responseBody.string();
                    JSONObject root = new JSONObject(json);
                    fullNameTextView.setText("Hey " + root.getString("name") + "!!!");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion

        //add note button
        //region
        view.findViewById(R.id.add_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create note and assign values
                Note note = new Note();
                note.noteText = "";

                //go to add note fragment
                mListener.goToAddNoteFragment();
            }
        });
        //endregion

        view.findViewById(R.id.logout_imageView_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout async
                //region
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/logout")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        Log.d("demo", "onFailure: get name failed");
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            String json = responseBody.string();
                            JSONObject root = new JSONObject(json);

                            if(root.getBoolean("auth") == false) {
                                mainActivity.prefEditor.clear();
                                mainActivity.prefEditor.commit();
                                mainActivity.userToken = null;
                                mListener.loggedOut();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //endregion
            }
        });

        return view;
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotesFragment.OnContactsFragmentInteractionListener) {
            mListener = (NotesFragment.OnContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnContactsFragmentInteractionListener {
        // TODO: Update argument type and name
        //void goToCreateContactFragment();
        void goToAddNoteFragment();
        void loggedOut();
    }
}
