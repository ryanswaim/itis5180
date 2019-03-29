package com.group9.homework05;

//Homework05
//Group 9
//Rockford Stoller
//Ryan Swaim

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnExpenseAppFragmentInteractionListener, AddOrEditExpenseFragment.OnAddExpenseFragmentInteractionListener {

    ArrayList<Expense> expenses = new ArrayList<>();

    public boolean isOnMainScreen = true;

    //create database variables
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenses");
    ExpenseAppFragment expenseAppFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Expense App");

        //get all expenses on start up and clear the list and repopulate it when values are set
        //I know this is a bad way to do it, but I am unsure how to not add multiples if I do not clear
        //and I am not sure of the other options
        //add value event listener
        //region
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //clear the array list and repopulate it
                expenses.clear();

                if(dataSnapshot.exists()) {
                    //if the data snapshot exists. meaning the location has a least one expense at start, after removal, or after adding an expense
                    for (DataSnapshot expenseSnap : dataSnapshot.getChildren()) {
                        Expense expense = expenseSnap.getValue(Expense.class);

                        expenses.add(expense);

                        Log.d("demo", expense.toString());
                    }
                }

                sortExpensesByDate();

                //create expense app fragment with ArrayList<Expense> that is stored in MainActivity as an argument
                //region
                //store the expense app that is the main screen/activity in a global variable
                expenseAppFragment = new ExpenseAppFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ExpenseAppFragment.EXPENSES_LIST_KEY, expenses);
                expenseAppFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, expenseAppFragment, "expense_app_fragment")
                        .commit();
                //endregion
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("demo", "onCancelled: databaseError "  + databaseError);
            }
        });
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isOnMainScreen) {
            inflater.inflate(R.menu.menu_with_dropdown, menu);
        } else {
            super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch statement for options menu
        //region
        switch (item.getItemId()) {
            case R.id.sort_by_cost_item:
                Collections.sort(expenses, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense o1, Expense o2) {
                        if(o1.amount > o2.amount) {
                            return 1;
                        } else if (o1.amount < o2.amount) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                expenseAppFragment.notifyAdapter();
                break;
            case R.id.sort_by_date_item:
                sortExpensesByDate();
                expenseAppFragment.notifyAdapter();
                break;
            case R.id.reset_all_item:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Reset Expenses")
                        .setMessage("Are you sure you want to reset all expenses?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                expenses.clear();
                                myRef.setValue(expenses);
                                Log.d("demo", "before notify ");
                                expenseAppFragment.notifyAdapter();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }
        //endregion
        return super.onOptionsItemSelected(item);
    }

    private void sortExpensesByDate() {
        Collections.sort(expenses, new Comparator<Expense>() {
            @Override
            public int compare(Expense o1, Expense o2) {
                if(o1.year < o2.year) {
                    return 1;
                } else if (o1.year == o2.year) {
                    if(o1.month < o2.month) {
                        return 1;
                    } else if(o1.month == o2.month) {
                        if(o1.day < o2.day) {
                            return 1;
                        } else if(o1.day == o2.day) {
                            return 0;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        });
    }

    public void goToEditExpenseFragment(Expense expense, int index) {
        //first pop the expense app fragment of the stack
        //then replace the expense app fragment with edit expense fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().popBackStack();

        AddOrEditExpenseFragment addOrEditExpenseFragment = new AddOrEditExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowExpenseFragment.EXPENSE_KEY, expense);
        bundle.putInt(ShowExpenseFragment.INDEX_KEY, index);
        addOrEditExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, addOrEditExpenseFragment, "add_or_edit_expense_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void goToAddExpenseFragment() {
        //replace expense app fragment with add expense fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddOrEditExpenseFragment(), "add_or_edit_expense_fragment")
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
    public void addExpenseToList(Expense expense) {
        //add teh expense passed from the add expense fragment
        expenses.add(expense);
        //store the new list in the database
        myRef.setValue(expenses);
        //use the global expense app fragment variable to notify the adapter
        //expenseAppFragment.notifyAdapter();
        //pop the expense app fragment off the stack
        getSupportFragmentManager().popBackStack();
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
