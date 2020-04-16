package com.example.testapplication.shared.util;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

public enum AccountMapper {
    INSTANCE;

    public Client accountToClient(Account account) {
        Client client = new Client();
        client.setName(String.format("%s %s",account.getFirstName(),account.getLastName()));
        client.setLocation(account.getLocation());
        client.setContactNo(account.getContactNumber());
        client.setToken(account.getToken());
        client.setUid(account.getId());
        return client;
    }

    public Account documentToAccount(Map<String, Object> accountMap, String id) {
        Account account = new Account();
        account.setId(id);
        account.setFirstName((String) accountMap.get("firstName"));
        account.setLastName((String) accountMap.get("lastName"));
        account.setLocation((String)accountMap.get("location"));
        account.setContactNumber((String)accountMap.get("contact"));
        account.setEmail((String)accountMap.get("email"));
        account.setClients(new RealmList<>());
        return account;
    }
}
