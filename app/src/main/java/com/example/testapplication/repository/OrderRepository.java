package com.example.testapplication.repository;

import com.example.testapplication.pojo.Client;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.pojo.Order;

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

    public abstract Consumer save(Order order);
    public abstract List<Order> getOrders(Client client);
}
