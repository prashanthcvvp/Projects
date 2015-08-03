package com.backend;

import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by prashanth on 7/24/15.
 */
public class GetDataFromServer extends AsyncTask<String, Void, HashMap<String, String>> {
    private GlobalAppData globalAppData;


    public GetDataFromServer(GlobalAppData globalAppData) {
        this.globalAppData= globalAppData;
    }

    @Override
    protected HashMap<String, String> doInBackground(String[] params) {
        get_param_from_server(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> stringStringHashMap) {
        super.onPostExecute(stringStringHashMap);
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
            this.globalAppData.setJson_string(builder);
            this.globalAppData.setIf_get_place(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
