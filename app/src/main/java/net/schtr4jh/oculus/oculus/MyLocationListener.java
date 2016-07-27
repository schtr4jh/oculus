package net.schtr4jh.oculus.oculus;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by schtr4jh on 26.7.2016.
 */
public class MyLocationListener implements LocationListener {

    private MainActivity mainActivity = null;

    public MyLocationListener(MainActivity activity) {
        mainActivity = activity;
    }

    public void onLocationChanged(Location loc) {
        EditText locationText = (EditText) mainActivity.findViewById(R.id.locationText);
        if (loc == null) {
            locationText.setText("No location ...");
            return;
        }
        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
        locationText.setText("Location: " + longitude + " " + latitude);
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onProviderEnabled(String s) {

    }

    public void onProviderDisabled(String s) {

    }

}
