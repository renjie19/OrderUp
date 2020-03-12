package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.ui.views.ClientListView;

public class ClientListPresenter {
    private AccountService accountService;
    private ClientListView clientListView;

    public ClientListPresenter(ClientListView clientListView) {
        this.accountService = (AccountService) ServiceFactory.INSTANCE.create(ServiceEnum.ACCOUNT);
        this.clientListView = clientListView;
    }

    public void getListOfClients() {
        clientListView.setListOfClients(accountService.getAccount().getClients());
    }

    public void addClient(Client client) {
        accountService.addClient(client);
    }
}
