package com.example.afetkurtar;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class Show_Subparts_Of_Disaster extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Create_Disaster_Event_On_Map";
    RequestQueue queue;
    JSONObject data = new JSONObject();
    private GoogleMap mMap;
    private Geocoder geocoder;

    private String disasterID = "";
    private double latitudeStart = 0;
    private double longitudeStart = 0;
    private double latitudeEnd = 0;
    private double longitudeEnd = 0;

    Spinner spinner;
    ArrayAdapter adapter;

    ArrayList<String> afetStringList = new ArrayList<String>();
    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_subparts_of_disaster);
        queue = Volley.newRequestQueue(this);

        spinner = (Spinner) findViewById(R.id.mainDisasterSpinnerForSubPart);
        setAfetToSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (((Spinner) findViewById(R.id.mainDisasterSpinnerForSubPart)).getSelectedItem().toString().equalsIgnoreCase("Afet Seçin")) {
                    //textAfetId.setText("seçilmedi");
                    resetMap();
                } else {


                    for (JSONObject x : jsonObjectList) {
                        try {
                            if (x.getString("disasterName").equals(((Spinner) findViewById(R.id.mainDisasterSpinnerForSubPart)).getSelectedItem().toString())) {
                                disasterID = x.getString("disasterID");
                                latitudeStart = Double.parseDouble(x.getString("latitudeStart"));
                                latitudeEnd = Double.parseDouble(x.getString("latitudeEnd"));
                                longitudeStart = Double.parseDouble(x.getString("longitudeStart"));
                                longitudeEnd = Double.parseDouble(x.getString("longitudeEnd"));

                                resetMap();
                                getSubpartsFromDB();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                    //textAfetId.setText(disasterID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initMap();
    }// onCreate End

    public void resetMap(){
        try{
            mMap.clear();
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void setAfetToSpinner() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/disasterEvents/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForDisasterEventTable(response);
                        } catch (Exception e) {
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

    public void handleResponseForDisasterEventTable(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            //    System.out.println(response.toString());
            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);

            afetStringList.add("Afet Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    afetStringList.add(tmp.getString("disasterName"));
                    jsonObjectList.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, afetStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSubpartsFromDB() {
        readFromSubpartTable();
    }

    public void handleResponseForSubpartTable(JSONObject obj) {

        System.out.println(obj.toString());

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
        try {
            //    System.out.println(response.toString());
            String cevap = obj.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);
            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    jsonList.add(tmp);

                    // tmp.getString("disasterId"); böyle yap
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            // burada add marker methodu olacak yada liste ekelemeden
                try {
                    createSubpartMarkerOnMap(jsonList);
                }catch (Exception e){
                    e.getMessage();
                }


        }catch (Exception e){
            e.getMessage();
        }

    }

        public ArrayList<Marker> createSubpartMarkerOnMap(ArrayList<JSONObject> jsonList){
        System.out.println("Test1 : createSubpartMarkerOnMap");
        ArrayList<Marker> markerList = new ArrayList<Marker>();

         String subpartID;
         String tmpdisasterID;
         double latitude;
         double longitude;
         String address;
         int missingPerson;
         int rescuedPerson;
         String stringIsOpenForVolunteers;
         String subpartName;
         String tmpdisasterName;
         String disasterStatus;
         int disasterEmergencyLevel;


            for(JSONObject object: jsonList){
                try{
                    System.out.println("object in createSubpartMarkerOnMap: " + object.toString());
                    subpartID = object.getString("subpartID").toString();
                    tmpdisasterID = object.getString("disasterID").toString();
                    latitude = Double.parseDouble(object.getString("latitude").toString());
                    longitude = Double.parseDouble(object.getString("longitude").toString());
                    address = object.getString("address").toString();
                    missingPerson = Integer.parseInt(object.getString("missingPerson"));
                    rescuedPerson = Integer.parseInt(object.getString("rescuedPerson"));
                    stringIsOpenForVolunteers = object.getString("isOpenForVolunteers").toString();
                    subpartName = object.getString("subpartName").toString();
                    tmpdisasterName = object.getString("disasterName").toString();
                    disasterStatus = object.getString("status").toString();
                    disasterEmergencyLevel = Integer.parseInt(object.getString("emergencyLevel"));

                    System.out.println("Test2 : createSubpartMarkerOnMap");
                    LatLng latLng = new LatLng(latitude, longitude);
                    Marker subMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Asıl Afet ID: " + tmpdisasterID).snippet("Afet Parçası ID : " + subpartID + "\n" +
                            "Afet Parçası İsmi : " + subpartName + "\n" +
                            "Adress : " + address + "\n" +
                            "Enlem : " + latitude + "\n" +
                            "Boylam : " + longitude + "\n" +
                            "Kayıp İnsan Sayısı : " + missingPerson + "\n" +
                            "Kurtarılan İnsan Sayısı : " + rescuedPerson + "\n" +
                            "Afet Durum Bilgisi : " + disasterStatus + "\n" +
                            "Afet Durum Düzeyi : " + disasterEmergencyLevel + "\n" +
                            "Gönüllülere Açık Mı : " + stringIsOpenForVolunteers + "\n")
                    );
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Show_Subparts_Of_Disaster.this));
                    markerList.add(subMarker);



                }catch (Exception e){
                    e.getMessage();
                }
            }
            try{
                double avarageLatitude = (latitudeStart + latitudeEnd)/2;
                double avarageLongitude = (longitudeStart + longitudeEnd)/2;
                LatLng forZoomLatlng = new LatLng(avarageLatitude,avarageLongitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(forZoomLatlng,5));
            }catch (Exception e){
                e.getMessage();
            }

            return markerList;
        }
        public void readFromSubpartTable () {
        JSONObject tempJObj = new JSONObject();
            try {
                if(!disasterID.equals("Afet Seçin") && !disasterID.equals("")){
                    tempJObj.put("disasterID",disasterID);
                }else{
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                    (Request.Method.POST, "https://afetkurtar.site/api/subpart/search.php", tempJObj, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //  System.out.println(response.toString());
                                handleResponseForSubpartTable(response);
                            } catch (Exception e) {
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
        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;
            System.out.println("test : map is ready");
            Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        }//onMapReady end

        public void initMap(){
            geocoder = new Geocoder(this);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.show_subparts_of_disaster_map);
            mapFragment.getMapAsync(Show_Subparts_Of_Disaster.this) ;
        }
    }



