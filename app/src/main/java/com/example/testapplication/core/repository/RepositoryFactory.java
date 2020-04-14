package com.example.testapplication.core.repository;

import java.util.HashMap;
import java.util.Map;

public enum RepositoryFactory {
    INSTANCE;

    private Map<RepositoryEnum, Object> services = new HashMap<>();

    public Object create(RepositoryEnum repo) {
        switch (repo) {
            case ORDER: {
                Object orderRepository = services.get(repo);
                if(orderRepository == null) {
                    orderRepository = new OrderRepositoryImpl();
                }
                return orderRepository;
            }
            case ACCOUNT: {
                Object accountRepository = services.get(repo);
                if(accountRepository == null) {
                    accountRepository = new AccountRepositoryImpl();
                }
                return accountRepository;
            }
        }
        return null;
    }
}
