package com.nd.erpdemoapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

public class GPSTracker extends Service implements LocationListener {
	public static Activity mActivity;
	public static Context mContext;
	public static LocationManager locationManager = null;
	public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = true;

	Location location;
	double latitude;
	double longitude;

	protected static final int REQUEST_ACCESS_LOCATION = 102;

	public static AlarmManager alarmManager;
	public static PendingIntent pendingIntent;
	public static Integer MIN_TIME_BW_UPDATES = 1000*60*1;

	public GPSTracker(Context context){
		this.mContext = context;
	}

	public GPSTracker(Activity activity, Context context) {
		this.mActivity = activity;
		this.mContext = context;
	}

	public Location getLocation() {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
					&& ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
						"Allow permission for get user current location.",
						REQUEST_ACCESS_LOCATION);

				return null;
			}

			if (locationManager == null) {
				locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
			}

			try {
				isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!isGPSEnabled && !isNetworkEnabled) {
				this.canGetLocation = false;
			} else {
				if(isGPSEnabled){
					if (location == null) {
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					}

					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							this.canGetLocation = true;
						} else {
							this.canGetLocation = false;
							location = null;
						}
					} else {
						location = null;
					}
				}

				if(location == null){
					if (isNetworkEnabled) {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								this.canGetLocation = true;
							} else {
								this.canGetLocation = false;
								location = null;
							}
						} else {
							location = null;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	private void requestPermission(final String permission, String rationale, final int requestCode){
		if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)){
			ActivityCompat.requestPermissions(mActivity, new String[]{permission}, requestCode);
		} else {
			ActivityCompat.requestPermissions(mActivity, new String[]{permission}, requestCode);
		}
	}

	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}

	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void startBackgroundLocationUpdate() {
		alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(mContext, BackgroundLocationReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, MIN_TIME_BW_UPDATES, pendingIntent);
	}

	public void stopBackgroundLocationUpdate(){
		alarmManager.cancel(pendingIntent);
	}
}