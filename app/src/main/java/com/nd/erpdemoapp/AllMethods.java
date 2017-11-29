package com.nd.erpdemoapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class AllMethods {
    public Activity mActivity;
    Context mContext;

    ConnectivityManager mConnectivityManager;
    NetworkInfo mNetworkInfo;
    boolean isNetError;

    public AllMethods() {
    }

    public AllMethods(Context mContext) {
        this.mContext = mContext;
    }

    public AllMethods(Activity mContext) {
        this.mActivity = mContext;
    }

    public AllMethods(FragmentActivity mContext) {
        this.mActivity = mContext;
    }

    public static Pattern getEmailPattern() {
        return Pattern.compile("^[_A-Za-z0-9/.]+([_A-Za-z0-9-/+/-/?/*/=///^/!/#/$/%/'/`/{/}/|/~/;]+)*@[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*(\\.[A-Za-z]{2,})$");
    }

    public static Pattern getMobileNoPattern() {
        return Pattern.compile("^[0-9]{10}");
    }

    public String formatPhoneNumber(String number) {
        number = number.substring(0, number.length() - 4) + "-" + number.substring(number.length() - 4, number.length());
        number = number.substring(0, number.length() - 8) + ")" + number.substring(number.length() - 8, number.length());
        number = number.substring(0, number.length() - 12) + "(" + number.substring(number.length() - 12, number.length());
        number = number.substring(0, number.length() - 14) + "+" + number.substring(number.length() - 14, number.length());

        return number;
    }

    public void showToastAtBottom(String strMsg) {
        Toast toast = Toast.makeText(mActivity.getApplicationContext(), strMsg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 30);
        toast.show();
    }


    public Boolean isLoggedIn() {
        SharedPreferences mSharedPreferences = mActivity.getApplicationContext().getSharedPreferences("temp", 0);
        return mSharedPreferences.getBoolean("isLogin", false);
    }

    public void setLoggedInUserData() {
        SharedPreferences mSharedPreferences = mActivity.getApplicationContext().getSharedPreferences("temp", 0);
        // SolutionEnterprises.userId = mSharedPreferences.getString("userId", "");
    }

    public String getDeviceUUID() {
        return Secure.getString(mActivity.getContentResolver(), Secure.ANDROID_ID);
    }

    public boolean isSDCARDPResent() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return isSDPresent;
    }

    public static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public boolean isTabletDevice(FragmentActivity activityContext) {
        boolean large = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

        if (large) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        } else if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }

        return false;
    }

    public void hideKeyboard(Activity mActivity2, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity2.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public void ShowDialog(final Activity activity, String Title, String Message, String Ok) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(true)
                .setPositiveButton(Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void ShowDialog(FragmentActivity activity, String Title, String Message, String Ok) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message)
                .setCancelable(true)
                .setPositiveButton(Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ShowNoConnectionDialog(Activity activity, Context mContext, String Title, String Message, String Ok, String Cancle) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

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

                .setNegativeButton(Cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ShowLocationOffDialog(Activity activity, Context mContext, String Title, String Message, String Ok, String Cancle) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message)
                .setCancelable(true)
                .setPositiveButton(Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mActivity.startActivity(intent);
                    }
                })

                .setNegativeButton(Cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean check_Internet() {
        boolean status = false;

        mConnectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting()) {
            status = true;
        }

        return status;
    }

    public static String CustomTimeFormatter(String dateString, String dateFormat) {
        String strDateFormat = "";
        SimpleDateFormat smDateFormat = new SimpleDateFormat(dateFormat);


        try {
            Date mDateObj = smDateFormat.parse(dateString);
            strDateFormat = smDateFormat.format(mDateObj);
        } catch (Exception e) {
            System.out.println("Date Format Exception : " + e.toString());
        }
        return strDateFormat;
    }

    public static String CustomDateFormatter(String dateString, String dateFormat) {
        String strDateFormat = "";
        SimpleDateFormat smDateFormat = new SimpleDateFormat(dateFormat);

        try {
            Date mDateObj = smDateFormat.parse(dateString);
            strDateFormat = smDateFormat.format(mDateObj);
        } catch (Exception e) {
            System.out.println("Date Format Exception : " + e.toString());
        }
        return strDateFormat;
    }

    public static Date CustomDate(String dateString, String dateFormat) {
        Date mDateObj = null;
        SimpleDateFormat smDateFormat = new SimpleDateFormat(dateFormat);

        try {
            mDateObj = smDateFormat.parse(dateString);
        } catch (Exception e) {
            System.out.println("Date Exception : " + e.toString());
        }
        return mDateObj;
    }

    public static String CustomDateFormatter(Date dateObj, String dateFormat) {
        String strDateFormat = "";
        SimpleDateFormat smDateFormat = new SimpleDateFormat(dateFormat);

        try {
            strDateFormat = smDateFormat.format(dateObj);
        } catch (Exception e) {
            System.out.println("Date Format Exception : " + e.toString());
        }

        return strDateFormat;
    }

    public void openNoConnectionPage(Activity activity, Integer mIntentTag) {
        Intent intent = new Intent(activity, ActivityNoInternet.class).setFlags(mIntentTag);
        activity.startActivity(intent);
    }

}
