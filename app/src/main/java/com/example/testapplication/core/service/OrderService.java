package com.example.testapplication.core.service;

import com.example.testapplication.shared.pojo.Order;

public interface OrderService {

    Order save(final Order order);

    Order update(final Order order);

    void manageReceivedOrder(final Order order);

    Order getOrder(final String id);


}
