package com.example.testapplication.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.repository.ConsumerRepository;
import com.google.gson.GsonBuilder;

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private final ConsumerRepository consumerRepository = ConsumerRepository.getInstance();

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                try{
                    Consumer consumer = new GsonBuilder().create().fromJson(messages[0].getMessageBody(), Consumer.class);
                    consumerRepository.save(consumer);

                } catch (Exception e){
                    Log.d("PARSE", "CANNOT PARSE: "+ messages[0].getMessageBody());
                }
            }
        }
    }
}
