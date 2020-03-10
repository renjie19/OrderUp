package com.example.testapplication.ui.presenter;

import com.example.testapplication.ui.activity.OrderPage;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.service.NotificationService;
import com.example.testapplication.core.service.NotificationServiceImpl;
import com.example.testapplication.ui.views.OrderPageView;

public class OrderPagePresenter {
    private NotificationService service;
    private OrderPageView view;

    public OrderPagePresenter(OrderPageView orderPage){
        if(service == null) {
            service = new NotificationServiceImpl(orderPage);
        }
    }

    public void sendNotification(Order order) {
        try{
            service.sendNotification(order);
        } catch (Exception e) {

        }
    }
}
