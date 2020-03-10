package com.example.testapplication.core.repository;

import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Consumer;
import com.example.testapplication.shared.pojo.Order;

import java.util.List;

public abstract class OrderRepository {
    private static OrderRepository repository;

    protected OrderRepository(){}

    public static OrderRepository getInstance(){
        if(repository == null){
            repository = new OrderRepositoryImpl();
        }
        return repository;
    }

    public abstract Order save(Order order);
    public abstract List<Order> getOrders(Client client);
    public abstract Order getOrder(String id);
    public abstract Order update(Order order);
}
