package com.example.testapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.testapplication.R;

public class Login extends BaseActivity{
    private TextView passwordField;
    private TextView usernameField;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeComponents();
        checkForPermissions();
    }

    private void checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
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
