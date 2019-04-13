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

public class LoginFragment extends Fragment {

    OnLoginFragmentInteractionListener mListener;

    EditText emailEditText;
    EditText passwordEditText;

    MainActivity mainActivity;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle("Login");

        mainActivity = (MainActivity) getActivity();

        emailEditText = view.findViewById(R.id.email_editText);
        passwordEditText = view.findViewById(R.id.password_editText);

        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailEditText.length() > 0 && passwordEditText.length() > 0) {
                    new LoginAsync().execute();
                }
            }
        });

        view.findViewById(R.id.goTo_singup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSignUpFragment();
            }
        });

        return view;
    }

    public class LoginAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mainActivity.prefEditor.putString(getString(R.string.token_storage_key), s);
            mainActivity.prefEditor.commit();
            mainActivity.userToken = s;
            mListener.loggedIn();
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("email", emailEditText.getText().toString())
                    .add("password", passwordEditText.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login")
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
        if (context instanceof LoginFragment.OnLoginFragmentInteractionListener) {
            mListener = (LoginFragment.OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToSignUpFragment();
        void loggedIn();
    }
}
