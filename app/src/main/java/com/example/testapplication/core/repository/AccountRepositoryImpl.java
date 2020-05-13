package com.example.testapplication.core.repository;

import android.util.Log;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;

import java.util.List;

import io.realm.Realm;

class AccountRepositoryImpl implements AccountRepository {
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

    @Override
    public void clearData() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transaction -> transaction.deleteAll());
    }

    @Override
    public List<Client> getClients() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Client.class).findAll();
    }
}
