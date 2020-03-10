package com.example.testapplication.core.rest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FirebaseRestApi {
    @Headers("Content-Type: application/json")
    @POST("sendRequest")
    Call<String> sendNotification(@Query("text") String consumerJson, @Query("token") String token, @Header("Authorization") String auth);
}