package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.OrderRepository;
import com.example.testapplication.core.repository.OrderRepositoryImpl;
import com.example.testapplication.shared.pojo.Order;

public class OrderServiceImpl implements OrderService {
    private OrderRepository repository;

    public OrderServiceImpl() {
        this.repository = new OrderRepositoryImpl();
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public Order update(Order updatedOrder) {
        return save(updatedOrder);
    }

    @Override
    public void manageReceivedOrder(Order order) {
        Order orderFromDb = repository.getOrder(order.getId());
        if(orderFromDb == null) {
            save(order);
        } else {
            update(order);
        }
    }

    @Override
    public Order getOrder(String id) {
        return repository.getOrder(id);
    }
}
