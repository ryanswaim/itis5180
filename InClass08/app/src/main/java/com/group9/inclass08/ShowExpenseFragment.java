package com.group9.inclass08;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class ShowExpenseFragment extends Fragment {

    public static String EXPENSE_KEY = "expense_object";
    public static String INDEX_KEY = "index_int";

    private OnShowExpenseFragmentListener mListener;

    public ShowExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Show Expense");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show_expense, container, false);

        //get all fields
        TextView textViewName = view.findViewById(R.id.name_in_details_textView);
        TextView textViewCategory = view.findViewById(R.id.category_in_details_textView);
        TextView textViewAmount = view.findViewById(R.id.amount_in_details_textView);
        TextView textViewDate = view.findViewById(R.id.date_in_details_textView);
        final Expense expense = (Expense) this.getArguments().getSerializable(EXPENSE_KEY);
        final int index = this.getArguments().getInt(INDEX_KEY);

        //assign all fields their values
        textViewName.setText(expense.name);
        textViewCategory.setText(expense.category);
        textViewAmount.setText("$ " + expense.amount);
        textViewDate.setText(expense.date);

        //go to edit expense button
        //region
        view.findViewById(R.id.goTo_edit_expense_fragment_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call go to EditExpenseFragment function in MainActivity
                mListener.goToEditExpenseFragment(expense, index);
            }
        });
        //endregion

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowExpenseFragment.OnShowExpenseFragmentListener) {
            mListener = (ShowExpenseFragment.OnShowExpenseFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShowExpenseFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnShowExpenseFragmentListener {
        void goToEditExpenseFragment(Expense expense, int index);
    }
}
