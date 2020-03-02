package com.example.testapplication.rest;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FirebaseRestApi {
    @POST("sendRequest")
    Call<String> sendNotification(@Query("text") String consumerJson,@Query("token") String token);
}
