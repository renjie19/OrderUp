package com.example.testapplication;

import android.app.Application;
import android.content.Context;

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
