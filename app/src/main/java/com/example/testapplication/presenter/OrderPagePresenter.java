package com.example.testapplication.presenter;

import com.example.testapplication.pojo.Client;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.pojo.Order;
import com.example.testapplication.repository.OrderRepository;

import java.util.List;

public class OrderPagePresenter {
    private final OrderRepository orderRepository = OrderRepository.getInstance();


    public List<Order> getOrders(Client client){
        return orderRepository.getOrders(client);
    }
}
