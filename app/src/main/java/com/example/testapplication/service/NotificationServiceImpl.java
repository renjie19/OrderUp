package com.example.testapplication.service;

import android.util.Log;

import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.rest.FirebaseRestApi;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationServiceImpl implements NotificationService {
    private final String TAG = "NotificationService";
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://us-central1-orderup-c4548.cloudfunctions.net/").addConverterFactory(GsonConverterFactory.create()).build();
    FirebaseRestApi restApi = retrofit.create(FirebaseRestApi.class);
    @Override
    public void sendNotification(Consumer consumer) {
        restApi.sendNotification(new GsonBuilder().create().toJson(consumer), consumer.getToken()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()) {
                    onFailure(null, null);
                } else {
                    Log.d(TAG, "onResponse: "+ response.message());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
