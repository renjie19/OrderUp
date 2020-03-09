package com.example.testapplication.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.testapplication.R;

import java.util.ArrayList;

public class Login extends BaseActivity{
    private TextView passwordField;
    private TextView usernameField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeComponents();
        checkForPermissions();
    }

    private void checkForPermissions() {
        int PERMISSION_ALL = 1;
        ArrayList<String> permissions = new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.RECEIVE_SMS);
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.SEND_SMS);
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.READ_SMS);
        }
        if(permissions.size() != 0){
            String[] permissionsForRequest = new String[permissions.size()];
            permissionsForRequest = permissions.toArray(permissionsForRequest);
            ActivityCompat.requestPermissions(this, permissionsForRequest, PERMISSION_ALL);
        }




    }

    private void initializeComponents() {
        this.usernameField = findViewById(R.id.usernameField);
        this.passwordField = findViewById(R.id.passwordField);
        findViewById(R.id.loginButton).setOnClickListener(view -> verifyLogin());
    }

    private void verifyLogin() {
        startActivity(new Intent(this, MainPage.class));
    }
}
