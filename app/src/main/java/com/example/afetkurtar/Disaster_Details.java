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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Disaster_Details extends AppCompatActivity implements OnMapReadyCallback {
    RequestQueue queue;
    JSONObject data;
    private GoogleMap mMap;
    private Geocoder geocoder;
    static int k = 0;
    ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster__details);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.disaster_detail_geri_button).setOnClickListener(this::onClick);
        // findViewById(R.id.Create_new_Subpart).setOnClickListener(this::onClick);

        queue = Volley.newRequestQueue(this);
        list2 = new ArrayList<JSONObject>();
        getData("");
        // System.out.println(data.toString());


        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.notification_map);
        mapFragment.getMapAsync((OnMapReadyCallback) Disaster_Details.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        ((LinearLayout) findViewById(R.id.auth_lay_SubpartScroll)).removeAllViews();
        list2 = new ArrayList<JSONObject>();
        getData("");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        LatLng latLng = null;
        try {
            latLng = new LatLng(Authorized_ActiveDisasters.latitude, Authorized_ActiveDisasters.longtitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Enkaz Noktasi").draggable(true));
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.disaster_detail_geri_button:
                //  Intent intent = new Intent(this, Authorized_Notification.class); //DEGISECEK
                //  startActivity(intent);
                finish();
                break;
            case R.id.afetolusturBTN:
                Intent intent = new Intent(this, Create_Disaster_Event_On_Map.class);
                intent.putExtra("json", data.toString());
                startActivity(intent);
                //  finish();
                break;
            case R.id.HistoryText:

                TextView linear = (TextView) v.findViewById(R.id.HistoryText);
                String tmp = (String) linear.getText();
                tmp = tmp.substring(tmp.indexOf(" ") + 1, tmp.indexOf("İsim")).trim();
                // tmp = tmp.substring(0,tmp.indexOf(" ")).trim(); // **************************************** MESAJI DEGISTIRIRSEN BURAYI AYARLA
                // System.out.println(tmp);

                Intent asd = new Intent(this, Disaster_Subpart.class);
                JSONObject json = new JSONObject();
                for (JSONObject x : list2) {
                    try {
                        if (x.getString("disasterID").equals(tmp.trim())) {
                            json = x;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                asd.putExtra("json", json.toString());
                startActivity(asd);


                break;
        }
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
                linear.setText("ID: " + x.getString("subpartID") + "\n" + "İsim : " + x.getString("subpartName"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            linear.setOnClickListener(this::onClick);
            k++;
            scroll.addView(view);
        }

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

    public void ClickCreateSubpart(View view) {
        Intent intent = new Intent(this, Create_Disaster_Event_On_Map.class);
        intent.putExtra("json", "");
        startActivity(intent);
    }

    public void ClickUpdateAfetBilgisi(View view) {
        redirectActivity(this,Disaster_Update.class);
    }
    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }
}