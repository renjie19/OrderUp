package com.example.testapplication.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Text;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends BaseActivity {

    private TextView txtFname;
    private TextView txtLname;
    private TextView txtLocation;
    private TextView txtContact;
    private TextView txtEmail;
    private TextView txtPassword;
    private TextView txtConfirmPassword;
    private TextInputLayout txtInputLayoutPassword;
    private TextInputLayout txtInputLayoutConfirm;
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
        this.txtFname = findViewById(R.id.txtFname);
        this.txtLname = findViewById(R.id.txtLname);
        this.txtLocation = findViewById(R.id.txtLocation);
        this.txtContact = findViewById(R.id.txtContact);
        this.txtEmail = findViewById(R.id.txtEmail);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        this.txtInputLayoutConfirm = findViewById(R.id.txtInputLayoutConfirm);
        this.txtInputLayoutPassword = findViewById(R.id.txtInputLayoutPassword);
        findViewById(R.id.btnRegister).setOnClickListener(save());
    }

    private View.OnClickListener save() {
        return v -> {
            showProgressBar("Creating Account....");
            if (checkValidFields()) {
                mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
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
        info.put("firstName", String.valueOf(txtFname.getText()));
        info.put("lastame", String.valueOf(txtLname.getText()));
        info.put("location", String.valueOf(txtLocation.getText()));
        info.put("contact", String.valueOf(txtContact.getText()));
        info.put("email", String.valueOf(txtEmail.getText()));
        return info;
    }

    private boolean checkValidFields() {
        return validate(txtFname, txtLname, txtLocation, txtContact, txtEmail) || verifyPassword();
    }

    private boolean verifyPassword() {
        boolean isValidPassword = validate(txtInputLayoutPassword);
        boolean isValidConfirmPassword = validate(txtInputLayoutConfirm);
        return (isValidPassword && isValidConfirmPassword)
                || !String.valueOf(txtPassword.getText()).equals(String.valueOf(txtConfirmPassword.getText()));
    }

    private boolean validate(View... view) {
        boolean isValid = true;
        for (View v : view) {
            if (v instanceof TextView && (((TextView) v).getText() == null || String.valueOf(((TextView) v).getText()).isEmpty())) {
                ((TextView) v).setError("Required Field");
                isValid = false;
            }
            if (v instanceof TextInputLayout) {
                EditText editText = ((TextInputLayout) v).getEditText();
                if (editText.getText() == null || String.valueOf(editText.getText()).isEmpty()) {
                    ((TextInputLayout) v).setError("Required Field");
                    isValid = false;
                }
            }
        }
        return isValid;
    }
}
