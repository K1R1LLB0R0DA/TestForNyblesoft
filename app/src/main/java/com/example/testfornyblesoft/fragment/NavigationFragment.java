package com.example.testfornyblesoft.fragment;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.adapter.SavedData;
import com.example.testfornyblesoft.pojo.Geocoding;
import com.example.testfornyblesoft.pojo.Weather;
import com.example.testfornyblesoft.preference.Preferences;
import com.example.testfornyblesoft.server.GeocodingServer;
import com.example.testfornyblesoft.server.WeatherServer;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Response;


public class NavigationFragment extends Fragment {

    private OnNavigationFragmentInteractionListener mListener;
    private TextView tvLocation;
    private Location location;

    private Geocoding geocoding;
    private Weather weather;

    public NavigationFragment() {
    }
     /*   Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
     */

    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        tvLocation = view.findViewById(R.id.tvLocation);
        return view;
    }

    private void getLocation() {
        location = mListener.getLocation();
        showLocation(location);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    private void showLocation(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Latitude : ");
        stringBuilder.append(location.getLatitude());
        stringBuilder.append("\nLongitude : ");
        stringBuilder.append(location.getLongitude());
        stringBuilder.append("\n");
        tvLocation.setText(stringBuilder);
        waitGeocoding();
        waitWeather();
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
                loadData();
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

    private void loadData() {
        Preferences preferences = Preferences.getPreferences(getContext());
        List<SavedData> data = preferences.loadFile();
        tvLocation.append(data.size() + "");
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
        tvLocation.append(geocoding.getDisplayName() + "\n");
    }

    public void showWeather() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(weather.getMain().getTemp());
        stringBuilder.append(", ");
        stringBuilder.append(weather.getMain().getHumidity());
        stringBuilder.append(", ");
        stringBuilder.append(weather.getMain().getPressure());
        stringBuilder.append(".\n");
        tvLocation.append(stringBuilder);
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
