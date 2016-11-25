package com.posvert.mobility;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONArray;

import beans.GeoLocation;
import beans.JSONHandler;
import beans.MessaggioOffline;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
  /*      LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        URLHelper.invokeURL(this, buildUrl(), new ResponseHandler() {
                    @Override
                    public void parseResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            mMap.clear();
                            for (int i = 0; i < array.length(); i++) {

                                GeoLocation loc = JSONHandler.parseGeoLocationJSON(array.getJSONObject(i));
                                LatLng sydney = new LatLng(loc.getLatitute(), loc.getLongitute());
                                String user = loc.getUsername();
                                mMap.addMarker(new MarkerOptions().position(sydney).title(user));
                                if (user.equals(Heap.
                                        getUserCorrente().getUsername()))
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }


    private String buildUrl() {
        String url = URLHelper.buildWithPref(this, "geo", "getAllLocations", false);
        return url;
    }
}
