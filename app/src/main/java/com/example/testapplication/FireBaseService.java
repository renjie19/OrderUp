package com.example.testapplication;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.testapplication.activity.BaseActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FireBaseService extends FirebaseMessagingService {
    private final String TAG = "FIREBASE";
    String message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Message Received");
        message = remoteMessage.getNotification().getBody();
//        Toast.makeText(BaseActivity.getsContext(), message, Toast.LENGTH_LONG).show();
    }
}
