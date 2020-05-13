package com.example.testapplication.core.service;

import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;

public interface FirebaseService {
    void createUser(Account account, CallBack callBack);
    void updateUser(Account account);
    void getAccount(String id, CallBack callBack);
    void login(String email, String password, CallBack callBack);
    void signUp(String email, String password, CallBack callBack);
    void logout();
    void createOrder(Order order, CallBack callBack);
    void updateOrder(Order order, CallBack callBack);
    void removeClient(Client client);
    void addClient(Client client);
    void initializeListeners(CallBack callBack);
}
