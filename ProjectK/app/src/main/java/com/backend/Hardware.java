package com.backend;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by prashanth on 8/20/15.
 */
public class Hardware {

    public void gpsCheck(LocationManager locationManager,GlobalAppData globalAppData){
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            globalAppData.setGps(true);
        }else{
            globalAppData.setGps(false);
        }
    }

}
