package com.example.testapplication.ui.presenter;

import android.util.Log;

import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.core.service.NotificationServiceImpl;
import com.example.testapplication.core.service.OrderService;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.service.NotificationService;
import com.example.testapplication.ui.views.OrderPageView;

public class OrderPagePresenter {
    private NotificationService notificationService;
    private OrderService orderService;
    private FirebaseService firebaseService;

    private OrderPageView view;
    private final String TAG = "OrderPagePresenter";

    public OrderPagePresenter(OrderPageView view){
        if(notificationService == null) {
            notificationService = new NotificationServiceImpl(view);
        }
        if(orderService == null) {
            orderService = (OrderService) ServiceFactory.INSTANCE.create(ServiceEnum.ORDER);
        }
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public void sendNotification(Order order) {
        try{
            notificationService.sendNotification(order);
        } catch (Exception e) {
            Log.d(TAG, "sendNotification: ");
        }
    }

    public Order saveOrder(Order order) {
        firebaseService.createOrder(order, view);
        return orderService.save(order);
    }

    public Order updateOrder(Order order) {
        firebaseService.updateOrder(order, view);
        return orderService.update(order);
    }
}
