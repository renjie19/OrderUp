package com.example.testapplication.shared.util;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public enum AccountMapper {
    INSTANCE;

    public Client accountToClient(Account account) {
        Client client = new Client();
        client.setName(String.format("%s %s",account.getFirstName(),account.getLastName()));
        client.setLocation(account.getLocation());
        client.setContactNo(account.getContactNumber());
        client.setToken(account.getToken());
        return client;
    }

    public Account documentToAccount(DocumentSnapshot object, String id) {
        Account account = new Account();
        account.setId(id);
        account.setFirstName((String) object.get("firstName"));
        account.setLastName((String) object.get("lastName"));
        account.setLocation((String)object.get("location"));
        account.setContactNumber((String)object.get("contact"));
        account.setEmail((String)object.get("email"));
        return account;
    }
}
