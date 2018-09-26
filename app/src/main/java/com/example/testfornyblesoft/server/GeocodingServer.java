package com.example.testfornyblesoft.server;

import com.example.testfornyblesoft.api.GeocodingApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeocodingServer {
    private static final String BASE_URL = "https://eu1.locationiq.com/v1/";
    private static GeocodingServer geocodingServer;
    private GeocodingApi geocodingApi;
    private Retrofit retrofit;

    private GeocodingServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geocodingApi = retrofit.create(GeocodingApi.class);
    }

    public static GeocodingServer getInstance() {
        if (geocodingServer == null) {
            geocodingServer = new GeocodingServer();
        }
        return geocodingServer;
    }

    public GeocodingApi getApiCategory() {
        return geocodingApi;
    }
}
