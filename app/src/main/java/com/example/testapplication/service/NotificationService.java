package com.example.testapplication.service;

import com.example.testapplication.pojo.Order;

public interface NotificationService {
    void sendNotification(Order order);
}
