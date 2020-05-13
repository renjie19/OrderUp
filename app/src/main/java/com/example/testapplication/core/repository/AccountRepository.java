package com.example.testapplication.core.repository;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;

import java.util.List;

public interface AccountRepository {

    Account save(Account account);

    Account getAccount();

    void clearData();

    List<Client> getClients();
}
