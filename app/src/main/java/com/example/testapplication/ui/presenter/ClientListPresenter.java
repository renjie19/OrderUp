package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.ui.views.ClientListView;

import java.util.List;

public class ClientListPresenter {
    private AccountService accountService;
    private ClientListView clientListView;
    private FirebaseService firebaseService;

    public ClientListPresenter(ClientListView clientListView) {
        this.accountService = (AccountService) ServiceFactory.INSTANCE.create(ServiceEnum.ACCOUNT);
        this.clientListView = clientListView;
        this.firebaseService = new FirebaseServiceImpl();
    }

    public List<Client> getListOfClients() {
        return accountService.getAccount().getClients();
    }

    public void addClient(Client client) {
        accountService.addClient(client);
    }

    public void deleteClient(Client removedClient) {
        accountService.deleteClient(removedClient);
    }

    public void restoreClient(int itemIndex, Client removedClient) {
        accountService.restoreClient(itemIndex, removedClient);
    }

    public void initListeners() {
        firebaseService.initializeListeners();
    }
}
