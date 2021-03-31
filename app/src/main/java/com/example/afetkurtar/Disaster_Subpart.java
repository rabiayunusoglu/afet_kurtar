package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.List;
import java.util.Locale;

public class Disaster_Subpart extends AppCompatActivity implements OnMapReadyCallback {
    EditText diname, subname, adrs, misper, resper, isvol, level, durum;
    RequestQueue queue;
    JSONObject data;
    static String enkazNoktasi;
    private GoogleMap mMap;
    private Geocoder geocoder;
    static int k = 0;
    ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
    static ArrayList<JSONObject> subpartList = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster__subpart);
        diname = findViewById(R.id.subpartdisname);
        subname = findViewById(R.id.subpartname);
        adrs = findViewById(R.id.subpartaddres);
        misper = findViewById(R.id.subpartmisper);
        resper = findViewById(R.id.subpartresper);
        isvol = findViewById(R.id.subpartisvolstate);
        level = findViewById(R.id.subpartlevel);
        durum = findViewById(R.id.subpartstatus);
        System.out.println(Disaster_Details.disname + "**************************************************");
        diname.setText(Disaster_Details.disname);
        diname.setFocusable(false);
        subname.setText(Disaster_Details.subpartName);
     //   subname.setFocusable(false);
        adrs.setText(Disaster_Details.subpartAdres);
       // adrs.setFocusable(false);
        misper.setText(Disaster_Details.missingPerson);
       // misper.setFocusable(false);
        resper.setText(Disaster_Details.rescuedPerson);
     //   resper.setFocusable(false);
        level.setText(Disaster_Details.level);
       // level.setFocusable(false);
        durum.setText(Disaster_Details.status);
       // durum.setFocusable(false);
        System.out.println(Disaster_Details.isOpenForVolunteer + "--------------------------------------------");
        if (Disaster_Details.isOpenForVolunteer.equals("1"))
            isvol.setText("Açık");
        else
            isvol.setText("Kapalı");
        //isvol.setFocusable(false);
        queue = Volley.newRequestQueue(this);
        list2 = new ArrayList<JSONObject>();
        getData("");
        // System.out.println(data.toString());


        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.subpart_map);
        mapFragment.getMapAsync((OnMapReadyCallback) Disaster_Subpart.this);

    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }

    public void ClickDisasterActiveGeri(View view) {
        finish();
    }

    public void ClickDisasterCreate(View view) {
        redirectActivity(this, DisasterCreate.class);
    }

    public void addtext(JSONObject obj) {
        String cevap = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            //    System.out.println(response.toString());
            cevap = obj.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);
            for (int x = list.size() - 1; x >= 0; x--) {
                try {
                    JSONObject tmp = new JSONObject(list.get(x));

                    list2.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());


        for (JSONObject x : list2) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_personel_history_addlayout, null);
            LinearLayout scroll = findViewById(R.id.auth_lay_SubpartScroll);
            TextView linear = (TextView) view.findViewById(R.id.HistoryText); // personel_history ile ayni addlayout'u kullaniyor
            linear.setTextSize(20);


            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(x.getString("latitude")), Double.parseDouble(x.getString("longitude")), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String addres = "Koordinatlardan Adres Bilgisi Alınamadı";
                try {
                    addres = addresses.get(0).getAddressLine(0);
                } catch (Exception e) {
                }
                //  linear.setText("ID: " + x.getString("subpartID") + "\n" + "İsim : " + x.getString("subpartName"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   linear.setOnClickListener(this::onClick);
            k++;
            // scroll.addView(view);
        }

        //this method creates subparts on map
        subpartList = list2;
        System.out.println(subpartList.toString() + "*************************************************");
        initSubpartsOnMap(subpartList);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        LatLng latLng = null;
        try {
            latLng = new LatLng(Double.parseDouble(Authorized_ActiveDisasters.disasterlatitude), Double.parseDouble(Authorized_ActiveDisasters.disasterlongitude));
            // Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Enkaz Noktasi").snippet(enkazNoktasi).draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClickDisasterInfo() {

    }

    public void ReturnBack() {
        finish();
    }

    public void getData(String ID) {
        JSONObject obj = new JSONObject();
        System.out.println("Authorized_ActiveDisasters.disasterID:**************************" + Authorized_ActiveDisasters.disasterID);
        try {
            obj.put("disasterID", Authorized_ActiveDisasters.disasterID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/subpart/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            addtext(response);
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

    public void initSubpartsOnMap(ArrayList<JSONObject> myList) {
        createSubpartMarkerOnMap(myList);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Assign_Team_Subpart:
                Intent intent = new Intent(this, Create_Subpart_On_Map.class);
                intent.putExtra("json", data.toString());
                startActivity(intent);
                //  finish();
                break;
        }
    }

    public void delete() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("subpartID", Disaster_Details.subpartID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/subpart/delete.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            ReturnBack();
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


    public ArrayList<Marker> createSubpartMarkerOnMap(ArrayList<JSONObject> jsonList) {
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

        // System.out.println(jsonList.toString());
        for (JSONObject object : jsonList) {

            try {
                if (object.getString("subpartID").equals(Disaster_Details.subpartID + "")) {
                    System.out.println("(object.getString(\"subpartID\").equals(Disaster_Details.subpartID))" + (object.getString("subpartID").equals(Disaster_Details.subpartID)));

                    System.out.println(Disaster_Details.subpartID);
                    System.out.println(object.getString("subpartID"));
                    System.out.println("object in createSubpartMarkerOnMap: " + object.toString());
                    subpartID = object.getString("subpartID").toString();
                    tmpdisasterID = object.getString("disasterID").toString();
                    latitude = Double.parseDouble(object.getString("latitude").toString());
                    longitude = Double.parseDouble(object.getString("longitude").toString());
                    address = object.getString("address").toString();
                    System.out.println("AMINEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                    missingPerson = Integer.parseInt(object.getString("missingPerson"));
                    rescuedPerson = Integer.parseInt(object.getString("rescuedPerson"));
                    stringIsOpenForVolunteers = object.getString("isOpenForVolunteers").toString();
                    subpartName = object.getString("subpartName").toString();
                    tmpdisasterName = object.getString("disasterName").toString();
                    disasterStatus = object.getString("status").toString();
                    disasterEmergencyLevel = Integer.parseInt(object.getString("emergencyLevel"));
                    enkazNoktasi = "Asıl Afet ID: " + tmpdisasterID + "\n" + "Afet Parçası ID : " + subpartID + "\n" +
                            "Afet Parçası İsmi : " + subpartName + "\n" +
                            "Adress : " + address + "\n" +
                            "Enlem : " + latitude + "\n" +
                            "Boylam : " + longitude + "\n" +
                            "Kayıp İnsan Sayısı : " + missingPerson + "\n" +
                            "Kurtarılan İnsan Sayısı : " + rescuedPerson + "\n" +
                            "Afet Durum Bilgisi : " + disasterStatus + "\n" +
                            "Afet Durum Düzeyi : " + disasterEmergencyLevel + "\n" +
                            "Gönüllülere Açık Mı : " + stringIsOpenForVolunteers + "\n";
                    System.out.println("Test2 : createSubpartMarkerOnMap");
                    LatLng latLng = new LatLng(latitude, longitude);
                    System.out.println("naber");
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
                    System.out.println("naber2");
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Disaster_Subpart.this));
                    markerList.add(subMarker);
                    System.out.println("naber333");

                }
            } catch (Exception e) {
                e.getMessage();
            }

        }
        try {
                /*
                double avarageLatitude = (latitudeStart + latitudeEnd)/2;
                double avarageLongitude = (longitudeStart + longitudeEnd)/2;
                */

            LatLng forZoomLatlng = new LatLng(Double.parseDouble(Disaster_Details.subpartlatitude), Double.parseDouble(Disaster_Details.subpartlongitude));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(forZoomLatlng, 5));
        } catch (Exception e) {
            e.getMessage();
        }
        System.out.println("naberson");
        return markerList;

    }

    public void clickSubpartguncelle(View view) {
        JSONObject obj = new JSONObject();
        try {
//Get current date time
            obj.put("disasterID", Authorized_ActiveDisasters.disasterID);
            obj.put("subpartID", Disaster_Details.subpartID);
            obj.put("emergencyLevel", Integer.parseInt(level.getText().toString()));
            obj.put("latitude", Disaster_Details.subpartlatitude);
            obj.put("longitude", Disaster_Details.subpartlongitude);
            obj.put("status", durum.getText().toString());
            obj.put("address",adrs.getText().toString());
            obj.put("disasterName", diname.getText().toString());
            obj.put("subpartName", subname.getText().toString());
            obj.put("missingPerson", misper.getText().toString());
            obj.put("rescuedPerson", resper.getText().toString());
            if (isvol.getText().toString().trim().toLowerCase().equals("açık") || isvol.getText().toString().trim().toLowerCase().equals("açik") )
                obj.put("isOpenForVolunteers", true);
            else if (isvol.getText().toString().trim().toLowerCase().equals("kapalı")||isvol.getText().toString().trim().toLowerCase().equals("kapali") )
                obj.put("isOpenForVolunteers", false);
            else
                throw new Exception("");
            Toast.makeText(this, "Afet alt parçası güncellendi.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Gönüllü İstek Talep Edebilme Durumunu Evet yada HAyır şeklinde belirleyiniz", Toast.LENGTH_SHORT).show();

        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/subpart/update.php", obj, new Response.Listener<JSONObject>() {
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
        Toast.makeText(Disaster_Subpart.this, "Afet Güncellendi.", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void clickSubpartsil(View view) {
        delete();
    }
}