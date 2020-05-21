package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.callback.OnComplete;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.ui.views.LoginView;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginPresenter {
    private AccountService accountService = (AccountService) ServiceFactory.INSTANCE.create(ServiceEnum.ACCOUNT);
    private FirebaseService firebaseService;
    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.firebaseService = new FirebaseServiceImpl();
        this.view = view;
    }

    public void login(String email, String password) {
        firebaseService.login(email, password, result -> {
            if(result instanceof String) {
                accountService.clearData();
                firebaseService.getAccount((String) result, view);
            } else {
                view.onFailure(((Exception)result).getMessage());
            }
        });
    }
}
