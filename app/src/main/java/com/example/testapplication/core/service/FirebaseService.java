package com.example.testapplication.core.service;

import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.callback.OnComplete;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.shared.pojo.CustomTask;

public interface FirebaseService {
    void createUser(Account account, OnComplete<CustomTask<Account>> onComplete);
    void updateUser(Account account);
    void getAccount(String id, OnComplete<CustomTask<Account>> customTask);
    void login(String email, String password, OnComplete<CustomTask<String>> onComplete);
    void signUp(String email, String password, OnComplete<CustomTask> onComplete);
    void logout();

    void createOrder(Order order, OnComplete<CustomTask<String>> task);
    void updateOrder(Order order, OnComplete<CustomTask<String>> task);

    void removeClient(Client client);
    void addClient(Client client);

    void initializeListeners();
}
