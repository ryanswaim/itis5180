package com.group9.inclass08;

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

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    public ExpenseAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Expense> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Expense expense = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.name_in_item_list_textView);
            viewHolder.textViewAmount = convertView.findViewById(R.id.amount_in_item_list_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewName.setText(expense.name);
        viewHolder.textViewAmount.setText("$" + expense.amount);

        return convertView;
    }

    private static class ViewHolder{
        TextView textViewName;
        TextView textViewAmount;
    }
}