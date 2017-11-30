package com.nd.erpdemoapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMain extends FragmentActivity implements OnMapReadyCallback {
    AllMethods mAllMethods;

    public static Activity mActivity;
    public static Context mContext;

    public GoogleMap mGoogleMap;
    public static GPSTracker locationManager;
    public static Location userLocation;
    public static double userLatitude, userLongitude;
    public static final int REQUEST_ACCESS_LOCATION = 102;

    private LocalBroadcastManager mLocalBroadcastMng;
    private BroadcastReceiver mLocationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mContext = mActivity.getApplicationContext();
        mAllMethods = new AllMethods(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Bundle b = intent.getBundleExtra("location");
                    Location location = (Location) b.getParcelable("location");

                    if (location != null) {
                        setMarkerOnMap(location);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocalBroadcastMng = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastMng.registerReceiver(mLocationReceiver, new IntentFilter("GPSLocationUpdates"));

        setPageRedirection();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unRegisterReceiver();
    }

    private void setPageRedirection() {
        try {
            if (locationManager == null) {
                locationManager = new GPSTracker(this, getApplicationContext());
            }

            checkLocationPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        if (!mAllMethods.check_Internet()) {
            mAllMethods.openNoConnectionPage(this, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            try {
                if (locationManager == null) {
                    locationManager = new GPSTracker(this, getApplicationContext());
                }

                checkLocationPermission();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            ActivityCompat.requestPermissions(mActivity, new String[]{permission}, requestCode);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void checkLocationPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        "Allow permission for location permission.",
                        REQUEST_ACCESS_LOCATION);
            } else {
                if (locationManager.canGetLocation()) {
                    userLocation = locationManager.getLocation();

                    if (userLocation != null) {
                        if (userLocation.getLatitude() != 0.0 && userLocation.getLongitude() != 0.0) {
                            userLatitude = userLocation.getLatitude();
                            userLongitude = userLocation.getLongitude();

                            if (mGoogleMap != null) {
                                setMarkerOnMap(userLocation);
                                locationManager.startBackgroundLocationUpdate();
                            }
                        } else {
                            mAllMethods.ShowLocationOffDialog(mActivity, mContext, mContext.getString(R.string.location_service), mContext.getString(R.string.location_off), mContext.getString(R.string.settings), mContext.getString(R.string.cancel));
                        }
                    } else {
                        mAllMethods.ShowLocationOffDialog(mActivity, mContext, mContext.getString(R.string.location_service), mContext.getString(R.string.location_off), mContext.getString(R.string.settings), mContext.getString(R.string.cancel));
                    }
                } else {
                    mAllMethods.ShowLocationOffDialog(mActivity, mContext, mContext.getString(R.string.location_service), mContext.getString(R.string.location_off), mContext.getString(R.string.settings), mContext.getString(R.string.cancel));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        checkLocationPermission();
    }

    private void setMarkerOnMap(Location location) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();

            LatLng mPlaceLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(mPlaceLatLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mPlaceLatLng, 15));
        }
    }

    private void unRegisterReceiver() {
        try {
            if (mLocalBroadcastMng != null && mLocationReceiver != null) {
                mLocalBroadcastMng.unregisterReceiver(mLocationReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
