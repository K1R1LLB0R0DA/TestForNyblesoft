package com.example.testfornyblesoft.fragment;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.adapter.SavedData;
import com.example.testfornyblesoft.pojo.Geocoding;
import com.example.testfornyblesoft.pojo.Weather;
import com.example.testfornyblesoft.preference.Preferences;
import com.example.testfornyblesoft.server.GeocodingServer;
import com.example.testfornyblesoft.server.WeatherServer;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Response;


public class NavigationFragment extends Fragment {

    private OnNavigationFragmentInteractionListener mListener;
    private TextView tvLatitude, tvLongitude, tvAddress, tvTemperature, tvHumidity, tvPressure;
    private Location location;

    private Geocoding geocoding;
    private Weather weather;

    public NavigationFragment() {
    }

    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvPressure = view.findViewById(R.id.tvPressure);
        return view;
    }

    private void getLocation() {
        location = mListener.getLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
        showLocation(location);
        if (!checkConnection()) {
            Toast.makeText(getContext(), getResources().getString(R.string.connection), Toast.LENGTH_LONG).show();
        } else {
            waitGeocoding();
            waitWeather();
        }
    }

    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void showLocation(Location location) {
        tvLatitude.append(String.valueOf(location.getLatitude()));
        tvLongitude.append(String.valueOf(location.getLongitude()));
    }

    private void getGeocodingFromServer() {
        try {
            Response<Geocoding> geoResponse = GeocodingServer.getInstance().getApiCategory()
                    .getLocation(getResources().getString(R.string.geocodingKey), location.getLatitude(), location.getLongitude(), "json").execute();
            if (geoResponse.isSuccessful()) {
                geocoding = geoResponse.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitWeather() {
        new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                showWeather();
                saveData(new SavedData(location.getLatitude(), location.getLongitude(), geocoding.getDisplayName(), geocoding.getAddress().getCity(), Calendar.getInstance().getTime(), weather));
                ListFragment listFragment = ListFragment.getInstance();
                listFragment.updateData();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                getWeatherFromServer();
                return null;
            }
        }.execute();
    }

    private void saveData(SavedData savedData) {
        Preferences preferences = Preferences.getPreferences(getContext());
        preferences.saveFile(savedData);
    }


    private void getWeatherFromServer() {
        try {
            Response<Weather> weatherResponse = WeatherServer.getInstance().getWeatherApi()
                    .getWeather(location.getLatitude(), location.getLongitude(), getResources().getString(R.string.weatherKey), getResources().getString(R.string.units)).execute();
            if (weatherResponse.isSuccessful()) {
                weather = new Weather();
                weather.setMain(weatherResponse.body().getMain());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitGeocoding() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                getGeocodingFromServer();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                showAddress();
            }
        }.execute();
    }

    public void showAddress() {
        tvAddress.setText(geocoding.getDisplayName());
    }

    public void showWeather() {
        tvTemperature.append(weather.getMain().getTemp().toString());
        tvPressure.append(weather.getMain().getPressure().toString());
        tvHumidity.append(weather.getMain().getHumidity().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationFragmentInteractionListener) {
            mListener = (OnNavigationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
