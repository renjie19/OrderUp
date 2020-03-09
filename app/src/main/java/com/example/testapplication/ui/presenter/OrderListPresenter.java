package com.example.testapplication.ui.presenter;

import com.example.testapplication.ui.activity.ConsumerOrderList;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.service.NotificationService;
import com.example.testapplication.core.service.NotificationServiceImpl;
import com.example.testapplication.ui.views.ConsumerOrderListView;

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
