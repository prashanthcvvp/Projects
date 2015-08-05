package com.backend;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prashanth.projectk.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by prashanth on 8/2/15.
 */
public class UpdatePlacesRunnable extends TimerTask implements GoogleMap.OnMarkerClickListener {
    private Handler handler;
    private GetDistance getNewPlaces;
    private GlobalAppData globalAppData;
    private GoogleMap g_map;
    private Activity activity;

    private LayoutInflater inflater = null;

    public UpdatePlacesRunnable(GlobalAppData globalAppData, GoogleMap g_map, Activity activity) {
        handler = new Handler();
        getNewPlaces = new GetDistance();
        this.globalAppData = globalAppData;
        this.g_map = g_map;
        this.activity = activity;
    }

    @Override
    public void run() {
        ArrayList<JSONObject> nearby_places = getNewPlaces.calculateDistance(globalAppData.getJson_string(), globalAppData.getLatitude(), globalAppData.getLongitude());
        for (int i = 0; i < nearby_places.size(); i++) {
            JSONObject json_obj = nearby_places.get(i);
            try {
                final String title = json_obj.getString("title");
                String[] lat_long = json_obj.getString("geo").split(" ");
                final String snippet = json_obj.getString("body");
                final LatLng lat_lng = new LatLng(Double.valueOf(lat_long[1]), -Double.valueOf(lat_long[0]));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        g_map.addMarker(new MarkerOptions().position(lat_lng).title(title).snippet(snippet));
                        g_map.setOnMarkerClickListener(UpdatePlacesRunnable.this);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                g_map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        LatLng[] latLngs = new LatLng[2];
                        latLngs[0]=new LatLng(globalAppData.getLatitude(),globalAppData.getLongitude());
                        latLngs[1]=marker.getPosition();
                        new GetDocumentFromGoogleServer(g_map).execute(latLngs);
                    }
                });
                g_map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(final Marker marker) {
                        if (inflater == null) {
                            inflater = (LayoutInflater) globalAppData.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        }

                        View v = inflater.inflate(R.layout.marker, null);
                        TextView title_view = (TextView) v.findViewById(R.id.title_view);
                        TextView snippet_view = (TextView) v.findViewById(R.id.snippet_view);
                        ImageButton navigate_btn = (ImageButton) v.findViewById(R.id.navigate_btn);

                        title_view.setText(marker.getTitle());
                        snippet_view.setText(marker.getSnippet());
                        final LatLng marker_pos = marker.getPosition();
                        navigate_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                               // Toast.makeText(activity.getApplicationContext(), "lat - " + marker_pos.latitude + " Longi - " + marker_pos.longitude, Toast.LENGTH_LONG).show();
                            }
                        });

                        return v;
                    }
                });
            }
        });
        return false;
    }
}
