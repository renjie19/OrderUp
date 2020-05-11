package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.shared.callback.CallBack;
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
        firebaseService.signUp(email, password, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                createUserAccount(account);
            }

            @Override
            public void onFailure(Object object) {
                view.onFailure((String) object);
            }
        });
    }

    private void createUserAccount(Account account) {
        firebaseService.createUser(account, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                view.onCreateAccountSuccess();
            }

            @Override
            public void onFailure(Object object) {
                view.onFailure((String) object);
            }
        });
    }
}
