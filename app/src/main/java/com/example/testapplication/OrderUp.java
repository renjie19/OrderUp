package com.example.testapplication;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.testapplication.activity.BaseActivity;
import com.example.testapplication.util.TokenGenerator;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OrderUp extends Application {

    private static  Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        TokenGenerator.getToken();

    }
}
