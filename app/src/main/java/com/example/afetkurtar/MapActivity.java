package com.example.afetkurtar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //variables
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
        geocoder = new Geocoder(MapActivity.this);
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializeing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this) ;

    }

    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                System.out.println("permissions true !???!");
               initMap(); // not sure !!!
            }else{
                ActivityCompat.requestPermissions(this, permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // burada kaldim Log.d
        mLocationPermissionsGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0 ; i< grantResults.length; i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady: map is ready here");
        mMap = googleMap;

        Button btngit = (Button) findViewById(R.id.btn_git);
        Log.d(TAG,"test0: test0");
        btngit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                EditText editlokasyon = (EditText)findViewById(R.id.git_lokasyon);
                String location = editlokasyon.getText().toString();
                if(location != null && !location.equals("")){

                    List<Address> addressList = null;
                    //geocoder = new Geocoder(MapActivity.this);

                    try{
                        addressList=geocoder.getFromLocationName(location,1);

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    try{
                        if(addressList.size() > 0){
                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Burası "+ location).draggable(true));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }else{
                            Log.d(TAG,"onMapReady: can not find this area!");
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                mMap.clear();
                LatLng latLng = arg0;
                System.out.println("map clicked");
                System.out.println("longitude : " + latLng.longitude);
                System.out.println("latitude : " + latLng.latitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Another marker").draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

    }//onMapReady




}
