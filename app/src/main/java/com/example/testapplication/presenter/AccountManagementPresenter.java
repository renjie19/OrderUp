package com.example.testapplication.presenter;

import com.example.testapplication.pojo.Account;
import com.example.testapplication.service.AccountService;
import com.example.testapplication.views.AccountManagementViews;

public class AccountManagementPresenter {
    private AccountService service;
    private AccountManagementViews view;

    public AccountManagementPresenter(AccountManagementViews view) {
        this.service = AccountService.getInstance();
        this.view = view;
    }

    public void save(Account account) {
        service.save(account);
        view.hideProgressBar();
        view.showMessage("Save Success..");
    }

    public void getAccount() {
        view.loadAccount(service.getAccount());
    }
}
