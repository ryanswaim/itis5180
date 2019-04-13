package com.group9.inclass10;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
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

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener, SignUpFragment.OnSignUpFragmentInteractionListener,
        NotesFragment.OnContactsFragmentInteractionListener, AddNoteFragment.OnAddNoteFragmentInteractionListener {

    ArrayList<Note> notes = new ArrayList<>();

    NotesFragment notesFragment = null;

    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEditor;
    String userToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Login");

        sharedPref = this.getPreferences(MODE_PRIVATE);
        prefEditor = sharedPref.edit();

        userToken = sharedPref.getString(getString(R.string.token_storage_key), null);

        Log.d("demo", "userToken: " + userToken);

        //if there is user token in the sharedPref
        if(userToken != null) {
            getAllNotesAndGoToNotesFragment();
        } else {
            //create login fragment with ArrayList<Note> that is stored in MainActivity as an argument
            //region
            //store the expense app that is the main screen/activity in a global variable
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, loginFragment, "login_fragment")
                    .commit();
            //endregion
        }
    }

    private void getAllNotesAndGoToNotesFragment() {
        //call get all from api
        //region
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                .header("x-access-token", userToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.d("demo", "onFailure: get name failed");
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        //if token is no longer valid/failed
                        //create login fragment with ArrayList<Note> that is stored in MainActivity as an argument
                        //region
                        //store the expense app that is the main screen/activity in a global variable
                        LoginFragment loginFragment = new LoginFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, loginFragment, "login_fragment")
                                .commit();
                        //endregion

                        throw new IOException("Unexpected code " + response);
                    }

                    String json = responseBody.string();
                    JSONObject root = new JSONObject(json);

                    JSONArray notesArray = root.getJSONArray("notes");

                    Log.d("demo", notesArray.length() + "");

                    //clear array for certain
                    notes.clear();
                    //add all notes
                    for(int i = 0; i < notesArray.length(); i++) {
                        JSONObject noteJson = notesArray.getJSONObject(i);
                        Note note = new Note();

                        note.noteId = noteJson.getString("_id");
                        note.userId = noteJson.getString("userId");
                        note.noteText = noteJson.getString("text");
                        note.v = noteJson.getInt("__v");

                        notes.add(note);
                    }

                    //load the notes fragment
                    //region
                    notesFragment = new NotesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(NotesFragment.NOTE_LIST_KEY, notes);
                    notesFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, notesFragment, "notes_fragment")
                            .commit();
                    //endregion

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion
    }

    public void goToDisplayNoteFragment(Note note) {
        //load the display note fragment
        //region
        DisplayNoteFragment displayNoteFragment = new DisplayNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DisplayNoteFragment.NOTE_KEY, note);
        displayNoteFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, displayNoteFragment, "display_note_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void goToSignUpFragment() {
        //replace login fragment with sign up fragment (null is the default BackStack)
        //region
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, signUpFragment, "sign_up_fragment")
                .commit();
        //endregion
    }

    @Override
    public void loggedIn() {
        if(userToken != null) {
            getAllNotesAndGoToNotesFragment();
        }
        //replace login fragment with notes fragment (null is the default BackStack)
        //region
        /*
        notesFragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NotesFragment.NOTE_LIST_KEY, notes);
        notesFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, notesFragment, "notes_fragment")
                .commit();
                */
        //endregion
    }

    @Override
    public void goToLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "login_fragment")
                .commit();
    }

    @Override
    public void signedUp() {
        if(userToken != null) {
            getAllNotesAndGoToNotesFragment();
        }
        //replace sign up fragment with notes fragment (null is the default BackStack)
        //region
        /*
        notesFragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NotesFragment.NOTE_LIST_KEY, notes);
        notesFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, notesFragment, "notes_fragment")
                .commit();
                */
        //endregion
    }

    @Override
    public void goToAddNoteFragment() {
        //replace notes fragment with add note fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddNoteFragment(), "add_note_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void loggedOut() {
        //clear notes list when logged out
        notes.clear();

        //load login fragment
        //region
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "login_fragment")
                .commit();
        //endregion
    }

    @Override
    public void addedNote(Note note) {
        notes.add(note);
        notesFragment.notifyAdapter();
        getSupportFragmentManager().popBackStack();
    }

    //unnecessary
    //region
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    //endregion
}