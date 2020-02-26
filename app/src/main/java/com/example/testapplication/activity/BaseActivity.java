package com.example.testapplication.activity;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected static Context sContext;

    public static Context getsContext() {
        return sContext;
    }
}
