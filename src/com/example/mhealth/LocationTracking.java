package com.example.mhealth;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.example.mhealth.helper.GlobalVars;

import java.util.Calendar;

public class LocationTracking extends Service {

    public boolean foundGPSLocation = false;
    AlarmManager alarmManager;
    String strUserName, strPassword, strFooter, strEnterUserPass, strPleaseWait, strAuthOnline,
            strEnableInternet, strNoValidUser, strGPSEnable, strGPSSetting, strConnectingAGPS, strYes,
            strNo, strAGPS, strType, strGPS, strLat, strLang;
    double latitude; // latitude
    double longitude; // longitude
    LocationManager locationManager;
    long minTime = 1000;
    float minDistance = 1;
    String tag = "";
    MyLocationListener mylistener;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mylistener = new MyLocationListener();

        try {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            startGettingLocationUsingGPSProvider();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startGettingLocationUsingGPSProvider() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime, minDistance, mylistener);
        CountDownTimer count = new CountDownTimer(5000, 1) {
            @Override
            public void onFinish() {
//				Toast.makeText(getApplicationContext(), strConnectingAGPS, 200).show();
                Notify("a", "aa", "agps");
                tag = strAGPS;
                startGettingLocationUsingNetworkProvider();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }

        };
        count.start();

    }

    public void startGettingLocationUsingNetworkProvider() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mylistener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Intent servIntent = new Intent(this, LocationTracking.class);
        PendingIntent pintent = PendingIntent
                .getService(this, 0, servIntent, 0);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), 6 * 1000, pintent);
        try {
            startGettingLocationUsingGPSProvider();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void Notify(String notificationTitle, String notificationMessage,
                       String path) {
        /*Uri soundUri = Uri.parse("android.resource://com.example.anganwarisupport/raw/"+ path);*/
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher) // notification icon
                .setContentTitle("Geo-Location Updates") // title for notification
                .setContentText("Geo-Location Updates") // message for notification
                .setAutoCancel(true);
        /*.setAutoCancel(true).setSound(soundUri);*/
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    class MyLocationListener implements LocationListener {
        String provider_tag = "";

        @Override
        public void onLocationChanged(Location location) {
            String sss = location.getLongitude() + "";
            if (sss.length() > 10) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            Toast.makeText(LocationTracking.this, "latitude" + latitude + " and " + "longitude" + longitude,
                    Toast.LENGTH_LONG).show();
            GlobalVars.lattitude = location.getLatitude() + "";
            GlobalVars.longitude = location.getLongitude() + "";

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            if (provider.equalsIgnoreCase("gps")) {
                Notify("a", "aa", "gps");
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 10, 1, mylistener);
                tag = strAGPS;
            }
        }
    }
}
