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
        findViewById(R.id.settingsBtn).setOnClickListener(v -> startActivity(new Intent(this, Settings.class)));
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
}
