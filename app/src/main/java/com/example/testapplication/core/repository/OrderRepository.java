package com.example.testapplication.core.repository;

import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);
    List<Order> getOrdersByClient(Client client);
    void removeOrdersByClient(Client client);
    Order getOrder(String id);
    @Deprecated
    Order update(Order order);
}
