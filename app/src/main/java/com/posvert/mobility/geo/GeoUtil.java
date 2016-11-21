package com.posvert.mobility.geo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.posvert.mobility.R;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandlerPOST;
import com.posvert.mobility.common.URLBuilder;
import com.posvert.mobility.common.URLHelper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import beans.Commento;
import beans.GeoLocation;

/**
 * Created by giovanni on 20/11/16.
 */

public class GeoUtil {
    private final Context ctx;
    private LocationManager locationManager;

    public static boolean isGpsEnabled(Context ctx) {
        ContentResolver contentResolver = ctx
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);


        return gpsStatus;
    }

    public GeoUtil(Context ctx) {
        this.ctx = ctx;
    }

    public void init() {
        locationManager = (LocationManager)
                ctx.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener(ctx, this);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 60000, 10, locationListener);

        Location location = getLastBestLocation();



        setLocation(location);
    }

    private Location getLastBestLocation() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    public void getCity(Location loc) {
        String cityName = null;
        Geocoder gcd = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("WW", "My Current City is: "
                + cityName);
    }

    public void setLocation(final Location loc) {
        getCity(loc);
        double longitude = loc.getLongitude();
        double latitide = loc.getLatitude();

        URLHelper.invokeURLPOST(ctx, buildUrl(), new ResponseHandlerPOST() {
            @Override
            public void parseResponse(String response) {

            }

            @Override
            public String getJSONMessage() {
                StringWriter sw = new StringWriter();
                GeoLocation u = getValori(loc);
                Gson gson = new Gson();
                gson.toJson(u, sw);
                String val = sw.toString();
                return val;
            }
        });
    }

    private String buildUrl() {
        String url = URLHelper.buildWithPref(ctx, "geo", "setGeoLocation", false);
        return url;
    }

    private GeoLocation getValori(Location loc) {
        GeoLocation u = new GeoLocation();
        u.setLatitute(loc.getLatitude());
        u.setLongitute(loc.getLongitude());

        u.setUsername(Heap.getUserCorrente().getUsername());
        return u;
    }

}
