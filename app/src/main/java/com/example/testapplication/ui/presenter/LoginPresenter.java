package com.example.testapplication.ui.presenter;

import com.example.testapplication.core.service.AccountService;
import com.example.testapplication.core.service.ServiceEnum;
import com.example.testapplication.core.service.ServiceFactory;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.pojo.Account;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginPresenter {
    private AccountService accountService = (AccountService) ServiceFactory.INSTANCE.create(ServiceEnum.ACCOUNT);

    public boolean checkIfUserHasExistingData(String id) {
        Account account = accountService.getAccount();
        return account.getEmail() != null && account.getEmail().equals(id);
    }

    public void clearDataAndReplace(DocumentSnapshot result, String uid, CallBack callBack) {
        accountService.saveAccountFromSnapshot(result, uid, callBack);
    }
}
