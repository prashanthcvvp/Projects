package com.backend;

import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by prashanth on 8/2/15.
 */
public class UpdatePlacesRunnable extends TimerTask{
    private Handler handler;
    GetDistance getNewPlaces;
    GlobalAppData globalAppData;
    public UpdatePlacesRunnable(GlobalAppData globalAppData){
        handler=new Handler();
        getNewPlaces=new GetDistance();
        this.globalAppData= globalAppData;
    }
    @Override
    public void run() {
        getNewPlaces.calculateDistance(globalAppData.getJson_string(), globalAppData.getLatitude(), globalAppData.getLongitude());
    }
}
