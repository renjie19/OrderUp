package com.example.testapplication.activity;

import android.app.ProgressDialog;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
}
