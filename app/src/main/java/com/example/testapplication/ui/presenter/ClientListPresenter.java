package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.util.FirebaseUtil;
import com.example.testapplication.ui.views.ClientListView;

import java.util.List;

public class ClientListPresenter {
    private AccountService accountService;
    private ClientListView clientListView;

    public ClientListPresenter(ClientListView clientListView) {
        this.accountService = (AccountService) ServiceFactory.INSTANCE.create(ServiceEnum.ACCOUNT);
        this.clientListView = clientListView;
    }

    public List<Client> getListOfClients() {
         List<Client> list = accountService.getAccount().getClients();
         Client client = new Client();
         client.setName("Test");
         client.setLocation("TEst3234");
         list.add(client);
         return list;
    }

    public void addClient(Client client) {
        accountService.addClient(client);
    }

}
