package com.example.afetkurtar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Disaster_Update extends AppCompatActivity implements OnMapReadyCallback {

    RequestQueue queue;
    JSONObject data = new JSONObject();
    private GoogleMap mMap;
    private Geocoder geocoder;

    DrawerLayout drawerLayout;
    Marker clickMarker;

    EditText afetName, latitude, longtitude,type,levell,base;
    static Double latitudeMap,logtitudeMap;
    Button gonder,geri;
    static String afetTipi, afetussu;
    static String level;
    static int indexType, indexbase, indexlevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster__update);
        queue = Volley.newRequestQueue(this);
        type = findViewById(R.id.disastertypeBtnUpdate);
        levell = findViewById(R.id.disasterlevelBtnUpdate);
        base = findViewById(R.id.disasterussuBtnUpdate);
        latitude = findViewById(R.id.latitudeBtnUpdate);
        longtitude = findViewById(R.id.longtitudeBtnUpdate);
        afetName = findViewById(R.id.disasterNameBtnUpdate);
        type.setText(Authorized_ActiveDisasters.disasterType);
        levell.setText(Authorized_ActiveDisasters.emergencyLevel);
        base.setText(Authorized_ActiveDisasters.disasterBase);
        latitude.setText(Authorized_ActiveDisasters.disasterlatitude);
        longtitude.setText(Authorized_ActiveDisasters.disasterlongitude);
        afetName.setText(Authorized_ActiveDisasters.disasterName);
        gonder = findViewById(R.id.afetupdateBTN);
        geri=findViewById(R.id.disaster_update_geri_button);
        try {

            Bundle bundle = getIntent().getExtras();

            if(!bundle.isEmpty()){
                try {
                    String message = bundle.getString("json");
                    data = new JSONObject(message);
                }catch (Exception e){
                    e.getMessage();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        initMap();
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gonder.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
              /*  JSONObject obj = new JSONObject();

              Authorized_ActiveDisasters.disasterID
                try {
//Get current date time
                    System.out.println("*********************************************************************");
                    System.out.println(afetTipi+"***********"+level+"**********"+afetussu);
                    System.out.println("*********************************************************************");
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatDateTime = now.format(formatter);
                    obj.put("disasterType", afetName.getText().toString());
                    obj.put("emergencyLevel", Integer.parseInt(levell.getText().toString()));
                    obj.put("latitude", latitudeMap);
                    obj.put("longitude", logtitudeMap);
                    obj.put("disasterDate", formatDateTime);
                    obj.put("disasterBase", base.getText().toString());
                    obj.put("disasterName", afetName.getText().toString());
                } catch (Exception e) {

                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/disasterEvents/create.php", obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
                queue.add(request);
                Toast.makeText(Disaster_Update.this, "Afet Oluşturuldu.", Toast.LENGTH_LONG).show();
                finish();*/
            }
        });
    }
    public void initMap(){
        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.disaster_update_map);
        mapFragment.getMapAsync(Disaster_Update.this) ;
    }
    public void calculateCoordinates(LatLng myLatlng){
        try{
            if(!clickMarker.equals(null)){
                clickMarker.remove();
            }
        }catch (Exception e){
            e.getMessage();
        }

        double tmpLatitude = myLatlng.latitude;
        double tmpLongitude = myLatlng.longitude;
        String tmpLatitudeString = String.valueOf(myLatlng.latitude);
        String tmpLongitudeString = String.valueOf(myLatlng.longitude);

        try{
            if(tmpLatitudeString.indexOf(".") != -1 &&tmpLatitudeString.substring(tmpLatitudeString.indexOf(".")).length() >4){
                tmpLatitude = Double.parseDouble(tmpLatitudeString.substring(0,tmpLatitudeString.indexOf(".")+4));
            }
            if(tmpLongitudeString.indexOf(".") != -1 && tmpLongitudeString.substring(tmpLongitudeString.indexOf(".")).length() >4){
                tmpLongitude = Double.parseDouble(tmpLongitudeString.substring(0,tmpLongitudeString.indexOf(".")+4));
            }
        }catch(Exception e){
            e.getMessage();
        }
        ((EditText)(findViewById(R.id.latitudeBtnUpdate))).setText(tmpLatitudeString);
        ((EditText)(findViewById(R.id.longtitudeBtnUpdate))).setText(tmpLongitudeString);

        latitudeMap=tmpLatitude;
        logtitudeMap=tmpLongitude;

        LatLng latLng1 = new LatLng(myLatlng.latitude,myLatlng.longitude);
        clickMarker = mMap.addMarker(new MarkerOptions().position(latLng1));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Disaster_Update.this));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,8));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("test : map is ready");
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        LatLng latLng1=null;
        try {
            latLng1 = new LatLng(Double.parseDouble(data.getString("latitude")), Double.parseDouble(data.getString("longitude")));
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng1).title("Enkaz Noktasi").draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {

                //for calculate coordinates and show on screen
                calculateCoordinates(arg0);
            }
        });

        // if data is not empty get dataLatitude and dataLongitude

        if(!data.equals(new JSONObject())){

            try {
                String dataLatitude = data.getString("latitude");
                String dataLongitude = data.getString("longitude");

                double doubleDataLatitude = Double.parseDouble(dataLatitude);
                double doubleDataLongitude = Double.parseDouble(dataLongitude);

                LatLng latLng = new LatLng(doubleDataLatitude, doubleDataLongitude);

                ((EditText)(findViewById(R.id.afet_latitude))).setText(dataLatitude);
                ((EditText)(findViewById(R.id.afet_longitude))).setText(dataLongitude);

                Marker dataMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Disaster_Update.this));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}