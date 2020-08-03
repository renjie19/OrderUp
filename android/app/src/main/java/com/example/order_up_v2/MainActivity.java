package com.example.order_up_v2;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;


import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "sendSms";

    private MethodChannel.Result callResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler(
                (call, result) -> {
                    if(call.method.equals("send")){
                        String num = call.argument("phone");
                        String msg = call.argument("msg");
                        sendSMS(num,msg,result);
                    }else{
                        result.notImplemented();
                    }
                });
    }

    private void sendSMS(String phoneNo, String msg,MethodChannel.Result result) {
        try {
            PendingIntent sent = PendingIntent.getBroadcast(this, 0, new Intent("SENT"), 0);
            ArrayList<PendingIntent> sentPendingIntent = new ArrayList<>();
            sentPendingIntent.add(sent);
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                   try {
                       Log.d(this.getClass().getName(), "onReceive: received");
                       if (getResultCode() == Activity.RESULT_OK) {
                           result.success("Success");
                       } else {
                           result.error(String.valueOf(getResultCode()),"Sms Not Sent","");
                       }
                   } catch (Exception e) {
                       Log.d(this.getClass().getName(), "onReceive: " + e.getMessage());
                   }
                }
            }, new IntentFilter("SENT"));
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> completeMessage = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(phoneNo, null,
                    completeMessage, sentPendingIntent, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.error("Err","Sms Not Sent",ex.getMessage());
        }
    }

}
