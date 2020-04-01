package com.example.testapplication.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.example.testapplication.R;

import java.util.ArrayList;

public class MainPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initializeComponents();
    }

    private void initializeComponents() {
        findViewById(R.id.accountBtn).setOnClickListener(v -> startActivity(new Intent(this, AccountManagement.class)));
        findViewById(R.id.clientsBtn).setOnClickListener(v -> startActivity(new Intent(this, ClientList.class)));
    }
}
