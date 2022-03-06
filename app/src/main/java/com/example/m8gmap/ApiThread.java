package com.example.m8gmap;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiThread extends AsyncTask<LatLng,Void,String> {

    @Override
    protected String doInBackground(LatLng... latLngs) {
        try {
            URL url = new URL("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data = bufferedReader.readLine();
            return data;
        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String... data) throws JSONException {
        JSONObject jObject = new JSONObject(data);
        jObject = jObject.getJSONObject("results");
        String sunrise = jObject.getString("sunrise");
        Log.i("logtest", "------>" + sunrise);
    }
}
