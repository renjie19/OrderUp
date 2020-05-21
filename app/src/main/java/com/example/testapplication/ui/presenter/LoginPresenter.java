package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.AccountServiceImpl;
import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.ui.views.LoginView;

public class LoginPresenter {
    private AccountService accountService;
    private FirebaseService firebaseService;
    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.accountService = new AccountServiceImpl();
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public void login(String email, String password) {
        firebaseService.login(email, password, result -> {
            if(result.isSuccessful()) {
                clearAndSaveUserData(result.getResult());
            } else {
                view.onFailure(result.getException().getMessage());
            }
        });
    }

    private void clearAndSaveUserData(String userId) {
        accountService.clearData();
        firebaseService.getAccount(userId, result -> {
            boolean successful = result.isSuccessful();
            if (successful) {
                view.onSuccess(result.getResult());
            } else {
                view.onFailure(result.getException().getMessage());
            }
        });
    }
}
