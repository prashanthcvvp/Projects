package com.backend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetDistance {
	
	public ArrayList<JSONObject> calculateDistance(StringBuilder string_format,double lat1,double longi1){
		JSONArray jarray;
		ArrayList<JSONObject> nearby_places = new ArrayList<JSONObject>();
		try {
			jarray = new JSONArray(string_format.toString());
			for(int i=0;i<jarray.length();i++){
				JSONObject jobj = (JSONObject)jarray.get(i);
				String geo = jobj.get("geo").toString();
				String[] lat_long = geo.split(" ");
				String title = jobj.get("title").toString();
				if(lat_long.length>1){
					double lat2 = Double.parseDouble(lat_long[1]);
					double longi2 = -Double.parseDouble(lat_long[0]);
					double d=haversine(lat1, longi1, lat2, longi2);
					if(d<50) {
						nearby_places.add(jobj);
					}
				}
			}
			return nearby_places;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static final double R = 6372.8; // In kilometers
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

}
