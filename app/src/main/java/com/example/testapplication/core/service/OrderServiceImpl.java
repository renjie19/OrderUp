package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.OrderRepository;
import com.example.testapplication.shared.pojo.Order;

class OrderServiceImpl implements OrderService {
    private OrderRepository repository;

    public OrderServiceImpl() {
        this.repository = OrderRepository.getInstance();
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return repository.update(order);
    }
}
