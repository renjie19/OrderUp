package com.example.testapplication.ui.presenter;

import android.util.Log;

import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.core.service.OrderService;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.ui.views.OrderPageView;

public class OrderPagePresenter {
    private OrderService orderService;
    private FirebaseService firebaseService;

    private OrderPageView view;
    private final String TAG = "OrderPagePresenter";

    public OrderPagePresenter(OrderPageView view){
        if(orderService == null) {
            orderService = (OrderService) ServiceFactory.INSTANCE.create(ServiceEnum.ORDER);
        }
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public Order saveOrder(Order order) {
        if(order.getId() == null) {
            firebaseService.createOrder(order, view);
            return orderService.save(order);
        }
        return updateOrder(order);
    }

    public Order updateOrder(Order order) {
        firebaseService.updateOrder(order, view);
        return orderService.update(order);
    }
}
