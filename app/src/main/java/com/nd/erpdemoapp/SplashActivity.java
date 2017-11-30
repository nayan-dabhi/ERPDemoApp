package com.nd.erpdemoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    Activity mActivity;
    Context mContext;

    AllMethods mAllMethods;
    public int welcomeScreenDisplay = 2000;
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mActivity = this;
        mContext = mActivity.getApplicationContext();

        mAllMethods = new AllMethods(mActivity);
        isLoggedIn = mAllMethods.isLoggedIn();

        if(isLoggedIn){
            //mAllMethods.setLoggedInUserData();
        }

        pageRedirection();
    }

    private void pageRedirection() {
        Thread welcomeThread = new Thread() {
            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();

                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent mIntent;

                    if(isLoggedIn){
                        mIntent = new Intent(mActivity, ActivityMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    } else {
                        mIntent = new Intent(mActivity, ActivityLogin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }

                    startActivity(mIntent);
                    finish();
                }
            }
        };

        welcomeThread.start();
    }
}
