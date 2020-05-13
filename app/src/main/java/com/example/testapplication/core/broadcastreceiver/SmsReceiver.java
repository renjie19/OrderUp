package com.example.testapplication.core.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.testapplication.core.repository.RepositoryEnum;
import com.example.testapplication.core.repository.RepositoryFactory;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.core.repository.OrderRepository;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private final OrderRepository orderRepository = (OrderRepository) RepositoryFactory.INSTANCE.create(RepositoryEnum.ORDER);
    Map<Long, String> messageDetails = new HashMap<>();
    String address;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    messageDetails.put(messages[i].getTimestampMillis(), messages[i].getOriginatingAddress());
                    address = messages[i].getOriginatingAddress();
                }
                try {
                    String completeMessage = "";
                    for (SmsMessage message : messages) {
                        completeMessage += message.getMessageBody();
                    }
                    Order order = new GsonBuilder().create().fromJson(completeMessage, Order.class);
                    orderRepository.save(order);
                    deleteReceivedParsedMessage(context, completeMessage);
                } catch (Exception e) {
                    Log.d("PARSE", "CANNOT PARSE: " + messages[0].getMessageBody());
                }
            }
        }
    }

    private void deleteReceivedParsedMessage(Context context, String message) {
        new Thread(() -> {
            try {
                sleep(5000);
                Cursor c = context.getContentResolver().query(
                        Uri.parse("content://sms/inbox"), new String[]{
                                    "_id", "thread_id", "address", "person", "date", "body"}, null, null, null);
                while (c.moveToNext()) {
                    long id = c.getLong(0);
                    String address = c.getString(2);
                    String text = c.getString(5);
                    if (text.equalsIgnoreCase(message)) {
                        context.getContentResolver().delete(Uri.parse("content://sms/"+id),"date=?",new String[]{c.getString(4)});
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).run();

    }
}
