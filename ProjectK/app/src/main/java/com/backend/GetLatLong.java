package com.backend;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by prashanth on 6/27/15.
 */
public class GetLatLong implements LocationListener {

    private static LatLng lat_long = null;
    private static Marker map_marker;
    private GlobalAppData globalAppData;


    public GetLatLong(Marker map_marker,GlobalAppData globalAppData) {
        this.map_marker=map_marker;
        this.globalAppData=globalAppData;


    }

    public static LatLng getLat_long() {
        return lat_long;
    }

    public static void setLat_long(LatLng lat_long) {
        GetLatLong.lat_long = lat_long;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.globalAppData.setLatitude(location.getLatitude());
        this.globalAppData.setLongitude(location.getLongitude());
        lat_long = new LatLng(location.getLatitude(), location.getLongitude());
        map_marker.setPosition(lat_long);

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
