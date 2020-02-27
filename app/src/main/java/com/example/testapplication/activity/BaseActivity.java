package com.example.testapplication.activity;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static BaseActivity baseActivity;

    public static BaseActivity getInstance(){
        if(baseActivity == null){
            baseActivity = new BaseActivity();
        }
        return baseActivity;
    }
    private ProgressBar mProgressBar;

    protected void showProgressBar(){
        if(mProgressBar == null) {
            mProgressBar = new ProgressBar(this);
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar(){
        if(mProgressBar != null){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
