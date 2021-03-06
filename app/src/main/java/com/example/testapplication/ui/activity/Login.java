package com.example.testapplication.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.testapplication.R;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.ui.presenter.LoginPresenter;
import com.example.testapplication.ui.views.LoginView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Login extends BaseActivity implements LoginView {
    public static final String NO_RECORD = "There is no user record corresponding to this identifier. The user may have been deleted.";
    public static final String BAD_EMAIL_FORMAT = "The email address is badly formatted.";
    public static final String INVALID_PASSWORD = "The password is invalid or the user does not have a password.";
    private TextView passwordField;
    private TextView usernameField;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LoginPresenter presenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeComponents();
        checkForPermissions();
        verifyCurrentUser();
    }

    private void verifyCurrentUser() {
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, ClientList.class));
        }
    }

    private void checkForPermissions() {
        int PERMISSION_ALL = 1;
        ArrayList<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECEIVE_SMS);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.SEND_SMS);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_SMS);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }
        if (permissions.size() != 0) {
            String[] permissionsForRequest = new String[permissions.size()];
            permissionsForRequest = permissions.toArray(permissionsForRequest);
            ActivityCompat.requestPermissions(this, permissionsForRequest, PERMISSION_ALL);
        }
    }

    private void initializeComponents() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.presenter = new LoginPresenter(this);
        this.usernameField = findViewById(R.id.usernameField);
        this.passwordField = findViewById(R.id.passwordField);
        this.emailLayout = findViewById(R.id.emailLayout);
        this.passwordLayout = findViewById(R.id.passwordLayout);
        this.usernameField.setOnClickListener(v -> this.emailLayout.setError(""));
        this.passwordField.setOnClickListener(v -> this.passwordLayout.setError(""));
        findViewById(R.id.loginButton).setOnClickListener(view -> verifyLogin());
        findViewById(R.id.signUpBtn).setOnClickListener(view -> openSignUpPage());
    }

    private void openSignUpPage() {
        startActivity(new Intent(this, SignUp.class));
    }

    private void verifyLogin() {
        showProgressBar("Logging In...");
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if(!username.isEmpty() && !password.isEmpty()) {
            presenter.login(username, password);
        } else {
            setErrorOnField();
            showMessage("FILL IN ALL FIELDS");
            hideProgressBar();
        }

    }

    private String getCause(String cause) {
        switch (cause) {
            case NO_RECORD: {
                return "LOGIN FAILED";
            }
            case BAD_EMAIL_FORMAT: {
                emailLayout.setError("BAD EMAIL FORMAT");
                return "BAD EMAIL FORMAT";
            }
            case INVALID_PASSWORD: {
                passwordLayout.setError("INCORRECT PASSWORD");
                return "INCORRECT PASSWORD";
            }
            default: {
               return "ERROR OCCURED";
            }
        }

    }

    private void setErrorOnField() {
        if(String.valueOf(usernameField.getText()).isEmpty()
                || Patterns.EMAIL_ADDRESS.matcher(String.valueOf(usernameField.getText())).matches()) {
            emailLayout.setError("Provide valid email");
        }
        if(String.valueOf(passwordField.getText()).isEmpty()) {
            passwordLayout.setError("Required Field");
        }
    }

    @Override
    public void onSuccess(Object object) {
        hideProgressBar();
        startActivity(new Intent(this, ClientList.class));
    }

    @Override
    public void onFailure(Object message) {
        hideProgressBar();
        showMessage(getCause((String) message));
    }
}
