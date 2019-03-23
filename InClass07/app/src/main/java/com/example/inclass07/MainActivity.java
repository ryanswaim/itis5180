package com.example.inclass07;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

//In Class Assignment 07
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnExpenseAppFragmentInteractionListener, AddExpenseFragment.OnAddExpenseFragmentInteractionListener {

    ArrayList<Expense> expenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create expense app fragment with ArrayList<Expense> that is store in MainActivity as an argument
        //region
        ExpenseAppFragment expenseAppFragment = new ExpenseAppFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExpenseAppFragment.EXPENSES_LIST_KEY, expenses);
        expenseAppFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, expenseAppFragment, "expense_app_fragment")
                .commit();
        //endregion
    }

    @Override
    public void goToAddExpenseFragment() {
        //replace expense app fragment with add expense fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddExpenseFragment(), "add_expense_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void goToShowExpenseFragment(Expense expense) {
        //replace expense app fragment with show expense fragment (null is the default BackStack)
        //region
        ShowExpenseFragment showExpenseFragment = new ShowExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowExpenseFragment.EXPENSE_KEY, expense);
        showExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, showExpenseFragment, "show_expense_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void addExpenseToList(Expense expense) {
        expenses.add(expense);
    }

    //unnecessary
    //region
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    //endregion
}
