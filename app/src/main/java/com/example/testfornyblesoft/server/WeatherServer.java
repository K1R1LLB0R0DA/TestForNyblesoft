package com.example.testfornyblesoft.server;

import com.example.testfornyblesoft.api.WeatherApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherServer {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static WeatherServer weatherServer;
    private WeatherApi weatherApi;
    private Retrofit retrofit;

    private WeatherServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(WeatherApi.class);
    }

    public static WeatherServer getInstance() {
        if (weatherServer == null) {
            weatherServer = new WeatherServer();
        }
        return weatherServer;
    }

    public WeatherApi getWeatherApi() {
        return weatherApi;
    }
}
