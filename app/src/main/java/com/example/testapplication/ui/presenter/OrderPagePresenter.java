package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.core.service.OrderService;
import com.example.testapplication.core.service.OrderServiceImpl;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.ui.views.OrderPageView;

public class OrderPagePresenter {
    private OrderService orderService;
    private FirebaseService firebaseService;

    private OrderPageView view;
    private final String TAG = "OrderPagePresenter";

    public OrderPagePresenter(OrderPageView view){
        if(orderService == null) {
            orderService = new OrderServiceImpl();
        }
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public Order saveOrder(Order order) {
        if(order.getId() == null) {
            return save(order);
        }
        return update(order);
    }

    private Order save(Order order) {
        firebaseService.createOrder(order, task -> {
            if(task.isSuccessful()) {
                view.onSuccess("Order Sent");
            } else {
                view.onFailure(task.getException().getMessage());
            }
        });
        return orderService.save(order);
    }

    private Order update(Order order) {
        firebaseService.updateOrder(order, result -> {
            if(result.isSuccessful()) {
                view.onSuccess("Order Updated");
            } else {
                view.onFailure(result.getException().getMessage());
            }
        });
        return orderService.update(order);
    }
}
