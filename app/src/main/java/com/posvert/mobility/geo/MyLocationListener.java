package com.posvert.mobility.geo;

/**
 * Created by giovanni on 20/11/16.
 */

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocationListener implements LocationListener {
    private final Context context;
    private final GeoUtil geoutil;

    public MyLocationListener(Context context, GeoUtil geoUtil){
        this.context=context;
        this.geoutil=geoUtil;
    }

    @Override
    public void onLocationChanged(Location loc) {

/*
        Toast.makeText(
                context,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("W", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("W", latitude);*/

        geoutil.setLocation(loc);

        /*------- To get city name from coordinates -------- */


    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}