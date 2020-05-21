package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.repository.OrderRepositoryImpl;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.repository.OrderRepository;

import java.util.List;

public class OrderTrailPresenter {
    private OrderRepository orderRepository;

    public OrderTrailPresenter() {
        this.orderRepository = new OrderRepositoryImpl();
    }

    public List<Order> getOrders(Client client){
        return orderRepository.getOrdersByClient(client);
    }
}
