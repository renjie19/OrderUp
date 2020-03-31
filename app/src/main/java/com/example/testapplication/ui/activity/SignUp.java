package com.example.testapplication.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends BaseActivity {

    private TextView txtFname;
    private TextView txtLname;
    private TextView txtLocation;
    private TextView txtContact;
    private TextView txtEmail;
    private TextView txtPassword;
    private TextView txtConfirmPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initializeComponents();
    }

    private void initializeComponents() {
        this.mAuth = FirebaseAuth.getInstance();
        this.txtFname = findViewById(R.id.txtFname);
        this.txtLname = findViewById(R.id.txtLname);
        this.txtLocation = findViewById(R.id.txtLocation);
        this.txtContact = findViewById(R.id.txtContact);
        this.txtEmail = findViewById(R.id.txtEmail);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        findViewById(R.id.btnRegister).setOnClickListener(save());
    }

    private View.OnClickListener save() {
        return v -> {
            showProgressBar("Creating Account....");
            if(checkValidFields()){
                //save to localdb user management
                mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                showMessage("Registered Successfully");
                                finish();
                            } else {
                                showMessage("Registration Failed");
                            }
                        });
            } else {
                showMessage("Fill in all fields!");
                hideProgressBar();
            }
        };
    }

    private boolean checkValidFields() {
        return validate(txtFname) &&
        validate(txtLname) &&
        validate(txtLocation) &&
        validate(txtContact) &&
        validate(txtEmail) &&
        validate(txtPassword) &&
        validate(txtConfirmPassword) &&
        verifyPassword();
    }

    private boolean verifyPassword() {
        if(!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
            txtPassword.setTextColor(getResources().getColor(R.color.error));
            txtConfirmPassword.setTextColor(getResources().getColor(R.color.error));
            return false;
        }
        return true;
    }

    private boolean validate(TextView view) {
        if(view.getText() == null || view.getText().toString().isEmpty()) {
            view.setHintTextColor(getResources().getColor(R.color.error));
            return false;
        } else {
            view.setHintTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
            return true;
        }
    }
}
