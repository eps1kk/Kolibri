package app.main.kolibri;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by epS on 01.07.2015.
 */
class NotifyLocationListener implements LocationListener {
    private static LocationManager locationManager;
    private static DataSingleton singleton;
    private static LocationListener locationListener;
    public static void setUpLocationListener(Context context) {
        singleton = DataSingleton.getInstance();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new NotifyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000*60, 100, locationListener);
        singleton.setLastKnownLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

    }
    public static void disableGPS() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000 * 60, 100, locationListener);
    }
    public static void enableGPS() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000*60, 100, locationListener);
    }
    @Override
    public void onLocationChanged(Location location) {
        singleton.setLastKnownLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000*60, 100, locationListener);
   }
}