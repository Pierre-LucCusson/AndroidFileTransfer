package com.androidfiletransfer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class Tracker {
    private static long minTime = 1000;
    private static float minDistance = 1;

    private Activity activity;
    private LocationManager locationManager;

    public Tracker(Activity activity) {
        if (activity != null) {
            this.activity = activity;
            this.locationManager = (LocationManager) this.activity.getSystemService(LOCATION_SERVICE);
        }
    }

    public boolean startTracking(LocationListener locationListener) {
        boolean active = false;

        if (this.activity != null) {
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
                active = true;
            }
        }

        return active;
    }

    public Location getLastLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }

                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }

        }

        return bestLocation;
    }
}
