package com.example.testapplication.views;

import com.example.testapplication.pojo.Account;

public interface AccountManagementViews {
    void hideProgressBar();

    void loadAccount(Account account);

    void showMessage(String message);
}
