package com.example.testapplication.ui.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.testapplication.R;

public class BaseActivity extends AppCompatActivity {
    private static BaseActivity baseActivity;
    private static final String CHANNEL_ID = "0001";

    public static BaseActivity getInstance() {
        if (baseActivity == null) {
            baseActivity = new BaseActivity();
        }
        return baseActivity;
    }

    private ProgressDialog mProgressDialog;

    protected void showProgressBar(String message) {
        mProgressDialog = ProgressDialog.show(this, "", message, true);
    }

    protected void hideProgressBar() {
        if(mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

    protected void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void showNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "Test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.add_shopping_cart_48dp)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
