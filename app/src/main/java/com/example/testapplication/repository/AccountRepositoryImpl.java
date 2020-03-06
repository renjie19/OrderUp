package com.example.testapplication.repository;

import android.util.Log;

import com.example.testapplication.pojo.Account;
import com.example.testapplication.pojo.Client;

import io.realm.Realm;

class AccountRepositoryImpl extends AccountRepository {
    private final String TAG = "AccountRepositoryImpl";

    @Override
    public Account save(Account account) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.refresh();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(account);
            realm.commitTransaction();
            Log.d(TAG, "SAVE SUCCESSFUL");
        } catch (Exception e) {
            Log.d(TAG, "save: " + e.getMessage());
        } finally {
            realm.close();
        }
        return account;
    }

    @Override
    public Account getAccount() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.refresh();
            return realm.copyFromRealm(realm.where(Account.class).findFirst());
        } catch (Exception e) {
            Log.d(TAG, "getAccount: " + e.getMessage());
        } finally {
            realm.close();
        }
        return null;
    }
}
