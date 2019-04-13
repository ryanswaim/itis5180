package com.group9.inclass10;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//In-Class10
//Group 9
//Rockford Stoller

public class SignUpFragment extends Fragment {

    OnSignUpFragmentInteractionListener mListener;

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;

    MainActivity mainActivity;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        getActivity().setTitle("Sign Up");

        mainActivity = (MainActivity) getActivity();

        firstNameEditText = view.findViewById(R.id.first_name_editText);
        lastNameEditText = view.findViewById(R.id.last_name_editText);
        emailEditText = view.findViewById(R.id.email_on_signup_editText);
        passwordEditText = view.findViewById(R.id.password_on_signup_editText);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_editText);

        view.findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "onClick: entered");

                if(firstNameEditText.length() > 0 && lastNameEditText.length() > 0
                        && emailEditText.length() > 0 && passwordEditText.length() > 5
                        && confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                    Log.d("demo", "if statement: entered");
                    new RegisterAsync().execute();
                } else {
                    if(firstNameEditText.length() <= 0) {
                        Toast.makeText(getActivity(), "Please enter your first name", Toast.LENGTH_LONG).show();
                    } else if(lastNameEditText.length() <= 0) {
                        Toast.makeText(getActivity(), "Please enter your last name", Toast.LENGTH_LONG).show();
                    } else if(emailEditText.length() <= 0) {
                        Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_LONG).show();
                    } else if(passwordEditText.length() <= 0) {
                        Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_LONG).show();
                    } else if(!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                        Toast.makeText(getActivity(), "Please ensure both passwords are the same", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLoginFragment();
            }
        });

        return view;
    }

    public class RegisterAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mainActivity.prefEditor.putString(getString(R.string.token_storage_key), s);
            mainActivity.prefEditor.commit();
            mainActivity.userToken = s;
            mListener.signedUp();
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("name", firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString())
                    .add("email", emailEditText.getText().toString())
                    .add("password", passwordEditText.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build();

            String tokenString = null;

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String json = response.body().string();

                JSONObject root = new JSONObject(json);
                if(root.getBoolean("auth") == true) {
                    tokenString = root.getString("token");
                }

                Log.d("demo", "tokenString: " + tokenString);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return tokenString;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragment.OnSignUpFragmentInteractionListener) {
            mListener = (SignUpFragment.OnSignUpFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignUpFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSignUpFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToLoginFragment();
        void signedUp();
    }
}
