package com.example.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Data.DataCache;
import Proxy.ServerProxy;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.FamilyResult;
import result.LoginResult;
import result.RegisterResult;

public class LoginFragment extends Fragment {

    private static final String LOG_TAG = "LoginFragment";

    private EditText serverHost;
    private EditText serverPort;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private Button registerButton;
    private Button loginButton;
    private RadioGroup genderButton;
    private RadioButton currGender;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private DataCache instance;
    private String currGenderString;
    private Listener listener;

    private String title;


    public LoginFragment() {
        // Required empty public constructor
    }

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreateView(...)");
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        Log.i(LOG_TAG, "setting up edit texts");
        serverHost = view.findViewById(R.id.serverHostEditText);
        serverHost.addTextChangedListener(watcher);

        serverPort = view.findViewById(R.id.serverPortEditText);
        serverPort.addTextChangedListener(watcher);

        username = view.findViewById(R.id.usernameEditText);
        username.addTextChangedListener(watcher);

        password = view.findViewById(R.id.passwordEditText);
        password.addTextChangedListener(watcher);

        firstName = view.findViewById(R.id.firstNameEditText);
        firstName.addTextChangedListener(watcher);

        lastName = view.findViewById(R.id.lastNameEditText);
        lastName.addTextChangedListener(watcher);

        email = view.findViewById(R.id.emailEditText);
        email.addTextChangedListener(watcher);

        genderButton = view.findViewById(R.id.genderGroup);

        registerButton = view.findViewById(R.id.registerButton);

        loginButton = view.findViewById(R.id.loginButton);

        currGenderString = "m";
        genderButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check) {
                currGender = view.findViewById(check);
                switch(check) {
                    case R.id.male:
                       currGenderString = "m";
                       break;
                    case R.id.female:
                        currGenderString = "f";
                }
            }
        });

        Log.i(LOG_TAG, "setting login  click listener");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        boolean success = bundle.getBoolean("success");
                        instance = DataCache.getInstance();

                        if (success) {
                            Toast.makeText(getActivity(), "Logged in: " + instance.getCurrPerson().getFirstName() + " " + instance.getCurrPerson().getLastName(), Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.notifyDone();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                Log.i(LOG_TAG, "start login task");
                LoginTask task = new LoginTask(loginRequest, serverHost.getText().toString().trim(), serverPort.getText().toString().trim(), handler);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        Log.i(LOG_TAG, "setting register click listener");
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Log.i(LOG_TAG, "handling message");
                        Bundle bundle = message.getData();
                        boolean success = bundle.getBoolean("success");
                        instance = DataCache.getInstance();

                        if (success) {
                            Toast.makeText(getActivity(), "Registered: " + instance.getCurrPerson().getFirstName() + " " + instance.getCurrPerson().getLastName(), Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.notifyDone();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Register error", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                Log.i(LOG_TAG, "start register task");
                RegisterTask task = new RegisterTask(registerRequest, serverHost.getText().toString().trim(), serverPort.getText().toString().trim(), handler);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        return view;
    }

   private final TextWatcher watcher = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

       }

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            loginButtonListener();
       }

       @Override
       public void afterTextChanged(Editable editable) {

       }
   };

    private void loginButtonListener() {
            Log.i(LOG_TAG, "in login button listener");
         String serverPortString = serverPort.getText().toString().trim();
         String serverHostString = serverHost.getText().toString().trim();
         String userNameString = username.getText().toString().trim();
         String passwordString = password.getText().toString().trim();
         String firstNameString = firstName.getText().toString().trim();
         String lastNameString = lastName.getText().toString().trim();
         String emailString = email.getText().toString().trim();

        boolean registerEnable = !serverHostString.isEmpty() && !serverPortString.isEmpty() && !userNameString.isEmpty()
                && !passwordString.isEmpty() && !firstNameString.isEmpty() && !lastNameString.isEmpty() && !emailString.isEmpty();

        boolean loginEnable = !serverHostString.isEmpty() && !serverPortString.isEmpty() && !userNameString.isEmpty() && !passwordString.isEmpty() && firstNameString.isEmpty()
                && lastNameString.isEmpty() && emailString.isEmpty();

        Log.i(LOG_TAG, "setting button enablers");
        registerButton.setEnabled(registerEnable);
        loginButton.setEnabled(loginEnable);

        if (registerEnable) {
            registerRequest = new RegisterRequest(userNameString, passwordString, emailString, firstNameString, lastNameString, currGenderString);
        }
        if (loginEnable) {
            loginRequest = new LoginRequest(userNameString, passwordString);
        }
    }

    private class LoginTask implements Runnable {

        private final LoginRequest request;
        private final String serverHost;
        private final String serverPort;
        private final Handler handler;
        private DataCache instance;

        public LoginTask(LoginRequest log, String host, String port, Handler handle) {
            this.request = log;
            this.serverHost = host;
            this.serverPort = port;
            this.handler = handle;
        }

        @Override
        public void run() {
            Log.i(LOG_TAG, "calling serverProxy");
            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            instance = DataCache.getInstance();
            LoginResult result = proxy.login(request);
            if (instance.getPeople().isEmpty() || instance.getEvents().isEmpty()) {
                FamilyResult familyResult = proxy.getFamily();
                AllEventResult eventResult = proxy.getEvents();
                instance.addPeopleToCache(familyResult.getFamily());
                instance.addEventsToCache(eventResult.getData());
                instance.addPersonEventsToCache(familyResult.getFamily(), eventResult.getData());
                sendMessage(result.isSuccess() && familyResult.isSuccess() && eventResult.isSuccess());
            }
            else {
                sendMessage(result.isSuccess());
            }
        }


        private void sendMessage(boolean success) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            String key = "success";
            messageBundle.putBoolean(key, success);
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private class RegisterTask implements Runnable {

        private RegisterRequest request;
        private String serverHost;
        private String serverPort;
        private Handler handler;

        public RegisterTask(RegisterRequest reg, String host, String port, Handler handle) {
            this.request = reg;
            this.serverHost = host;
            this.serverPort = port;
            this.handler = handle;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            RegisterResult result = proxy.register(request);
            FamilyResult familyResult = proxy.getFamily();
            AllEventResult eventResult = proxy.getEvents();
            instance.addPeopleToCache(familyResult.getFamily());
            instance.addEventsToCache(eventResult.getData());
            sendMessage(result.isSuccess() && familyResult.isSuccess() && eventResult.isSuccess());
        }

        private void sendMessage(boolean success) {
            Log.i(LOG_TAG, "sending message");
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            String key = "success";
            messageBundle.putBoolean(key, success);
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }
}