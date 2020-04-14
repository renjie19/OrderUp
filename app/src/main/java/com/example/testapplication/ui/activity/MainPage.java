package com.example.testapplication.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.example.testapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainPage extends BaseActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initializeComponents();
    }

    private void initializeComponents() {
        this.mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.accountBtn).setOnClickListener(v -> startActivity(new Intent(this, AccountManagement.class)));
        findViewById(R.id.clientsBtn).setOnClickListener(v -> startActivity(new Intent(this, ClientList.class)));
    }


}
