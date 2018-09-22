package com.example.testfornyblesoft.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.adapter.FragmentAdapter;
import com.example.testfornyblesoft.fragment.OnListFragmentInteractionListener;
import com.example.testfornyblesoft.fragment.OnNavigationFragmentInteractionListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnNavigationFragmentInteractionListener, OnListFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, LocationListener {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //int for permissions request result
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private Location location;
    private GoogleApiClient googleApiClient;
    //lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsRejected = new ArrayList<>();

    private FragmentAdapter fragmentAdapter;
    private ViewPager vpMain;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initViewPager();
        textView = findViewById(R.id.tvMain);
        textView.setText("Current Location");

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        //build google api client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPlaServices()) {
            Toast.makeText(this, "You need to install GPS", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlaServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }
        return true;
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> permissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String perm : permissions) {
            if (!hasPermissions(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermissions(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    private void initViewPager() {
        vpMain = findViewById(R.id.vpMain);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), MainActivity.this);
        vpMain.setAdapter(fragmentAdapter);

    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public Location getLocation() {
        return location;
    }

    private List<Address> getAdress() throws IOException {
        Geocoder geocoder = new Geocoder(this, getResources().getConfiguration().locale);
        return geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    }

    private void showAddress() {
        try {
            Address addresses = getAdress().get(0);
            StringBuilder address = new StringBuilder();

            address.append(addresses.getFeatureName());
            address.append(", ");
            address.append(addresses.getCountryName());
            textView.append(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //show location
            showLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermissions((perm))) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("These permissions are mandatory to get your location. You need to allow them.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    } else {
                        if (googleApiClient != null) {
                            googleApiClient.connect();
                        }
                    }
                }
                break;
        }
    }

    private void showLocation(Location location) {
        textView.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
        textView.append("\n");
        showAddress();
    }
}
