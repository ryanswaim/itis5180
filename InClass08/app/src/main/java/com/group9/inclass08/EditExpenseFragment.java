package com.group9.inclass08;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class EditExpenseFragment extends Fragment {

    private OnEditExpenseFragmentInteractionListener mListener;

    public EditExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Edit Expense");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_expense, container, false);

        //get arguments
        Expense expense = (Expense) this.getArguments().getSerializable(ShowExpenseFragment.EXPENSE_KEY);
        final int index = this.getArguments().getInt(ShowExpenseFragment.INDEX_KEY);

        //get fields
        final EditText nameEditText = view.findViewById(R.id.expense_name_editText);
        final EditText amountEditText = view.findViewById(R.id.amount_editText);
        final Spinner categorySpinner = view.findViewById(R.id.category_spinner);

        //fill in the fields
        nameEditText.setText(expense.name);
        amountEditText.setText(expense.amount + "");

        //get strings values from StringArray in resource file
        String[] categoriesTemp = getResources().getStringArray(R.array.categories);

        //check which string the category matches to
        for(int i = 0; i < categoriesTemp.length; i++) {
            if(expense.category.equals(categoriesTemp[i])) {
                //select the correct category index
                categorySpinner.setSelection(i);
            }
        }

        //save button
        //region
        view.findViewById(R.id.save_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEditText.length() > 0 && amountEditText.length() > 0) {
                    //set new date
                    String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

                    //create new expense
                    Expense expense = new Expense();

                    //assign new expense details
                    expense.name = nameEditText.getText().toString();
                    expense.category = categorySpinner.getSelectedItem().toString();
                    expense.amount = Double.parseDouble(amountEditText.getText().toString());
                    expense.date = date;

                    //call update expense function in MainActivity
                    mListener.updateExpenseInList(expense, index);
                } else {
                    if(nameEditText.length() == 0) {
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
        if (context instanceof OnEditExpenseFragmentInteractionListener) {
            mListener = (OnEditExpenseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEditExpenseFragmentInteractionListener {
        // TODO: Update argument type and name
        void updateExpenseInList(Expense expense, int index);
    }
}
