package com.example.testapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.testapplication.util.FirebaseToken;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OrderUp extends Application {

    private static  Context sContext;
    private static Realm realm;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        FirebaseToken.INSTANCE.getToken();
        realm = Realm.getDefaultInstance();
    }
}
