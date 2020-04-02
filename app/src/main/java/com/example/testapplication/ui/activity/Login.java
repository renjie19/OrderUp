package com.example.testapplication.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.testapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Login extends BaseActivity {
    private TextView passwordField;
    private TextView usernameField;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeComponents();
        checkForPermissions();
        verifyCurrentUser();
    }

    private void verifyCurrentUser() {
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainPage.class));
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
        this.usernameField = findViewById(R.id.usernameField);
        this.passwordField = findViewById(R.id.passwordField);
        this.emailLayout = findViewById(R.id.emailLayout);
        this.passwordLayout = findViewById(R.id.passwordLayout);
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
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            retrieveUserData();
                            startActivity(new Intent(this, MainPage.class));
                        } else {
                            showMessage("Login Failed");
                        }
                        hideProgressBar();
                    });
        } else {
            setErrorOnField();
            showMessage("Fill in fields");
            hideProgressBar();
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

    private void retrieveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        //check db if the saved user is same as the current user else download user data
        db.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            //clear data and load user data to realm
            showMessage((String) task.getResult().get("firstName"));
        }).addOnFailureListener(e -> {
           showMessage("Data retrieval Failed");
        });
    }
}
