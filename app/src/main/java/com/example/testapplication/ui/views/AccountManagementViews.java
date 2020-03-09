package com.example.testapplication.ui.views;

import com.example.testapplication.shared.pojo.Account;

public interface AccountManagementViews {
    void hideProgressBar();

    void loadAccount(Account account);

    void showMessage(String message);
}
