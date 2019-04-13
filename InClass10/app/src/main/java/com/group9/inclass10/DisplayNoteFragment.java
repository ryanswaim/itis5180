package com.group9.inclass10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//In-Class10
//Group 9
//Rockford Stoller

public class DisplayNoteFragment extends Fragment {

    public static String NOTE_KEY = "note_key";

    private MainActivity mainActivity;

    public DisplayNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_note, container, false);

        getActivity().setTitle("Display Note");

        mainActivity = (MainActivity) getActivity();

        Note note = (Note) this.getArguments().getSerializable(NOTE_KEY);

        TextView noteDisplayTextView = view.findViewById(R.id.note_in_display_note_textView);
        noteDisplayTextView.setText(note.noteText);
        noteDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        view.findViewById(R.id.close_in_display_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
