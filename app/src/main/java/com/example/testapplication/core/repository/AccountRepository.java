package com.example.testapplication.core.repository;

import com.example.testapplication.shared.pojo.Account;

public interface AccountRepository {

    Account save(Account account);

    Account getAccount();

    void clearData();
}
