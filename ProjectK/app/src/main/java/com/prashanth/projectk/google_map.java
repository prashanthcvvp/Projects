package com.prashanth.projectk;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.backend.GetDataFromServer;
import com.backend.GetLatLong;
import com.backend.GlobalAppData;
import com.backend.UpdatePlacesRunnable;
import com.example.prashanth.projectk.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;

public class google_map extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GlobalAppData globalAppData;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetDataFromServer get_data_from_server = new GetDataFromServer(globalAppData);
        get_data_from_server.execute("http://projectk-search1.rhcloud.com/projectk-1.0/get_places");
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LocationManager loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        globalAppData= (GlobalAppData)getApplication();
        globalAppData.setContext(google_map.this);

        MarkerOptions marker_ops  = new MarkerOptions().position(new LatLng(0,0)).title("current location");
        marker_ops.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker curr_marker = mMap.addMarker(marker_ops);

        loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GetLatLong(curr_marker,globalAppData));
         timer= new Timer();
        Activity google_map_activity= google_map.this;
        timer.schedule(new UpdatePlacesRunnable(globalAppData,mMap,google_map_activity),5000,5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
    }
}
