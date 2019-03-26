package com.group9.inclass08;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class AddExpenseFragment extends Fragment {

    private OnAddExpenseFragmentInteractionListener mListener;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Add Expense");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        //Add Expense button
        //region
        view.findViewById(R.id.add_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText expenseNameEditText = view.findViewById(R.id.expense_name_editText);
                Spinner categorySpinner = view.findViewById(R.id.category_spinner);
                EditText amountEditText = view.findViewById(R.id.amount_editText);
                String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

                if(expenseNameEditText.length() > 0 && amountEditText.length() > 0) {
                    Expense expense = new Expense();
                    expense.name = expenseNameEditText.getText().toString();
                    expense.category = categorySpinner.getSelectedItem().toString();
                    expense.amount = Double.parseDouble(amountEditText.getText().toString());
                    expense.date = date;

                    //call add expense function in the MainActivity
                    mListener.addExpenseToList(expense);
                    //pop off stack in teh MainActivity
                    //getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    if(expenseNameEditText.length() == 0) {
                        Toast.makeText(getContext(), "Please enter the name of your expense", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Please enter the amount of your expense", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //endregion

        //cancel button
        //region
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //endregion

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddExpenseFragmentInteractionListener) {
            mListener = (OnAddExpenseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpenseAppFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddExpenseFragmentInteractionListener {
        // TODO: Update argument type and name
        void addExpenseToList(Expense expense);
    }
}
