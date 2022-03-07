package com.example.m8gmap.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {
    @GET("json?")
    Call<ModelApi> getData(@Query("lat") String lat, @Query("lng") String lng);
}
