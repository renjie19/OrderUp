package com.example.testapplication.core.broadcastreceiver;

import android.util.Log;

import androidx.annotation.NonNull;
import com.example.testapplication.core.service.NotificationService;
import com.example.testapplication.core.service.NotificationServiceImpl;
import com.example.testapplication.shared.pojo.Order;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;


public class NotificationReceiver extends FirebaseMessagingService {
    private final String TAG = "FIREBASE";
    private final NotificationService notificationService = new NotificationServiceImpl(null);

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Message Received");
        try{
            Order order = new GsonBuilder().create().fromJson(remoteMessage.getData().get("message"), Order.class);
            if(order != null) {
                notificationService.manageReceivedOrder(order);
                Log.d(TAG, "Save Successful");
            }
        } catch (Exception e){
            Log.d(TAG, "Could not parse message");
        }
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
