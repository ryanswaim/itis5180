package com.example.inclass07;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//In Class Assignment 07
//Group 9
//Rockford Stoller
//Ryan Swaim

public class ShowExpenseFragment extends Fragment {

    public static String EXPENSE_KEY = "expense_object";

    public ShowExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Show Expense");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show_expense, container, false);

        TextView textViewName = view.findViewById(R.id.name_in_details_textView);
        TextView textViewCategory = view.findViewById(R.id.category_in_details_textView);
        TextView textViewAmount = view.findViewById(R.id.amount_in_details_textView);
        TextView textViewDate = view.findViewById(R.id.date_in_details_textView);
        Expense expense = (Expense) this.getArguments().getSerializable(EXPENSE_KEY);

        textViewName.setText(expense.name);
        textViewCategory.setText(expense.category);
        textViewAmount.setText("$ " + expense.amount);
        textViewDate.setText(expense.date);

        //close button
        //region
        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //endregion

        return view;
    }

}
