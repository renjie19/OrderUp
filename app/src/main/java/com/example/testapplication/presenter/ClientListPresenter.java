package com.example.testapplication.presenter;

import com.example.testapplication.service.AccountService;
import com.example.testapplication.views.ClientListView;

public class ClientListPresenter {
    private AccountService accountService;
    private ClientListView clientListView;

    public ClientListPresenter(ClientListView clientListView) {
        this.accountService = AccountService.getInstance();
        this.clientListView = clientListView;
    }

    public void getListOfClients() {
        clientListView.setListOfClients(accountService.getAccount().getClients());
    }
}
