package com.example.alyx.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;


import server.proxy.ClientException;
import server.proxy.ServerProxy;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.LoginResult;
import server.result.RegisterResult;


public class LoginFragment extends Fragment implements Caller {
    // Input fields
    private EditText mServerHostField;
    private EditText mServerPortField;
    private EditText mUserNameField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;


    // Buttons
    private Button mLoginButton;
    private Button mRegisterButton;
    private RadioButton mMaleButton;
    private RadioButton mFemaleButton;

    // Strings to hold fields
    private String serverHost = "";
    private String serverPort = "";
    private String userName = "";
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String gender = "";

    // Requests
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Get text from Server Host field
        mServerHostField = (EditText) v.findViewById(R.id.serverHostInput);
        mServerHostField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serverHost = s.toString();
            }
        });

        // Get text from ServerPortField
        mServerPortField = (EditText) v.findViewById(R.id.serverPortInput);
        mServerPortField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serverPort = s.toString();
            }
        });

        // Get text from userName
        mUserNameField = (EditText) v.findViewById(R.id.userNameInput);
        mUserNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = s.toString();
            }
        });

        // Get text from password
        mPasswordField = (EditText) v.findViewById(R.id.passwordInput);
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
            }
        });

        // Get text from mFirstNameField
        mFirstNameField = (EditText) v.findViewById(R.id.firstNameInput);
        mFirstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstName = s.toString();
            }
        });

        // Get text from last name field
        mLastNameField = (EditText) v.findViewById(R.id.lastNameInput);
        mLastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastName = s.toString();
            }
        });

        // Get text from email
        mEmailField = (EditText) v.findViewById(R.id.emailInput);
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = s.toString();
            }
        });

        // Set up radio buttons
        mMaleButton = (RadioButton) v.findViewById(R.id.maleButton);
        mMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                gender = "male";
            }
        });
        mFemaleButton = (RadioButton) v.findViewById(R.id.femaleButton);
        mFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                gender = "female";
            }
        });

        // Check for login button press
        // Set up Login & Register Buttons
        mLoginButton = (Button) v.findViewById(R.id.loginButton);
        mRegisterButton = (Button) v.findViewById(R.id.registerButton);

        // Listen for user input on buttons
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // put fields into a login request
                if(putFieldsInLoginRequest()) {

                    // LOGIN
                    new LoginTask().execute(LoginFragment.this);
                }

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // put fields into a register request
                if(putFieldsInRegisterRequest()) {
                    // REGISTER!
                    new RegisterTask().execute(LoginFragment.this);
                }

            }
        });


        return v;
    }

    /**
     * Prints the toast.
     * @param toast String to print
     */
    @Override
    public void printToast(String toast){
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Re-downloads the Person & Event data from the database
     * @param toMapNext If true, will go to the MapFragment next. Else, will stay here.
     */
    @Override
    public void resync(boolean toMapNext){
        new ResyncTask().execute(this);
    }

    /**
     * Returns the fragment manager
     * @return the fragment manager
     */
    @Override
    public FragmentManager getThisFragmentManager(){
        return getFragmentManager();
    }

    /**
     * Sets login complete, going to the Map.
     */
    private void setLoginComplete() {
        ((MainActivity)getActivity()).loginComplete();
    }

    /**
     * Adds fields from the UI into a LoginRequest
     * @return true if succeeded, false if there was missing UI data.
     */
    private boolean putFieldsInLoginRequest(){
        if(userName.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your username", Toast.LENGTH_SHORT).show();

        } else if (password.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loginRequest = new LoginRequest(userName, password);
            return true;
        }
        return false;
    }

    /**
     * Adds fields from the UI into a RegisterRequest
     * @return true if succeeded, false if there was missing UI data.
     */
    private boolean putFieldsInRegisterRequest(){
        if(userName.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your username", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your password", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(getActivity(), "You forgot to enter your email", Toast.LENGTH_SHORT).show();
        } else if (firstName.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your first name", Toast.LENGTH_SHORT).show();
        } else if (lastName.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your last name", Toast.LENGTH_SHORT).show();
        } else if (gender.equals("")){
            Toast.makeText(getActivity(), "You forgot to enter your gender", Toast.LENGTH_SHORT).show();
        } else {
            registerRequest = new RegisterRequest(userName,password,
                    email,firstName,lastName,gender);
            return true;
        }
        return false;
    }

    /**
     * Logs a user in and updates the database.
     */
    public class LoginTask extends AsyncTask<Caller, Integer, Boolean> {
        /** Login result, contains the result of the login attempt */
        private LoginResult loginResult = new LoginResult();
        /** Access the model */
        private Model model = Model.instanceOf();

        /** Access the database */
        private ServerProxy mProxy = ServerProxy.server();

        /** Set the success/failure message */
        private String loginToast;

        /** Activity/Fragment that called this task */
        private Caller caller;

        protected Boolean doInBackground(Caller... urls) {
            // Get Activity
            this.caller = urls[0];

            // Login thru database
            try {
                this.loginResult = mProxy.login(loginRequest);
            } catch (ClientException e){
                loginResult.setMessage(e.getMessage());
                return false;
            }

            // If no error, set user in Model!
            if(loginResult.getMessage() == null || loginResult.getMessage().equals("")){
                model.setCurrentUser(loginResult.getUserName());
                return true;
            } else {
                return false;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            // progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Boolean success) {
            if(success){
                // If success, print login succeed, sync data, and go to map!
                loginToast = "Login succeeded!";
                caller.printToast(loginToast);
                setLoginComplete();
                caller.resync(true);
            } else {
                // If error, tell us about it!
                caller.printToast(loginResult.getMessage());
            }
        }
    }


    /**
     * Registers a user and logs them in.
     */
    public class RegisterTask extends AsyncTask<Caller, Integer, Boolean> {
        /** Register result, contains the result of the register attempt */
        private RegisterResult registerResult = new RegisterResult();

        /** Access the model */
        private Model model = Model.instanceOf();

        /** Access the database */
        private ServerProxy mProxy = ServerProxy.server();

        /** Set the success/failure message */
        private String registerToast;

        /** Activity/Fragment that called this task */
        private Caller caller;

        protected Boolean doInBackground(Caller... urls) {
            // Get Activity
            this.caller = urls[0];

            // Register thru database
            try {
                registerResult = mProxy.register(registerRequest);
            } catch (ClientException e){
                registerResult.setMessage(e.getMessage());
                return false;
            }

            // If no error, set User in the model.
            if(registerResult.getMessage() == null || registerResult.getMessage().equals("")){
                model.setCurrentUser(registerResult.getUserName());
                return true;
            } else {
                return false;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            // progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Boolean success) {
            // If success, print register succeed, sync data, and go to map!
            if(success){
                registerToast = "Register succeeded!";
                caller.printToast(registerToast);
                caller.resync(true);
                setLoginComplete();
            } else {
                // If error, tell us about it!
                caller.printToast(registerResult.getMessage());
            }
        }
    }
}
