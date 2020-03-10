package com.example.testapplication.shared.util;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;

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
}
