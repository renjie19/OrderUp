package com.example.testapplication.ui.presenter;

import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.repository.OrderRepository;

import java.util.List;

public class OrderTrailPresenter {
    private final OrderRepository orderRepository = OrderRepository.getInstance();


    public List<Order> getOrders(Client client){
        return orderRepository.getOrders(client);
    }
}
