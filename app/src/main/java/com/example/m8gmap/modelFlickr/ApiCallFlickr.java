package com.example.m8gmap.modelFlickr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallFlickr {
    @GET("services/rest/")
    Call<ModelApiFlickr> getData(@Query("method") String method, @Query("api_key") String key, @Query("lat") String lat, @Query("lon") String lng, @Query("per_page") String number, @Query("format") String json);
}
