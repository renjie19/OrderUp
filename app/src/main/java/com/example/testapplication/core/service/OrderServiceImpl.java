package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.OrderRepository;
import com.example.testapplication.core.repository.RepositoryEnum;
import com.example.testapplication.core.repository.RepositoryFactory;
import com.example.testapplication.shared.pojo.Order;

class OrderServiceImpl implements OrderService {
    private OrderRepository repository;

    public OrderServiceImpl() {
        this.repository = (OrderRepository) RepositoryFactory.INSTANCE.create(RepositoryEnum.ORDER);
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
