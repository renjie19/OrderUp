package com.example.testapplication.core.service;

import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.google.firebase.firestore.DocumentSnapshot;

public interface AccountService {

    void save(Account account);

    Account getAccount();

    Account update(Account account);

    Account addClient(Client client);

    void deleteClient(Client client);

    void clearData();

    void saveAccountFromSnapshot(DocumentSnapshot snapshot, String id, CallBack callBack);

    void restoreClient(int itemIndex, Client removedClient);
}
