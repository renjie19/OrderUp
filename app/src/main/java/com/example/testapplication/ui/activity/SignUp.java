package com.example.testapplication.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.testapplication.R;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.util.FirebaseUtil;
import com.example.testapplication.ui.presenter.SignUpPresenter;
import com.example.testapplication.ui.views.SignUpView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;

public class SignUp extends BaseActivity implements SignUpView {

    private TextInputLayout fnameLayout;
    private TextInputLayout lnameLayout;
    private TextInputLayout locationLayout;
    private TextInputLayout contactLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initializeComponents();
    }

    private void initializeComponents() {
        this.presenter = new SignUpPresenter(this);
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.fnameLayout = findViewById(R.id.fnameLayout);
        this.lnameLayout = findViewById(R.id.lnameLayout);
        this.locationLayout = findViewById(R.id.locationLayout);
        this.contactLayout = findViewById(R.id.contactLayout);
        this.emailLayout = findViewById(R.id.emailLayout);
        this.confirmLayout = findViewById(R.id.confirmLayout);
        this.passwordLayout = findViewById(R.id.passwordLayout);
        findViewById(R.id.btnRegister).setOnClickListener(save());
    }

    private View.OnClickListener save() {
        return v -> {
            showProgressBar("Creating Account....");
            if (checkValidFields()) {
                String email = String.valueOf(emailLayout.getEditText().getText());
                String password = String.valueOf(passwordLayout.getEditText().getText());
                presenter.signUp(buildAccount(), email, password);
            } else {
                showMessage("Fill in all fields!");
                hideProgressBar();
            }

        };
    }

    private Account buildAccount() {
        Account account = new Account();
        account.setFirstName(String.valueOf(fnameLayout.getEditText().getText()));
        account.setLastName(String.valueOf(lnameLayout.getEditText().getText()));
        account.setLocation(String.valueOf(locationLayout.getEditText().getText()));
        account.setContactNumber(String.valueOf(contactLayout.getEditText().getText()));
        account.setEmail(String.valueOf(emailLayout.getEditText().getText()));
        account.setToken(FirebaseUtil.INSTANCE.getToken());
        account.setClients(new RealmList<>());
        return account;
    }

    private boolean checkValidFields() {
        return validate(fnameLayout, lnameLayout, locationLayout, contactLayout, emailLayout) || verifyPassword();
    }

    private boolean verifyPassword() {
        boolean isValidPassword = validate(passwordLayout);
        boolean isValidConfirmPassword = validate(confirmLayout);
        return (isValidPassword && isValidConfirmPassword)
                || !String.valueOf(passwordLayout.getEditText().getText()).equals(String.valueOf(confirmLayout.getEditText().getText()));
    }

    private boolean validate(TextInputLayout... view) {
        boolean isValid = true;
        for (TextInputLayout v : view) {
            EditText editText = v.getEditText();
            if (editText.getText() == null || String.valueOf(editText.getText()).isEmpty()) {
                v.setError("Required Field");
                isValid = false;
            }
        }
        return isValid;
    }

    @Override
    public void onCreateAccountSuccess() {
        showMessage("Account Created");
        finish();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
        showMessage(message);
    }
}
