package com.example.testapplication.util;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public enum TokenGenerator {
    INSTANCE;
    private static String token;

    public static String getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            token = task.getResult().getToken();
        });
        return token;
    }
}
