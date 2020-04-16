package com.example.testapplication.shared.util;

import android.util.Log;

import com.example.testapplication.shared.pojo.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public enum FirebaseUtil {
    INSTANCE;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private final String TAG = getClass().getSimpleName();

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
                    Log.e(TAG, "updateListOfClients: " + e.getMessage() );
                });
    }

    public Client getClientFromDocumentReference(DocumentReference reference) {
        Map<String, Object> map = reference.get().getResult().getData();
        Client client = new Client();
        client.setUid(reference.getId());
        client.setName(String.valueOf(map.get("name")));
        client.setLocation(String.valueOf(map.get("location")));
        client.setContactNo(String.valueOf(map.get("contact")));
        client.setToken(String.valueOf(map.get("token")));
        return client;
    }
}
