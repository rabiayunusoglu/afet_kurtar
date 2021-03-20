package com.example.afetkurtar;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
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

public class Create_Disaster_Event_On_Map extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Create_Disaster_Event_On_Map";
    RequestQueue queue;
    JSONObject data;
    private GoogleMap mMap;
    private Geocoder geocoder;

    //variables for info
    private String disasterID;
    private String address;
    private double latitude;
    private double longitude;
    private int missingPerson;
    private int rescuedPerson;
    private String isOpenForVolunteers; //dikkat burasi sonra boolean olarak degistir

    private TextView tvCoordinatesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_disaster_event);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn_createDisaster).setOnClickListener(this::onClick);

        //init textView for Coordinates
        tvCoordinatesText = (TextView) findViewById(R.id.coordinateText);
        /*
         Onceden belirtilen kordinatları almak icin asagidaki islemi ekledim
         */
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("json");
        try {
            data = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
         NOT : data.getstring("latitude") veya "longitude" ile bu sayfaya gönderilmis olan
         veriden kordinatini alıyosun.
         */

        //create Map
        initMap();
    }// onCreate End



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("test : map is ready");
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {

                //for calculate coordinates and show on screen
                calculateCoordinates(arg0);
            }
        });


    }//onMapReady end

    public void calculateCoordinates(LatLng myLatlng){
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

        tvCoordinatesText.setText("(" + tmpLatitude + ", " + tmpLongitude + ")");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createDisaster:
                getEditTextData();
                break;
        }
    }
    public void getEditTextData(){
        try {
            mMap.clear(); //reset map


            EditText editAfetId = (EditText)findViewById(R.id.afet_id);
            EditText editAfetAddress = (EditText)findViewById(R.id.afet_address);
            EditText editAfetLatitude = (EditText)findViewById(R.id.afet_latitude);
            EditText editAfetLongitude = (EditText)findViewById(R.id.afet_longitude);
            EditText editAfetMissingPerson = (EditText)findViewById(R.id.afet_missing_person);
            EditText editAfetRescuedPerson = (EditText)findViewById(R.id.afet_rescued_person);
            EditText editIsOpenForVolunteers = (EditText)findViewById(R.id.afet_isOpenForVolunteers);

            /*
            System.out.println("editAfetId : " + editAfetId.getText().toString());
            System.out.println("editAfetAddress : " + editAfetAddress.getText().toString());
            System.out.println("editAfetLatitude : " + editAfetLatitude.getText().toString());
            System.out.println("editAfetLongitude : " + editAfetLongitude.getText().toString());
            System.out.println("editAfetMissingPerson : " + editAfetMissingPerson.getText().toString());
            System.out.println("editAfetRescuedPerson : " + editAfetRescuedPerson.getText().toString());
            System.out.println("editIsOpenForVolunteers : " + editIsOpenForVolunteers.getText().toString());
             */

            //Dikkat!!! burada bos input kontrolu olursa diye kontrol ekle sonra
            disasterID = editAfetId.getText().toString();
            address = editAfetAddress.getText().toString();
            latitude = Double.parseDouble(editAfetLatitude.getText().toString());
            longitude = Double.parseDouble(editAfetLongitude.getText().toString());
            missingPerson = Integer.parseInt(editAfetMissingPerson.getText().toString());
            rescuedPerson = Integer.parseInt(editAfetRescuedPerson.getText().toString());
            isOpenForVolunteers = editIsOpenForVolunteers.getText().toString();

            LatLng latLng = null;
            try {
                latLng = new LatLng(latitude,longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(disasterID).snippet("Adress : " + address + "\n" +
                        "latitude : " + latitude + "\n" +
                        "longitude : " + longitude + "\n" +
                        "missingPerson : " + missingPerson + "\n" +
                        "rescuedPerson : " + rescuedPerson + "\n" +
                                "isOpenForVolunteers : " + isOpenForVolunteers + "\n")
                        );
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Create_Disaster_Event_On_Map.this));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));


                // Dikkat !!! burada database e girilen verileri gönderme methodu eklenicek burada kaldın
                // sendDataToDB(); methodu yazılacak


            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    public void initMap(){
        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.create_disaster_event_map);
        mapFragment.getMapAsync(Create_Disaster_Event_On_Map.this) ;
    }
}//class end
