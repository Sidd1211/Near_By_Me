package com.example.nearbyme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private  GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private  Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final  int request_user_location_code = 99;
    private double latitude, longitude;
    private int proximityRadius = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onClick(View view){
        String hospital = "hospital", school = "school", hotel = "hotels", restaurant = "restaurant", parkingZones = "parking_zones", internetCafe = "internet_cafe", busStop = "bus_stop";
        Object transferData[] = new  Object[2];
        String  url;
        GetNearByPlaces getNearByPlaces = new GetNearByPlaces();


        switch (view.getId()){
            case R.id.search_btn:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                String  address = addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address)){
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(address,6);

                        if (addressList != null){
                            for (int i = 0; i<addressList.size(); i++){
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                currentUserLocationMarker = mMap.addMarker(userMarkerOptions);
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                        else {
                            Toast.makeText(this, "Location not found....", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(this, "Enter a valid location !!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.hospital_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, hospital);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for hospitals nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for hospitals nearby", Toast.LENGTH_SHORT).show();
                break;

            case R.id.school_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, school);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for hospitals nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for hospitals nearby", Toast.LENGTH_SHORT).show();
                break;

            case R.id.hotel_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, hotel);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for hotels nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for hotels nearby", Toast.LENGTH_SHORT).show();
                break;

            case R.id.restaurants_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, restaurant);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for restaurants nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for restaurants nearby", Toast.LENGTH_SHORT).show();
                break;

            case R.id.parking_zones_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, parkingZones);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for parking zones nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for parking zones nearby", Toast.LENGTH_SHORT).show();
                break;

            case R.id.internet_cafe_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, internetCafe);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for internet cafe nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for internet cafe nearby", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bus_stop_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, busStop);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearByPlaces.execute(transferData);
                Toast.makeText(this, "Searching for bus stops nearby", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing results for bus stops nearby", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyplace) {
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=" + latitude + "," + longitude);
        googleUrl.append("&radius" + proximityRadius);
        googleUrl.append("&type=" + nearbyplace);
        googleUrl.append("&sensor=true");
        googleUrl.append("&key" +"AIzaSyBuCvnRMhguTh_DC0Y0jqUjHuqC5Q8SUaI");

        Log.d("GoogleMapsActivity", "url = " + googleUrl.toString());

        return googleUrl.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

    }

    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_user_location_code);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_user_location_code);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case request_user_location_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1100);
        mLocationRequest.setFastestInterval(1100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}