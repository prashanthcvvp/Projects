package com.backend;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by prashanth on 8/4/15.
 */
public class GetDocumentFromGoogleServer extends AsyncTask<LatLng,Void,Void> {
    private GMapV2GetRouteDirection gMapV2GetRouteDirection;
    private GoogleMap g_map;
    private Document vertex_document;
    public GetDocumentFromGoogleServer(GoogleMap g_map){
        this.gMapV2GetRouteDirection=new GMapV2GetRouteDirection();
        this.g_map=g_map;
    }
    @Override
    protected Void doInBackground(LatLng... params) {
        vertex_document=this.gMapV2GetRouteDirection.getDocument(params[0],params[1],GMapV2GetRouteDirection.MODE_DRIVING);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        g_map.clear();

        ArrayList<LatLng> directionPoint = this.gMapV2GetRouteDirection.getDirection(vertex_document);
        PolylineOptions route_line= new PolylineOptions().width(10).color(Color.BLUE);

        for (int i = 0; i < directionPoint.size(); i++) {
            route_line.add(directionPoint.get(i));
            g_map.addPolyline(route_line);
        }
        super.onPostExecute(aVoid);
    }
}
