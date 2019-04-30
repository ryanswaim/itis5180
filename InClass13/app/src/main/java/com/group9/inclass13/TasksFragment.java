package com.group9.inclass13;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

//In-Class13
//Group 9
//Rockford Stoller

public class TasksFragment extends Fragment {

    public static String TASK_LIST_KEY = "task_list_key";

    OnTasksFragmentInteractionListener mListener;

    private TaskAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private EditText taskEditText;
    private String taskPriority = null;
    private MainActivity mainActivity = null;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        getActivity().setTitle("Tasks");

        mainActivity = (MainActivity) getActivity();

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.notes_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new TaskAdapter((ArrayList<Task>) this.getArguments().getSerializable(TASK_LIST_KEY));
        recyclerView.setAdapter(adapter);

        //get edit text
        taskEditText = view.findViewById(R.id.task_name_editText);

        //set spinner listener
        //region
        Spinner prioritySpinner = view.findViewById(R.id.priority_spinner);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] priorities = getResources().getStringArray(R.array.priority);
                taskPriority = priorities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //add task button
        //region
        view.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create task and assign values
                if(taskEditText.length() > 0 && taskPriority != null) {
                    Task task = new Task();
                    task.setText(taskEditText.getText().toString());
                    task.setPriority(taskPriority);
                    task.setDate(new Date());
                    mListener.addTask(task);
                } else {
                    if(taskEditText.length() == 0) {
                        Toast.makeText(mainActivity, "Enter a Task", Toast.LENGTH_LONG).show();
                    } else if(taskPriority == null) {
                        Toast.makeText(mainActivity, "Select a Priority", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //endregion

        return view;
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TasksFragment.OnTasksFragmentInteractionListener) {
            mListener = (TasksFragment.OnTasksFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTasksFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTasksFragmentInteractionListener {
        // TODO: Update argument type and name
        //void goToCreateContactFragment();
        void addTask(Task task);
    }
}
