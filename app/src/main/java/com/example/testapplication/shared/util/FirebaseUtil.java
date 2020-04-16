package com.example.testapplication.shared.util;

import android.util.Log;

import com.example.testapplication.shared.pojo.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public enum FirebaseUtil {
    INSTANCE;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private final String TAG = getClass().getSimpleName();
    private String token;

    public FirebaseFirestore getFirebaseDb() {
        return mFireStore;
    }

    public FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    public void saveClientToAccount(Client client) {
        String uid = mAuth.getUid();
        DocumentReference reference = mFireStore.document(String.format("Users/%s", client.getUid()));
        mFireStore.collection("Users")
                .document(uid)
                .update("clients", FieldValue.arrayUnion(reference))
                .addOnSuccessListener(aVoid -> {
                    Log.i(TAG, "updateListOfClients: Add Success");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updateListOfClients: " + e.getMessage());
                });
    }

    public String getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            token = task.getResult().getToken();
        });
        return token;
    }
}
