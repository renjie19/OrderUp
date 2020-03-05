package com.example.testapplication.service;

import com.example.testapplication.pojo.Account;

public abstract class AccountService {
    private static AccountService service;

    protected AccountService() {
    }

    public static AccountService getInstance() {
        if(service == null) {
            service = new AccountServiceImpl();
        }
        return service;
    }

    public abstract void save(Account account);

    public abstract Account getAccount();

    public abstract Account update(Account account);
}
