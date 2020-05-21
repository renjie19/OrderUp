package com.example.testapplication.core.service;

import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.callback.OnComplete;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;

public interface FirebaseService {
    void createUser(Account account, OnComplete<Exception> onComplete);
    void updateUser(Account account);
    void getAccount(String id, CallBack callBack);
    void login(String email, String password, OnComplete<Object> onComplete);
    void signUp(String email, String password, OnComplete<Exception> onComplete);
    void logout();
    void createOrder(Order order, CallBack callBack);
    void updateOrder(Order order, CallBack callBack);
    void removeClient(Client client);
    void addClient(Client client);
    void initializeListeners();
}
