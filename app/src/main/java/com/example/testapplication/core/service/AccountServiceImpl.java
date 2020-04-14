package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.OrderRepository;
import com.example.testapplication.core.repository.RepositoryEnum;
import com.example.testapplication.core.repository.RepositoryFactory;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.core.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;

class AccountServiceImpl implements AccountService {
    private AccountRepository repository;

    public AccountServiceImpl() {
        this.repository = (AccountRepository) RepositoryFactory.INSTANCE.create(RepositoryEnum.ACCOUNT);
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

    @Override
    public void clearData() {
        repository.clearData();
    }

}
