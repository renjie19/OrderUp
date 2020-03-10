package com.example.testapplication.core.service;

import com.example.testapplication.shared.pojo.Order;

public interface NotificationService {
    void sendNotification(Order order);
    void manageReceivedOrder(Order order);
}
