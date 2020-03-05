package com.example.testapplication.service;

import com.example.testapplication.pojo.Account;
import com.example.testapplication.repository.AccountRepository;

import io.realm.RealmList;

class AccountServiceImpl extends AccountService {
    private AccountRepository repository;

    public AccountServiceImpl() {
        this.repository = AccountRepository.getInstance();
    }

    @Override
    public void save(Account account) {
        repository.save(account);
    }

    @Override
    public Account getAccount() {
        Account account = repository.getAccount();
        if(account == null) {
            account = new Account();
            account.setClients(new RealmList<>());
        }
        return account;
    }

    @Override
    public Account update(Account account) {
        return null;
    }

}
