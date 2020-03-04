package com.example.testapplication.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.pojo.Account;
import com.example.testapplication.presenter.AccountManagementPresenter;
import com.example.testapplication.views.AccountManagementViews;

public class AccountManagement extends BaseActivity implements AccountManagementViews {
    private TextView userName;
    private TextView lastName;
    private TextView location;
    private TextView phoneNumber;
    private AccountManagementPresenter presenter;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initializeComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getAccount();
    }

    private void initializeComponents() {
        presenter = new AccountManagementPresenter(this);
        this.userName = findViewById(R.id.userNameField);
        this.lastName = findViewById(R.id.lastNameField);
        this.location = findViewById(R.id.locationField);
        this.phoneNumber = findViewById(R.id.contactNoField);
        findViewById(R.id.saveBtn).setOnClickListener(v -> {
            try {
                showProgressBar("Saving Account...Please Wait...");
                presenter.save(buildAccount());
            } catch (Exception e) {
                hideProgressBar();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private Account buildAccount() throws Exception {
        if(!fieldsAreInvalid()) {
            if(account == null) {
                account = new Account();
            }
            account.setFirstName(userName.getText().toString());
            account.setLastName(lastName.getText().toString());
            account.setLocation(location.getText().toString());
            account.setContactNumber(phoneNumber.getText().toString());
            return account;
        }
        throw new Exception("Fill In All Fields...");
    }

    private boolean fieldsAreInvalid() {
        return this.userName.getText().toString().isEmpty() &&
                this.lastName.getText().toString().isEmpty() &&
                this.location.getText().toString().isEmpty() &&
                this.phoneNumber.getText().toString().isEmpty();
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
    }

    @Override
    public void loadAccount(Account account) {
        this.account = account;
        if(account != null) {
            this.userName.setText(account.getFirstName());
            this.lastName.setText(account.getLastName());
            this.location.setText(account.getLocation());
            this.phoneNumber.setText(account.getContactNumber());
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
