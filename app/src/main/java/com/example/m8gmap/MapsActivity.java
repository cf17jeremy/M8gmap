package com.example.m8gmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.m8gmap.modelFlickr.ApiCallFlickr;
import com.example.m8gmap.modelFlickr.ModelApiFlickr;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.m8gmap.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.m8gmap.model.ApiCall;
import com.example.m8gmap.model.ModelApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        enableMyLocation();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d("Soy el chivato ", latLng.toString());
                getAddress(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(getAddress(latLng.latitude, latLng.longitude))).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                ApiThread process = new ApiThread(latLng.latitude, latLng.longitude);
                process.execute();
                String lat = Double.toString(latLng.latitude);
                String lng = Double.toString(latLng.longitude);

                //Aqui declarem el retrofit de sunrise i l'utilitzem
                ApiCall sunriseapi = getApiCallSun("https://api.sunrise-sunset.org/");
                Call<ModelApi> callsun = sunriseapi.getData(lat, lng);
                callsun.enqueue(new Callback<ModelApi>(){
                    @Override
                    public void onResponse(Call<ModelApi> call, Response<ModelApi> response) {
                        if(response.code()!=200){
                            Log.i("testApi", "checkConnection");
                            return;
                        }

                        Log.i("testApi", response.body().getStatus() + " - " + response.body().getResults().getSunrise());
                        Log.i("testApi", response.body().getStatus() + " - " + response.body().getResults().getSunset());
                    }

                    @Override
                    public void onFailure(Call<ModelApi> call, Throwable t) {

                    }
                });

                //Aqui declarem el retrofit de flickr i l'utilizem
                String api_key = "79d466885188b99d6762980d64029892";
                ApiCallFlickr flickrapi = getApiCallFlickr("https://www.flickr.com/");
                Call<ModelApiFlickr> callflickr = flickrapi.getData("flickr.photos.search",api_key, lat, lng, "5","json");
                callflickr.enqueue(new Callback<ModelApiFlickr>() {
                    @Override
                    public void onResponse(Call<ModelApiFlickr> call, Response<ModelApiFlickr> response) {
                        Log.i("testApiFlickr", response.toString());
                        if (response.code() != 200) {
                            Log.i("testApiFlickr", response.toString());
                            return;
                        }
                        for (int i=0; i<5; i++) {
                            ArrayList<String> urls = CreateUrlImg(response.body().getPhotos().getPhoto().get(i).getServer(), response.body().getPhotos().getPhoto().get(i).getId(), response.body().getPhotos().getPhoto().get(i).getSecret());
                            Log.i("testApiFlickr", response.body().getStat() + " - " + urls.get(i));
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelApiFlickr> call, Throwable t) {
                        Log.i("testApiFlickr", call.toString() + t.toString());
                    }
                });
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }


    private ArrayList<String> CreateUrlImg(String serverid, String id, String secret){
        String base = "https://live.staticflickr.com/";
        String composedurl = base + serverid + "/" + id + "_" + secret + ".jpg";
        ArrayList<String> urls = new ArrayList<>();
        urls.add(composedurl);
        return urls;
    }

    private ApiCall getApiCallSun(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiCall.class);
    }

    private ApiCallFlickr getApiCallFlickr(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiCallFlickr.class);
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    public String getAddress (double lat, double lng) {
        try {
            //Geocoder es una api que convierte las direcciones en coordenadas y viceversa (convierte las coordenadas en direcciones)
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            //Aqui le damos al geocoder las coordenadas y recogemos la direccion en una lista
            List<Address> addresses = geo.getFromLocation(lat, lng, 1);
            if (addresses.isEmpty()) {
                Toast.makeText(this, "No s’ha trobat informació", Toast.LENGTH_LONG).show();
            } else {
                if (addresses.size() > 0) {
                    String msg =addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    return msg;
                }
            }
        }
        catch(Exception e){
            Toast.makeText(this, "No Location Name Found", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }
}