package com.example.testapplication.core.service;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;

public interface AccountService {

    void save(Account account);

    Account getAccount();

    Account update(Account account);

    Account addClient(Client client);

    void clearData();
}
