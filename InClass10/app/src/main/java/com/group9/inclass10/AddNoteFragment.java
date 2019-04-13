package com.group9.inclass10;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//In-Class10
//Group 9
//Rockford Stoller

public class AddNoteFragment extends Fragment {

    OnAddNoteFragmentInteractionListener mListener;

    private MainActivity mainActivity;

    private EditText noteEditText;

    public AddNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        getActivity().setTitle("Add Note");

        mainActivity = (MainActivity) getActivity();

        noteEditText = view.findViewById(R.id.note_in_add_note_editText);

        view.findViewById(R.id.add_note_in_add_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddNoteAsync().execute();
            }
        });

        return view;
    }

    public class AddNoteAsync extends AsyncTask<Void, Void, Note> {

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);
            mListener.addedNote(note);
        }

        @Override
        protected Note doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("text", noteEditText.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post")
                    .header("x-access-token", mainActivity.userToken)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build();

            Note addedNote = null;

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String json = response.body().string();

                JSONObject root = new JSONObject(json);
                if(root.getBoolean("posted") == true) {
                    addedNote = new Note();
                    JSONObject jsonNote = root.getJSONObject("note");
                    addedNote.noteId = jsonNote.getString("_id");
                    addedNote.userId = jsonNote.getString("userId");
                    addedNote.noteText = jsonNote.getString("text");
                    addedNote.v = jsonNote.getInt("__v");
                }

                Log.d("demo", "addedNote: " + addedNote);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return addedNote;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddNoteFragment.OnAddNoteFragmentInteractionListener) {
            mListener = (AddNoteFragment.OnAddNoteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddNoteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddNoteFragmentInteractionListener {
        // TODO: Update argument type and name
        void addedNote(Note note);
    }
}
