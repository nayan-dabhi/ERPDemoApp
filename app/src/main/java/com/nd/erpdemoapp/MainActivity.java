package com.nd.erpdemoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static Activity mActivity;

    AllMethods mAllMethods;
    public static GPSTracker locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mAllMethods = new AllMethods(this);

        if (mAllMethods.check_Internet()) {
            mAllMethods.showToastAtBottom("Internet is available.");
        } else {
            mAllMethods.openNoConnectionPage(this, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        try {
            locationManager = new GPSTracker(this, getApplicationContext());
            locationManager.startBackgroundLocationUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
