package com.group9.inclass13;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

//In-Class13
//Group 9
//Rockford Stoller

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    ArrayList<Task> myData;

    MainActivity mainActivity;

    PrettyTime prettyTime = new PrettyTime();

    public TaskAdapter(ArrayList<Task> myData) {
        this.myData = myData;
    }

    @Override
    public int getItemViewType(int position) {
        //altered the getItemViewType to return the position of the overall view
        //to have it as an index in the onCreateViewHolder
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int index = i;

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item, viewGroup, false);

        //get all inner views
        TextView taskTextView = view.findViewById(R.id.task_in_item_list_textView);
        CheckBox statusCheckBox = view.findViewById(R.id.status_checkBox);
        TextView priorityTextView = view.findViewById(R.id.priority_in_item_list_textView);
        TextView timeTextView = view.findViewById(R.id.time_in_item_list_textView);

        //get correct task data
        final Task task = myData.get(index);

        //set text view text with task data
        taskTextView.setText(task.getText());
        taskTextView.setSingleLine(true);

        priorityTextView.setText(task.getPriority());
        priorityTextView.setSingleLine(true);

        timeTextView.setText(prettyTime.format(task.getDate()));

        //get MainActivity instance
        mainActivity = (MainActivity) view.getContext();

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        //ensure all text displayed is correct for this task
        final Task task = myData.get(position);
        viewHolder.taskTextView.setText(task.getText());
        viewHolder.taskTextView.setSingleLine(true);

        viewHolder.priorityTextView.setText(task.getPriority());
        viewHolder.priorityTextView.setSingleLine(true);

        viewHolder.timeTextView.setText(prettyTime.format(task.getDate()));

        //get MainActivity instance
        mainActivity = (MainActivity) viewHolder.timeTextView.getContext();
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView taskTextView;
        CheckBox statusCheckBox;
        TextView priorityTextView;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.task_in_item_list_textView);
            statusCheckBox = itemView.findViewById(R.id.status_checkBox);
            priorityTextView = itemView.findViewById(R.id.priority_in_item_list_textView);
            timeTextView = itemView.findViewById(R.id.time_in_item_list_textView);
        }
    }
}