package com.nd.erpdemoapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

import java.util.List;

public class BackgroundLocationReceiver extends BroadcastReceiver {
    public static GPSTracker locationManager;
    public static Location userLocation;
    public static double userLatitude, userLongitude;

    AllMethods mAllMethods;
    public static AlertDialog LocationAlertDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            boolean isGetLocation = false;
            mAllMethods = new AllMethods(MainActivity.mActivity);
            locationManager = new GPSTracker(context);

            if(locationManager.canGetLocation()){
                userLocation = locationManager.getLocation();

                if(userLocation != null){
                    if(userLocation.getLatitude()!=0.0 && userLocation.getLongitude()!=0.0){
                        userLatitude = userLocation.getLatitude();
                        userLongitude = userLocation.getLongitude();

                        isGetLocation = true;
                    }
                }
            }

            if (isGetLocation) {
                Log.e("Latlng : ", userLatitude + ", " + userLongitude);

                if(mAllMethods.check_Internet() == true){
                    // new userLocationUpdate().execute();
                }
            } else {
                showLocationErrorDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLocationErrorDialog() {
        ShowDialog(MainActivity.mActivity, "Location Service", "Turn on location services to locate your position.", "Setting", "Cancel");
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;

        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isInBackground;
    }

    public void ShowDialog(final Activity mActivity, String Title , String Message, String Ok, String cancel) {
        try {
            if(LocationAlertDialog == null || (LocationAlertDialog != null && !LocationAlertDialog.isShowing())){
                if(mActivity != null){
                    if(!isAppIsInBackground(mActivity)){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mActivity, R.style.AlertDialogTheme));

                        alertDialogBuilder.setTitle(Title);
                        alertDialogBuilder.setMessage(Message)
                                .setCancelable(true)
                                .setPositiveButton(Ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        mActivity.startActivity(intent);
                                    }
                                })

                                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        LocationAlertDialog = alertDialogBuilder.create();
                        LocationAlertDialog.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public class userLocationUpdate extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String latitude = String.valueOf(userLatitude);
            String longitude = String.valueOf(userLongitude);

            mGetMessage = (GetMessage) mPostParseGet.updateUserLocation(mGetMessage, userId, latitude, longitude, SolutionEnterprises.mStringFCMID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (mPostParseGet.isNetError) {
                    // Log.e("isNetError", "true");
                } else if (mPostParseGet.isOtherError) {
                    // Log.e("isOtherError", "data loading failed.");
                } else {
                    if (mGetMessage != null) {
                        if (mGetMessage.isStatus() == true) {
                            prefsEditor.putString("userLatitude", String.valueOf(userLatitude));
                            prefsEditor.putString("userLongitude", String.valueOf(userLongitude));
                            prefsEditor.putString("userLastLocation", mGetMessage.getLast_location());
                            prefsEditor.commit();

                            Log.e("Location", "Updated.");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    */
}