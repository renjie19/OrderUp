package com.example.testapplication.service;

import com.example.testapplication.pojo.Account;
import com.example.testapplication.pojo.Client;
import com.example.testapplication.repository.AccountRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Override
    public Account addClient(Client client) {
        Map<String, Client> clientMap = new HashMap<>();
        Account account = repository.getAccount();
        for(Client savedClient : account.getClients()) {
            clientMap.put(savedClient.getToken(), savedClient);
        }
        clientMap.put(client.getToken(), client);
        account.setClients(new RealmList<>());
        for(Map.Entry<String, Client> uniqueClients : clientMap.entrySet()) {
            account.getClients().add(uniqueClients.getValue());
        }
        return repository.save(account);
    }

}
