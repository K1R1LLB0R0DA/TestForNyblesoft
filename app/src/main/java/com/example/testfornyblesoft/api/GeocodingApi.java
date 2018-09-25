package com.example.testfornyblesoft.api;

import com.example.testfornyblesoft.pojo.Geocoding;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApi {
    @GET("reverse.php")
    Call<Geocoding> getLocation(@Query("key") String key, @Query("lat") double lat, @Query("lon") double lon, @Query("format") String format);
}
