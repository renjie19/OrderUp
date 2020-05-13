package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.repository.RepositoryEnum;
import com.example.testapplication.core.repository.RepositoryFactory;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.repository.OrderRepository;

import java.util.List;

public class OrderTrailPresenter {
    private final OrderRepository orderRepository = (OrderRepository) RepositoryFactory.INSTANCE.create(RepositoryEnum.ORDER);

    public List<Order> getOrders(Client client){
        return orderRepository.getOrdersByClient(client);
    }
}
