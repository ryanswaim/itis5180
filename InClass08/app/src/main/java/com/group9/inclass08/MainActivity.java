package com.group9.inclass08;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnExpenseAppFragmentInteractionListener, AddExpenseFragment.OnAddExpenseFragmentInteractionListener,
        EditExpenseFragment.OnEditExpenseFragmentInteractionListener, ShowExpenseFragment.OnShowExpenseFragmentListener {

    ArrayList<Expense> expenses = new ArrayList<>();

    //create database variables
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenses");
    ExpenseAppFragment expenseAppFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("expenses");
        //Expense exp1 = new Expense("WalMart", "1", "2", 30.0);
        //Expense exp2 = new Expense("Harris Teeter", "1", "2", 50.0);
        //expenses.add(exp1);
        //expenses.add(exp2);
        //myRef.setValue(expenses);

        //get all expenses on start up and clear the list and repopulate it when values are set
        //I know this is a bad way to do it, but I am unsure how to not add multiples if I do not clear
        //and I am not sure of the other options
        //add value event listener
        //region
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses.clear();
                for (DataSnapshot expenseSnap : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnap.getValue(Expense.class);

                    expenses.add(expense);

                    //create expense app fragment with ArrayList<Expense> that is stored in MainActivity as an argument
                    //region
                    //store the expense app that is the main screen/activity in a global variable
                    expenseAppFragment = new ExpenseAppFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ExpenseAppFragment.EXPENSES_LIST_KEY, expenses);
                    expenseAppFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, expenseAppFragment, "expense_app_fragment")
                            .commit();
                    //endregion

                    Log.d("demo", expense.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public void goToShowExpenseFragment(Expense expense, int index) {
        //replace expense app fragment with show expense fragment (null is the default BackStack)
        //region
        ShowExpenseFragment showExpenseFragment = new ShowExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowExpenseFragment.EXPENSE_KEY, expense);
        bundle.putInt(ShowExpenseFragment.INDEX_KEY, index);
        showExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, showExpenseFragment, "show_expense_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void removeExpenseFromList(ExpenseAdapter adapter, int index) {
        //use the adapter passed from the expense app fragment to remove from the list
        adapter.remove(adapter.getItem(index));
        //use the adapter to notify the change
        adapter.notifyDataSetChanged();
        //store the new list in the database
        myRef.setValue(expenses);
    }

    @Override
    public void addExpenseToList(Expense expense) {
        //add teh expense passed from the add expense fragment
        expenses.add(expense);
        //store the new list in the database
        myRef.setValue(expenses);
        //pop the expense app fragment off the stack
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToEditExpenseFragment(Expense expense, int index) {
        //first pop the expense app fragment of the stack
        //then replace the expense app fragment with edit expense fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().popBackStack();

        EditExpenseFragment editExpenseFragment = new EditExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowExpenseFragment.EXPENSE_KEY, expense);
        bundle.putInt(ShowExpenseFragment.INDEX_KEY, index);
        editExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editExpenseFragment, "edit_expense_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void updateExpenseInList(Expense expense, int index) {
        //replace the old expense with the new one passed from the edit expense fragment
        expenses.set(index, expense);
        //store the new list in the database
        myRef.setValue(expenses);
        //use the global expense app fragment variable to notify the adapter
        expenseAppFragment.notifyAdapter();
        //pop the expense app fragment off the stack
        getSupportFragmentManager().popBackStack();
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
