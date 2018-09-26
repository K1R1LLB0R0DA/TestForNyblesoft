package com.example.testfornyblesoft.api;

import com.example.testfornyblesoft.pojo.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<Weather> getWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid, @Query("units") String units);
}
