package com.backend;

import android.app.Application;
import android.content.Context;

/**
 * Created by prashanth on 8/2/15.
 */
public class GlobalAppData extends Application {
    private double longitude=0;
    private double latitude =0;
    private StringBuilder json_string;
    private boolean if_get_place=false;
    private Context context;
    private boolean gps=false;

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }




    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public boolean isIf_get_place() {
        return if_get_place;
    }

    public void setIf_get_place(boolean if_get_place) {
        this.if_get_place = if_get_place;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        json_string=new StringBuilder();
    }

    public StringBuilder getJson_string() {
        return json_string;
    }

    public void setJson_string(StringBuilder json_string) {
        this.json_string = json_string;
    }



    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
