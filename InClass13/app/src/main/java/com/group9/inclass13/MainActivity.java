package com.group9.inclass13;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TasksFragment.OnTasksFragmentInteractionListener {
    DatabaseDataManger databaseDataManger;
    TasksFragment tasksFragment;

    ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseDataManger = new DatabaseDataManger(this);

        //databaseDataManger.saveTask(new Task("Task 1", "Task 1 Text"));
        tasks = databaseDataManger.getAllTasks();

        Log.d("demo", tasks.toString());

        //load the notes fragment
        //region
        tasksFragment = new TasksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TasksFragment.TASK_LIST_KEY, tasks);
        tasksFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, tasksFragment, "tasks_fragment")
                .commit();
        //endregion
    }

    @Override
    protected void onDestroy() {
        databaseDataManger.close();
        super.onDestroy();
    }

    @Override
    public void addTask(Task task) {
        databaseDataManger.saveTask(task);
        tasks = databaseDataManger.getAllTasks();
    }
}
