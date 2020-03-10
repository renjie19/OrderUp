package com.example.testapplication.core.service;

import com.example.testapplication.shared.pojo.Order;

public interface OrderService {

    Order save(Order order);

    Order updateOrder(Order order);
}
