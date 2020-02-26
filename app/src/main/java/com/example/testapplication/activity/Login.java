package com.example.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.R;

public class Login extends AppCompatActivity {
    private TextView passwordField;
    private TextView usernameField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeComponents();
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
