package com.example.testapplication.util;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public enum TokenGenerator {
    INSTANCE;

    public static void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            Log.d("TOKEN", "getToken: "+task.getResult().getToken());
        });
    }
}
