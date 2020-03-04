package com.example.testapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.testapplication.R;

public class Management extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        initializeComponents();
    }

    private void initializeComponents() {
        findViewById(R.id.accountBtn).setOnClickListener(v -> startActivity(new Intent(this, AccountManagement.class)));
    }
}
