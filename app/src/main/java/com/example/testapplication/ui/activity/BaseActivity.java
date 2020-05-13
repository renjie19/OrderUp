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
}
