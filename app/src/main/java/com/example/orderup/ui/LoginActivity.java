package com.example.orderup.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.orderup.R;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();
    }

    private void initializeComponents() {
        this.et_email = findViewById(R.id.et_email);
        this.et_password = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(v -> {

        });
    }
}
