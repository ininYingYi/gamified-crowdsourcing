package nthu.nmsl.crowdsourcinggame.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by InIn on 2015/10/31.
 */
public class LocationSensor {
    private static final String TAG = "LocationSensor";
    private static LocationSensor objectSelf = null;
    private LocationManager manager;
    private Context context;
    private long minTime = 300000;
    private float minDistance = 20;
    private int hasPermission;
    private Listener gpsListener, netListener;
    private Location combineLocation = new Location(LocationManager.GPS_PROVIDER);

    public static LocationSensor getInstance(Context context) {
        if (objectSelf == null) {
            objectSelf = new LocationSensor(context, 300000, 20);
        }
        return objectSelf;
    }
    public static LocationSensor getInstance() {
        if (objectSelf == null) {
            Log.e(TAG, "LocationSensor did not init.");
        }
        return objectSelf;
    }

    public LocationSensor(Context context, long minTime, float minDistance) {
        this.context = context;
        this.minTime = minTime;
        this.minDistance = minDistance;
        manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        Criteria oGPSSettings = new Criteria();
        oGPSSettings.setAccuracy(Criteria.ACCURACY_FINE);
        oGPSSettings.setSpeedRequired(true);
        oGPSSettings.setAltitudeRequired(true);
        oGPSSettings.setBearingRequired(true);
        oGPSSettings.setCostAllowed(false);
        oGPSSettings.setPowerRequirement(Criteria.POWER_MEDIUM);
        String bestProvider = manager.getBestProvider(oGPSSettings, true);
        hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        //if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            if (manager.isProviderEnabled(bestProvider)) {
                Log.d(TAG, "bestProvider is enabled");
                gpsListener = new Listener(true, bestProvider);
                manager.requestLocationUpdates(bestProvider, this.minTime, this.minDistance, gpsListener);
            }
            /*if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d(TAG, "GPS is enabled");
                gpsListener = new Listener(true, LocationManager.GPS_PROVIDER);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, this.minTime, this.minDistance, gpsListener);
            }*/
            if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d(TAG, "Network is enabled");
                netListener = new Listener(true, LocationManager.NETWORK_PROVIDER);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, this.minTime, this.minDistance, netListener);
            }
        /*}
        else {
            Log.d(TAG,"GPS dynmic permiision denied");
        }*/
    }

    public Location getCurrentLocation() {
        Location gpsLocation = gpsListener.getCurrentLocation();
        Location netLocation = netListener.getCurrentLocation();
        if (gpsLocation != null && gpsListener.isEnable) {
            combineLocation = gpsLocation;
            return gpsLocation;
        }
        else if (netLocation != null && netListener.isEnable) {
            combineLocation = netLocation;
            return netLocation;
        }
        else {
            return combineLocation;
        }
    }

    private class Listener implements LocationListener {
        protected boolean isEnable = false;
        protected String provider;
        private Location currentLocation;

        public Listener(boolean enable, String provider) {
            this.isEnable = enable;
            this.provider = provider;
        }

        synchronized protected Location getCurrentLocation() {
            if(currentLocation == null) {
                /*hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {*/
                    currentLocation = manager.getLastKnownLocation(this.provider);
                /*}
                else {
                    Log.d(TAG,"GPS dynmic permiision denied");
                }*/
            }
            return currentLocation;
        }
        @Override
        synchronized public void onLocationChanged(Location location) {
            if (isEnable) {
                currentLocation = location;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if(provider.equals(this.provider)){
                isEnable = false;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            if(provider.equals(this.provider)) {
                hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    manager.requestLocationUpdates(this.provider, minTime, minDistance, this);
                    isEnable = true;
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}
