package com.example.testfornyblesoft.adapter;

import com.example.testfornyblesoft.pojo.Weather;

import java.util.Date;

public class SavedData {
    private double lat;
    private double lon;
    private String address;
    private String city;
    private Date date;
    private Weather weather;

    public SavedData(double lat, double lon, String address, String city, Date date, Weather weather) {
        this.lat = lat;
        this.lon = lon;
        this.address = address;
        this.city = city;
        this.date = date;
        this.weather = weather;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public Date getDate() {
        return date;
    }

    public Weather getWeather() {
        return weather;
    }
}
