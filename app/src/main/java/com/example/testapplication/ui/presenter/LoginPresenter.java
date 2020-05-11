package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.FirebaseService;
import com.example.testapplication.core.service.FirebaseServiceImpl;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.callback.CallBack;
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

    public boolean checkIfUserHasExistingData(String id) {
        Account account = accountService.getAccount();
        return account.getEmail() != null && account.getEmail().equals(id);
    }

    public void clearDataAndReplace(DocumentSnapshot result, String uid, CallBack callBack) {
        accountService.saveAccountFromSnapshot(result, uid, callBack);
    }

    public void login(String email, String password) {
        firebaseService.login(email, password, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                accountService.clearData();
                firebaseService.getAccount((String) object, view);
            }

            @Override
            public void onFailure(Object object) {
                view.onFailure(object);
            }
        });
    }
}
