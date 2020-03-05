package com.example.testapplication.presenter;

import com.example.testapplication.activity.ConsumerOrderList;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.pojo.Order;
import com.example.testapplication.service.NotificationService;
import com.example.testapplication.service.NotificationServiceImpl;
import com.example.testapplication.views.ConsumerOrderListView;

public class OrderListPresenter {
    private NotificationService service;
    private ConsumerOrderListView view;

    public OrderListPresenter(ConsumerOrderList consumerOrderList){
        if(service == null) {
            service = new NotificationServiceImpl(consumerOrderList);
        }
    }

    public void sendNotification(Order order) {
        try{
            service.sendNotification(order);
        } catch (Exception e) {

        }
    }
}
