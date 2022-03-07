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

public class ApiThread extends AsyncTask<Void,Void,String> {
    private double latitude;
    private double longitude;

    public ApiThread(double latitude, double longitude) {
        latitude = this.latitude;
        longitude = this.longitude;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.sunrise-sunset.org/json?lat="+ latitude +"&lng=" + longitude);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data = bufferedReader.readLine();
            return data;
        } catch (MalformedURLException e) {
             e.toString();
        } catch (IOException e) {
             e.toString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String data) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(String.valueOf(data));
            jObject = jObject.getJSONObject("results");
            String sunrise = jObject.getString("sunrise");
            Log.i("logtest", "------>" + sunrise);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
