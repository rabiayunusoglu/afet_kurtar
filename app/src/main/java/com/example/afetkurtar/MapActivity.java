package com.example.afetkurtar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    RequestQueue queue;
    //variables
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Marker mMarker;

    //markers
    private ArrayList<Marker>markers;

    //widgets
    private ImageView mInfo;

    //arrays from database data
    ArrayList<String> markerInfoArray;
    ArrayList<String> markerTitleArray;
    ArrayList<String> markerLatitudeArray;
    ArrayList<String> markerLongitudeArray;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mInfo = (ImageView) findViewById(R.id.place_info);

        //init markers array on create
        markers = new ArrayList<Marker>();

        getLocationPermission();
        geocoder = new Geocoder(MapActivity.this);
        /*
        queue = Volley.newRequestQueue(this);
        getMarkerData(""); // burasi degisecek
        */
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
                //System.out.println("permissions true !???!");
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
                            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Burası "+ location).draggable(true));
                            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
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
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Another marker")
                        .snippet("coordinates(" + latLng.latitude + ", " + latLng.longitude + ") \n" + "someInformation\n").draggable(true));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });


        Button btnGeri = (Button) findViewById(R.id.btn_geri);
        btnGeri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Intent intent = new Intent(MapActivity.this, Afet_Bolgesi.class);
                //startActivity(intent);
                finish();
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick : clicked place info");
                try{
                    if(mMarker.isInfoWindowShown()){
                        Log.d(TAG, "onClick: place info: Some Info");
                        mMarker.hideInfoWindow();
                    }else{
                        mMarker.showInfoWindow();
                    }

                }catch(NullPointerException e){
                    Log.d(TAG, "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });

    }//onMapReady

    public void getMarkerData(String ID){
        /*
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID",ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/users/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            getDisasterData(response);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }
    public void initMarkers(){

        String markerInfo ="";
        String markerTitle="";
        double markerLatitude=0;
        double markerLongitude=0;
        //markerInfo, markerTitle, markerLatitude, markerLongitude --> ourDatabase must have this info on table about disasterEvents

        int ourArraySize = markerInfoArray.size();//!!! this size can be markerInfoArray.size() or markerTitleArray.size() or markerLatitudeArray.size() etc.

        double focusLatLngLatitude = 0; //helper for animateCamera
        double focusLatLngLongitude = 0; //helper for animateCamera

        for(int i = 0 ; i < ourArraySize ; i++){
            markerInfo = markerInfoArray.get(i);
            markerTitle = markerTitleArray.get(i);
            markerLatitude = Double.parseDouble(markerLatitudeArray.get(i)); //if String array then convert it to double value
            markerLongitude = Double.parseDouble(markerLongitudeArray.get(i)); //if String array then convert it to double value

            focusLatLngLatitude += markerLatitude;
            focusLatLngLongitude += markerLongitude;


            LatLng latLng = new LatLng(markerLatitude, markerLongitude);
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(markerTitle).snippet(markerInfo));
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
            markers.add(mMarker);

            // burada orta noktaya yakin nokta bulunup camera oraya focus olsa mantklı

        }

        //burasi pinlenen noktaların ortasına dogru kameranin pozisyonunun degismesini saglayacak
        focusLatLngLatitude = focusLatLngLatitude/ourArraySize;
        focusLatLngLongitude = focusLatLngLongitude/ourArraySize;
        LatLng focusLatLng = new LatLng(focusLatLngLatitude,focusLatLngLongitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(focusLatLng));

    }

    public void getDisasterData(JSONObject obj) {
        String cevap = "";
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> listXCoordinates = new ArrayList<String>();
        ArrayList<String> listYCoordinates = new ArrayList<String>();
        ArrayList<String> listInfoDisaster = new ArrayList<String>();
        ArrayList<String> listTitleDisaster = new ArrayList<String>();

        try {
            //    System.out.println(response.toString());
            cevap = obj.getString("records");
            cevap = cevap.substring(1,cevap.length()-1);

            while(cevap.indexOf(",{") >-1){
                list.add(cevap.substring(0,cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{")+1);
            }
            list.add(cevap);
            for(int x = list.size()-1; x>=0;x--){
                try {
                    JSONObject tmp = new JSONObject(list.get(x));

                    listXCoordinates.add(tmp.getString("userID"));// Degisecek
                    listYCoordinates.add(tmp.getString("userType"));// Degisecek
                    /*
                    listXCoordinates.add(tmp.getString("xCoordinates"));
                    listYCoordinates.add(tmp.getString("yCoordinates"));
                    listInfoDisaster.add(tmp.getString("Info"));
                    listTitleDisaster.add(tmp.getString("Title"));
                     */

                }catch (Exception e){
                    // e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //for init ourArray with using db data
        markerInfoArray = listInfoDisaster;
        markerTitleArray = listTitleDisaster;
        markerLatitudeArray = listXCoordinates;
        markerLongitudeArray = listYCoordinates;

        /*
        for(int i = 0 ; i<listXCoordinates.size() ; i++){
            System.out.println("userID --> " + listXCoordinates.get(i));
        }

        for(int i = 0 ; i<listYCoordinates.size() ; i++){
            System.out.println("userType --> " + listYCoordinates.get(i));
        }
        */
    }//coordinatesMethod


}
