package com.backend;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by prashanth on 6/27/15.
 */
public class GetLatLong implements LocationListener {

    private static LatLng lat_long = null;
    private static GoogleMap googleMap;
    private MarkerOptions markerOptions;


    public GetLatLong(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.markerOptions=new MarkerOptions();

    }

    public static LatLng getLat_long() {
        return lat_long;
    }

    public static void setLat_long(LatLng lat_long) {
        GetLatLong.lat_long = lat_long;
    }

    @Override
    public void onLocationChanged(Location location) {
        lat_long = new LatLng(location.getLatitude(), location.getLongitude());
        this.googleMap.addMarker(this.markerOptions.position(lat_long).title("current position"));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
