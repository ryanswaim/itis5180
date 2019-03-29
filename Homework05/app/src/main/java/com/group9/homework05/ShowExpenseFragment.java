package com.group9.homework05;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

//Homework05
//Group 9
//Rockford Stoller
//Ryan Swaim

public class ShowExpenseFragment extends Fragment {

    public static String EXPENSE_KEY = "expense_object";
    public static String INDEX_KEY = "index_int";

    public ShowExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.isOnMainScreen = false;
        getActivity().invalidateOptionsMenu();

        getActivity().setTitle("Show Expense");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show_expense, container, false);

        //get all fields
        TextView textViewName = view.findViewById(R.id.name_in_details_textView);
        TextView textViewAmount = view.findViewById(R.id.amount_in_details_textView);
        TextView textViewDate = view.findViewById(R.id.date_in_details_textView);
        ImageView receiptImageView = view.findViewById(R.id.receipt_in_details_imageView);
        final Expense expense = (Expense) this.getArguments().getSerializable(EXPENSE_KEY);
        final int index = this.getArguments().getInt(INDEX_KEY);

        //assign all fields their values
        textViewName.setText(expense.name);
        //textViewCategory.setText(expense.category);
        textViewAmount.setText("$ " + expense.amount);
        textViewDate.setText(expense.date);

        Picasso.get().load(expense.receiptUrl).placeholder(R.drawable.loading).into(receiptImageView);

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
