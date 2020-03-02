package com.example.testapplication.broadcastreceiver;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.testapplication.OrderUp;
import com.example.testapplication.activity.BaseActivity;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.repository.ConsumerRepository;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;


public class FireBaseService extends FirebaseMessagingService {
    private final String TAG = "FIREBASE";
    String message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Message Received");
        try{
            Consumer consumer = new GsonBuilder().create().fromJson(remoteMessage.getData().get("message"), Consumer.class);
            if(consumer != null) {
                ConsumerRepository.getInstance().save(consumer);
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
