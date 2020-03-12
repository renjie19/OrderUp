package com.example.testapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.testapplication.R;

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
    }
}
