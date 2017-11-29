package com.nd.erpdemoapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ActivityNoInternet extends AppCompatActivity {
    Activity mActivity;
    Context mContext;

    AllMethods mAllMethods;

    RelativeLayout mRelativeLayoutRoot;
    ImageView mImageViewWifi;
    TextView mTextViewSetting;
    TextView mTextViewExit;

    boolean isLogin;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_no_internet_connection);

        try {
            mActivity = ActivityNoInternet.this;
            mContext = mActivity.getApplicationContext();

            mAllMethods = new AllMethods(mActivity);
            isLogin = mAllMethods.isLoggedIn();

            mRelativeLayoutRoot = (RelativeLayout) findViewById(R.id.root);
            mTextViewExit = (TextView) findViewById(R.id.txt_ext);
            mTextViewExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            mImageViewWifi = (ImageView) findViewById(R.id.img_wifi);
            mImageViewWifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPageRedirection();
                }
            });

            mTextViewSetting = (TextView) findViewById(R.id.txt_settings);
            mTextViewSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setPageRedirection();
    }

    private void setPageRedirection() {
        if (mAllMethods.check_Internet() == true) {
            if (isLogin) {
                /*
                Intent mIntent = new Intent(mActivity, ActivityDrawer.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
                */
            } else {
                /*
                Intent mIntent = new Intent(mActivity, ActivityMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
                */
            }
        } else {
            showSnackBar();
        }
    }

    private void showSnackBar() {
        try {
            Snackbar snackbar = Snackbar.make(mRelativeLayoutRoot, "Internet connection is not available.", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}