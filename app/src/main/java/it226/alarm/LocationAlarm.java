package it226.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;



public class LocationAlarm extends Activity {
    private LocationManager locationManager;
    private MyLocationListener myListener;
    private String provider;
    private Criteria criteria;
    private AlarmManager alarmManager;
    private TextView textView;
    private long defaultTimerLength;
    private PendingIntent alarmIntent;
    private Notification notification_popup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        defaultTimerLength = 120000;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            }
        } else {
            Toast.makeText(this, "" + Manifest.permission.ACCESS_FINE_LOCATION + " is already granted.", Toast.LENGTH_SHORT).show();
        }
        long pattern[] = {0,1000};
        notification_popup = new Notification.Builder(this)
                .setContentTitle("Get Up and Walk!")
                .setOngoing(true)
                .setVibrate(pattern)
                .build();
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            myListener.onLocationChanged(location);
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        locationManager.requestLocationUpdates(provider, 200, 1, myListener);
    }



    private class MyLocationListener extends AppCompatActivity implements AlarmManager.OnAlarmListener, LocationListener {

        private double latitudeValue, longitudeValue;

        @Override
        public void onLocationChanged(Location location) {
            latitudeValue = location.getLatitude();
            longitudeValue = location.getLongitude();
            resetAlarm();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(LocationAlarm.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(LocationAlarm.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(LocationAlarm.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }

        private void resetAlarm() {
            if (alarmManager.getNextAlarmClock() != null) {
                alarmManager.cancel(alarmIntent);
            }
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, defaultTimerLength, defaultTimerLength, alarmIntent);
        }

        private void cancelAlarm() {
            alarmManager.cancel(alarmIntent);
        }

        @Override
        public void onAlarm() {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            notificationManager.notify(0, notification_popup);
        }

    }
}

