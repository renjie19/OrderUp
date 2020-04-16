package com.example.testapplication.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.testapplication.R;
import com.example.testapplication.shared.Preferences;
import com.example.testapplication.shared.enums.PrefParamEnum;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.util.FirebaseUtil;
import com.example.testapplication.shared.util.QrGenerator;
import com.example.testapplication.ui.presenter.AccountManagementPresenter;
import com.example.testapplication.ui.views.AccountManagementViews;
import com.google.gson.GsonBuilder;

public class AccountManagement extends BaseActivity implements AccountManagementViews {
    private TextView userName;
    private TextView lastName;
    private TextView location;
    private TextView phoneNumber;
    private TextView email;
    private ToggleButton modeBtn;
    private AccountManagementPresenter presenter;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_management);
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
        this.email = findViewById(R.id.emailField);
        this.modeBtn = findViewById(R.id.mode);

        findViewById(R.id.saveBtn).setOnClickListener(v -> {
            try {
                showProgressBar("Saving Account...Please Wait...");
                presenter.save(buildAccount());
            } catch (Exception e) {
                hideProgressBar();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.qrBtn).setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            ImageView qrCode = new ImageView(this);
            qrCode.setImageBitmap(QrGenerator.INSTANCE.getQrCode(new GsonBuilder().create().toJson(account)));
            dialog.setView(qrCode).show();
        });

        this.modeBtn.setChecked(Preferences.getMode());
        this.modeBtn.setOnCheckedChangeListener((buttonView, isChecked) -> Preferences.setPref(PrefParamEnum.MODE.name(), isChecked));


    }

    private Account buildAccount() throws Exception {
        if(!fieldsAreInvalid()) {
            account.setFirstName(userName.getText().toString());
            account.setLastName(lastName.getText().toString());
            account.setLocation(location.getText().toString());
            account.setContactNumber(phoneNumber.getText().toString());
            account.setToken(FirebaseUtil.INSTANCE.getToken());
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
            this.account.getClients().clear();
            this.userName.setText(account.getFirstName());
            this.lastName.setText(account.getLastName());
            this.location.setText(account.getLocation());
            this.phoneNumber.setText(account.getContactNumber());
            this.email.setText(account.getEmail());
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void exitPage() {
        finish();
    }
}
