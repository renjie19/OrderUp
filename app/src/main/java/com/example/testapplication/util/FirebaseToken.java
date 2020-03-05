package com.example.testapplication.util;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public enum FirebaseToken {
    INSTANCE;
    private String token;

    public String getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            token = task.getResult().getToken();
        });
        return token;
    }
}
