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
import com.example.testfornyblesoft.api.LatLon;
import com.example.testfornyblesoft.pojo.Geocoding;
import com.example.testfornyblesoft.server.GeocodingServer;

import java.io.IOException;

import retrofit2.Response;


public class NavigationFragment extends Fragment {
    /*   private static final String ARG_PARAM1 = "param1";
       private static final String ARG_PARAM2 = "param2";

       private String mParam1;
       private String mParam2;
   */
    private OnNavigationFragmentInteractionListener mListener;
    private TextView tvLocation;
    private Location location;

    private Geocoding geocoding;

    public NavigationFragment() {
    }


    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();
     /*   Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
     */
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
    }

    private void getGeocodingFromServer() {
        try {
            String locationSB = location.getLatitude() + "," + location.getLongitude();
            LatLon latLon = new LatLon(location.getLatitude(), location.getLongitude());
            Response<Geocoding> geoResponse = GeocodingServer.getInstance().getApiCategory().getLocation(getResources().getString(R.string.geocodingKey), location.getLatitude(), location.getLongitude(), "json").execute();
            if (geoResponse.isSuccessful()) {
                geocoding = new Geocoding();
                geocoding.setDisplayName(geoResponse.body().getDisplayName());
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
