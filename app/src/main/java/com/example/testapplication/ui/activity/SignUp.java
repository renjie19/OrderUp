package com.example.testapplication.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.testapplication.R;
import com.example.testapplication.shared.util.FirebaseUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends BaseActivity {

    private TextInputLayout fnameLayout;
    private TextInputLayout lnameLayout;
    private TextInputLayout locationLayout;
    private TextInputLayout contactLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initializeComponents();
    }

    private void initializeComponents() {
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
                mAuth.createUserWithEmailAndPassword(String.valueOf(emailLayout.getEditText().getText()), String.valueOf(passwordLayout.getEditText().getText()))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                addToFireStore();
                            } else {
                                showMessage("Registration Failed");
                                hideProgressBar();
                            }
                        });
            } else {
                showMessage("Fill in all fields!");
                hideProgressBar();
            }

        };
    }
    //TODO refactor the hiding of progress dialog on one declaration only
    private void addToFireStore() {
        Map<String, Object> accountInfo = mapInfo();
        UserInfo user = FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(0);
        db.collection("Users")
                .document(user.getUid())
                .set(accountInfo)
                .addOnSuccessListener(documentReference -> {
                    hideProgressBar();
                    showMessage("Registered Successfully");
                    finish();
                })
                .addOnFailureListener(e -> {
                    showMessage("Error Saving Failed");
                    hideProgressBar();
                });
    }

    private Map<String, Object> mapInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("firstName", String.valueOf(fnameLayout.getEditText().getText()));
        info.put("lastName", String.valueOf(lnameLayout.getEditText().getText()));
        info.put("location", String.valueOf(locationLayout.getEditText().getText()));
        info.put("contact", String.valueOf(contactLayout.getEditText().getText()));
        info.put("email", String.valueOf(emailLayout.getEditText().getText()));
        info.put("token", FirebaseUtil.INSTANCE.getToken());
        return info;
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
}
