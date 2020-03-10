package com.example.testapplication.core.repository;

import com.example.testapplication.shared.pojo.Account;

public abstract class AccountRepository {
    private static AccountRepository repository;

    protected AccountRepository() {
    }

    public static AccountRepository getInstance(){
        if(repository == null) {
            repository = new AccountRepositoryImpl();
        }
        return repository;
    }

    public abstract Account save(Account account);

    public abstract Account getAccount();
}