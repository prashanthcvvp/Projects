package com.backend;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by prashanth on 7/24/15.
 */
public class GetDataFromServer extends AsyncTask<String, Void, HashMap<String, String>> {
    private GoogleMap g_map;
    private HashMap<String, String> places_map;

    public GetDataFromServer(GoogleMap googleMap) {
        this.g_map = googleMap;
        places_map = new HashMap<String, String>();
    }

    @Override
    protected HashMap<String, String> doInBackground(String[] params) {
        get_param_from_server(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> stringStringHashMap) {
        super.onPostExecute(stringStringHashMap);
        if(places_map.size()>0){
            Iterator<String> places_name_iterator = places_map.keySet().iterator();
            while(places_name_iterator.hasNext()){
                String name = places_name_iterator.next();
                String values = places_map.get(name).toString();
                if(values.length()>0) {
                    String[] lat_long = values.split(" ");
                    if(lat_long.length>1) {

                        double lat =Double.parseDouble(lat_long[1]);
                        double longi = -Double.parseDouble(lat_long[0]);
                        LatLng latLng = new LatLng(lat,longi);
                        Log.d("p5", "Lat " + latLng.latitude + " Longi " + latLng.longitude);
                        this.g_map.addMarker(new MarkerOptions().position(latLng).title(name));
                    }
                }
            }
        }
    }

    public void get_param_from_server(String url_str) {

        try {
            URL url = new URL(url_str);
            HttpURLConnection http_connection = (HttpURLConnection) url.openConnection();
            http_connection.setDoInput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(http_connection.getInputStream()));
            String json = "";
            StringBuilder builder = new StringBuilder();
            while ((json = reader.readLine()) != null) {
                builder.append(json);
            }
            JSONArray jarray = new JSONArray(builder.toString());
            Log.d("p5", "Hello");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject j_obj = (JSONObject) jarray.get(i);
                Log.d("p5", j_obj.get("title").toString());
                places_map.put(j_obj.get("title").toString(), j_obj.get("geo").toString());
            }
        } catch (MalformedURLException e) {
            Log.d("p5", "MalFormed");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("p5", "JSON Exception");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("p5", "IO Exception");
            e.printStackTrace();
        }

    }
}
