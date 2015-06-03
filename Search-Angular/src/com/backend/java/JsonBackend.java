package com.backend.java;

import java.util.Iterator;

import org.json.simple.JSONObject;

public class JsonBackend {

	public JSONObject concatJson(Object obj1, Object obj2){
		JSONObject json1 = (JSONObject)obj1;
		JSONObject json2 = (JSONObject)obj2;
		Iterator<String> json2_iterator = json2.keySet().iterator();
		while(json2_iterator.hasNext()){
			String key = json2_iterator.next();
			if(!key.equalsIgnoreCase("ifData")){
				String value = (String)json2.get(key);
				json1.put(key, value);
			}
		}
		return json1;
	}
}
