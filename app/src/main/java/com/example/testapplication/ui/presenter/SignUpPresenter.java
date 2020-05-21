package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.callback.OnComplete;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.ui.views.SignUpView;

public class SignUpPresenter {
    private FirebaseService firebaseService;
    private SignUpView view;

    public SignUpPresenter(SignUpView view) {
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public void signUp(Account account, String email, String password) {
        firebaseService.signUp(email, password, exception -> {
            if(exception == null) {
                createUserAccount(account);
            } else {
                view.onFailure(exception.getMessage());
            }
        });
    }

    private void createUserAccount(Account account) {
        firebaseService.createUser(account, exception -> {
            if(exception == null) {
               view.onCreateAccountSuccess();
            } else {
                view.onFailure(exception.getMessage());
            }
        });
    }
}
