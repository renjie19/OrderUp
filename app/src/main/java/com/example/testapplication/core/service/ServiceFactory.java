package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.RepositoryEnum;

import java.util.HashMap;
import java.util.Map;

public enum ServiceFactory {
    INSTANCE;

    private Map<ServiceEnum, Object> services = new HashMap<>();

    public Object create(ServiceEnum service) {
        switch (service) {
            case ACCOUNT: {
                Object accountService = services.get(service);
                if(accountService == null) {
                    accountService = new AccountServiceImpl();
                    services.put(service, accountService);
                }
                return accountService;
            }
            case ORDER: {
                Object orderService = services.get(service);
                if(orderService == null) {
                    orderService = new OrderServiceImpl();
                    services.put(service, orderService);
                }
                return orderService;
            }
            case NOTIFICATION: {
//                Object notificationService = services.get(service);
//                if(notificationService == null) {
//                    notificationService = new NotificationServiceImpl();
//                    services.put(service.toString(), notificationService);
//                }
//                return notificationService;
            }
        }
        return null;
    }
}
