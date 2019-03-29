package com.group9.homework05;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

//Homework05
//Group 9
//Rockford Stoller
//Ryan Swaim

public class ExpenseAppFragment extends Fragment {

    public static String EXPENSES_LIST_KEY = "expenses_object_list";

    private OnExpenseAppFragmentInteractionListener mListener;
    //private RecyclerView.Adapter adapter;
    //trying this for being able to add up total cost
    private ExpenseAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;

    public ExpenseAppFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.isOnMainScreen = true;
        getActivity().invalidateOptionsMenu();

        adapter.notifyDataSetChanged();

        if(adapter.getItemCount() != 0){
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

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.expense_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new ExpenseAdapter((ArrayList<Expense>) this.getArguments().getSerializable(EXPENSES_LIST_KEY));
        recyclerView.setAdapter(adapter);

        TextView totalCostTextView = view.findViewById(R.id.total_cost_display_textView);

        double totalCost = 0;

        for (Expense expense : adapter.myData) {
            totalCost += expense.amount;
        }

        totalCostTextView.setText(String.format("$%.2f", totalCost));

        //add button
        //region
        view.findViewById(R.id.goTo_add_expense_fab).setOnClickListener(new View.OnClickListener() {
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
        Log.d("demo", "after notify " + adapter.getItemCount());
    }

    public interface OnExpenseAppFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddExpenseFragment();
        void goToShowExpenseFragment(Expense expense, int index);
        //void removeExpenseFromList(ExpenseAdapter adapter, int index);
        //ArrayList<Expense> getExpensesArrayList(); //initial method switched
    }
}
