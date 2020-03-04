package com.example.testapplication.service;

import com.example.testapplication.pojo.Account;
import com.example.testapplication.repository.AccountRepository;

class AccountServiceImpl extends AccountService {
    private AccountRepository repository;

    public AccountServiceImpl() {
        this.repository = AccountRepository.getInstance();
    }

    @Override
    public Account save(Account account) {
        return repository.save(account);
    }

    @Override
    public Account getAccount() {
        return repository.getAccount();
    }
}
