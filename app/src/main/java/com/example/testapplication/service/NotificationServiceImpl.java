package com.example.testapplication.service;

import android.util.Log;

import com.example.testapplication.activity.ConsumerOrderList;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.rest.FirebaseRestApi;
import com.example.testapplication.views.ConsumerOrderListView;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationServiceImpl implements NotificationService {
    private final String TAG = "NotificationService";
    private final String serverKey = "AAAAmszXoYc:APA91bEM7zNtOKLc0TzPUM1nk43MjG7YgT3bJWllrECuWQIxnHqLiCkI9l53VAR3SqCM4tCYf36sGffBc_UFo-0UIrH_mCok3lVnhTsP4y5_DFbqNMmC886lupBN0mJdwMzsfvLfjo94";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://us-central1-orderup-c4548.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();
    private FirebaseRestApi restApi = retrofit.create(FirebaseRestApi.class);
    private ConsumerOrderListView view;

    public NotificationServiceImpl(ConsumerOrderList consumerOrderList) {
        this.view = consumerOrderList;
    }

    @Override
    public void sendNotification(Consumer consumer) {
        restApi.sendNotification(new GsonBuilder().create().toJson(consumer), consumer.getToken(),
                "Bearer " + serverKey).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                    view.onFailure("Something went wrong...Please check your internet and try again");
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                    view.onSuccess("Order Sent");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                view.onFailure(t.getMessage());
            }
        });
    }
}
