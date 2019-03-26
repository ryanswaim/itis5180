package com.group9.inclass08;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class ExpenseAppFragment extends Fragment {

    public static String EXPENSES_LIST_KEY = "expenses_object_list";

    private OnExpenseAppFragmentInteractionListener mListener;
    private ExpenseAdapter adapter;

    public ExpenseAppFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();

        if(!adapter.isEmpty()){
            getView().findViewById(R.id.message_textView).setVisibility(View.INVISIBLE);
        } else {
            getView().findViewById(R.id.message_textView).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Expense App");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_app, container, false);

        adapter = new ExpenseAdapter(getContext(), R.layout.expense_item, (ArrayList<Expense>) this.getArguments().getSerializable(EXPENSES_LIST_KEY));

        ListView listView = view.findViewById(R.id.expense_listView);

        listView.setAdapter(adapter);

        //click on list view
        //region
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Expense expense = adapter.getItem(position);
                //adapter.remove(expense);
                //adapter.notifyDataSetChanged();

                //call remove function in the MainActivity
                mListener.removeExpenseFromList(adapter, position);

                if(adapter.isEmpty()) {
                    getView().findViewById(R.id.message_textView).setVisibility(View.VISIBLE);
                }

                Toast.makeText(getContext(), "Expense deleted", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense expense = adapter.getItem(position);
                mListener.goToShowExpenseFragment(expense, position);
            }
        });
        //endregion

        //add button
        //region
        view.findViewById(R.id.goTo_add_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddExpenseFragment();
            }
        });
        //endregion

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExpenseAppFragmentInteractionListener) {
            mListener = (OnExpenseAppFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpenseAppFragmentInteractionListener");
        }
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    public interface OnExpenseAppFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddExpenseFragment();
        void goToShowExpenseFragment(Expense expense, int index);
        void removeExpenseFromList(ExpenseAdapter adapter, int index);
        //ArrayList<Expense> getExpensesArrayList(); //initial method switched
    }
}
