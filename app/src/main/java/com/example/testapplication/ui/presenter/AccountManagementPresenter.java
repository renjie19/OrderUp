package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountServiceImpl;
import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.ui.views.AccountManagementViews;

public class AccountManagementPresenter {
    private AccountService service;
    private AccountManagementViews view;
    private FirebaseService firebaseService;

    public AccountManagementPresenter(AccountManagementViews view) {
        this.service = new AccountServiceImpl();
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public void save(Account account) {
        firebaseService.updateUser(account);
        service.save(account);
        view.hideProgressBar();
        view.showMessage("Save Success..");
        view.exitPage();
    }

    public void getAccount() {
        view.loadAccount(service.getAccount());
    }
}
