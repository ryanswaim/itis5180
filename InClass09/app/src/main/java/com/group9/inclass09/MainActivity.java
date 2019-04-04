package com.group9.inclass09;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

//InClass09
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener, SignUpFragment.OnSignUpFragmentInteractionListener,
        ContactsFragment.OnContactsFragmentInteractionListener, CreateContactFragment.OnCreateContactFragmentInteractionListener {

    ArrayList<Contact> userContacts = new ArrayList<>();

    public FirebaseUser currentUser = null;

    //create authentication variables
    private FirebaseAuth mAuth;

    //create storage variables
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    //create database variables
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    ContactsFragment contactsFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Login");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Contact contact = new Contact();
        //contact.email = "email@one.com";
        //userContacts.add(contact);

        //create expense app fragment with ArrayList<Expense> that is stored in MainActivity as an argument
        //region
        //store the expense app that is the main screen/activity in a global variable
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "login_fragment")
                .commit();
        //endregion
    }

    @Override
    public void loginAttempt(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("demo", "signInWithEmail: success");
                            currentUser = mAuth.getCurrentUser();

                            myRef = database.getReference(currentUser.getUid());

                            contactsFragment = new ContactsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ContactsFragment.CONTACT_LIST_KEY, userContacts);
                            contactsFragment.setArguments(bundle);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, contactsFragment, "contacts_fragment")
                                    .commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("demo", "signInWithEmail: failure", task.getException());
                            Toast.makeText(MainActivity.this, "Login was not successful.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void goToSignUpFragment() {
        //replace login fragment with sign up fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SignUpFragment(), "sign_up_fragment")
                //.addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void signUpAttempt(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("demo", "createUserWithEmail: success");
                            Toast.makeText(MainActivity.this, "User has been created", Toast.LENGTH_LONG).show();
                            currentUser = mAuth.getCurrentUser();

                            myRef = database.getReference(currentUser.getUid());

                            contactsFragment = new ContactsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ContactsFragment.CONTACT_LIST_KEY, userContacts);
                            contactsFragment.setArguments(bundle);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, contactsFragment, "contacts_fragment")
                                    .commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("demo", "createUserWithEmail: failure", task.getException());
                            Toast.makeText(MainActivity.this, "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void goToLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "login_fragment")
                .commit();
    }

    @Override
    public void goToCreateContactFragment() {
        //replace login fragment with sign up fragment (null is the default BackStack)
        //region
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CreateContactFragment(), "create_contacts_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void logoutAttempt() {
        mAuth.signOut();

        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "login_fragment")
                .commit();
    }

    @Override
    public void addContact(Contact contact) {
        userContacts.add(contact);
        contactsFragment.notifyAdapter();
        myRef.setValue(userContacts);
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