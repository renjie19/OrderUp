package com.example.testapplication.presenter;

import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.service.NotificationService;
import com.example.testapplication.service.NotificationServiceImpl;

public class OrderListPresenter {
    private NotificationService service;

    public OrderListPresenter(){
        if(service == null) {
            service = new NotificationServiceImpl();
        }
    }

    public void sendNotification(Consumer consumer) {
        service.sendNotification(consumer);
    }
}
