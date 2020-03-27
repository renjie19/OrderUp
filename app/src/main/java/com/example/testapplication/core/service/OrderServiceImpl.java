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

    //TODO for verification for the behavior when receiving orders
    @Override
    public Order update(Order updatedOrder) {
//        Order orderCopy = repository.getOrder(updatedOrder.getId());
//        orderCopy.setItems(updatedOrder.getItems());
//        return repository.save(orderCopy);
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
