package com.example.testapplication;

import android.app.Application;
import android.content.Context;

import com.example.testapplication.shared.util.FirebaseUtil;

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
        FirebaseUtil.INSTANCE.getToken();
        realm = Realm.getDefaultInstance();
    }
}
