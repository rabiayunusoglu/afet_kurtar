package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Authorized_VolunteerRequest2 extends AppCompatActivity implements OnMapReadyCallback{
    ArrayList<String> arrayListSubpart = new ArrayList<>();
    ArrayList<Integer> arrayListSubpartID = new ArrayList<Integer>();
    ArrayAdapter<String> adapterSubpart;
    ArrayList<Double> arrayListLat = new ArrayList<>();
    ArrayList<Double> arrayListLog = new ArrayList<>();
    ArrayAdapter<String> adapterlat;
    ArrayAdapter<String> adapterlog;
    Button submits;
    static double lat=0.0,longt=0.0;
    public static JSONObject volInfo;

    String url1 = "https://afetkurtar.site/api/volunteerUser/update.php";
    DrawerLayout drawerLayout;
    RequestQueue queuev,queue,queuecheck;
    static EditText afet;
    Spinner subpartSpinner;
    static boolean controlresponce=false;
    int index = 0;
    static int dataSupartID=0;
    String urlSubpart = "https://afetkurtar.site/api/subpart/search.php";
    static String responceStringSubpart = "", dataSupartName = "";
    GoogleSignInClient mGoogleSignInClient;
    private GoogleMap mMap;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queuev = Volley.newRequestQueue(this);
        queue = Volley.newRequestQueue(this);
        queuecheck = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_authorized__volunteer_request2);
        drawerLayout = findViewById(R.id.drawer_layout2autreg);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        subpartSpinner = (Spinner) findViewById(R.id.spinnersubpartnew);
        afet =(EditText) findViewById(R.id.editAfet);
        afet.setText(String.valueOf(Authorized_VolunteerRequest.afetBolgesi));
        afet.setFocusableInTouchMode(false);
        arrayListSubpart.add("Seçilmedi");
        arrayListSubpartID.add(0);
        loadSpinnerDataSubpart(urlSubpart);

        submits = findViewById(R.id.gdrBtn);
        submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlresponce=false) {
                    Toast.makeText(getApplicationContext(), "Seçili alt parçası gönüllüler için açık değildir! ", Toast.LENGTH_SHORT).show();
                    finish();
                }else  if(dataSupartName.equals("Seçilmedi")){
                    Toast.makeText(getApplicationContext(), "Bir alt parça seçiniz!", Toast.LENGTH_SHORT).show();
                }
                else
                    redirectActivity(Authorized_VolunteerRequest2.this,Authorized_VolunteerRequest3.class);
            }
        });


        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.aut_subpart_Detail_map);
        mapFragment.getMapAsync((OnMapReadyCallback) Authorized_VolunteerRequest2.this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }



    private void loadSpinnerDataSubpart(String url) {
        JSONObject obj = new JSONObject();
        try {
            System.out.println("**********************************************************************************");
            System.out.println(Authorized_VolunteerRequest.afetID);
            System.out.println("**********************************************************************************");
            obj.put("isOpenForVolunteers", 1);
            obj.put("disasterName", Authorized_VolunteerRequest.afetBolgesi);
            System.out.println(Authorized_VolunteerRequest.afetID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                obj, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                       controlresponce=true;
                        System.out.println(response.toString().length());
                        responceStringSubpart = response.toString();
                        responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("[") + 1, responceStringSubpart.length() - 2);

                        while (responceStringSubpart.indexOf("}") > 1) {
                            if (responceStringSubpart.contains("{")) {

                                String subpartlat = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartlog = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpart = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartID = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                if (responceStringSubpart.contains("},"))
                                    responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("}") + 2);
                                else {
                                    responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("}") + 1);
                                }
                                String value = "\"subpartName\":\"";
                                String valueID="\"subpartID\":\"";
                                String valuelat = "\"latitude\":\"";
                                String valuelog = "\"longitude\":\"";
                                String flag = "\"";
                                String result = "";
                                Double resultlat =0.0;
                                Double resultlog = 0.0;
                                int resultID=0;
                                subpart = subpart.substring(subpart.indexOf(value) + value.length());
                                result = subpart.substring(0, subpart.indexOf(flag));
                                subpart = subpart.substring(subpart.indexOf("},") + 1);
                                arrayListSubpart.add(result);
                                subpartlat = subpartlat.substring(subpartlat.indexOf(valuelat) + valuelat.length());
                                resultlat = Double.parseDouble(subpartlat.substring(0, subpartlat.indexOf(flag)));
                                // subpart = subpart.substring(subpart.indexOf("},") + 1);
                                System.out.println(resultlat);
                                arrayListLat.add(resultlat);
                                subpartlog = subpartlog.substring(subpartlog.indexOf(valuelog) + valuelog.length());
                                resultlog = Double.parseDouble(subpartlog.substring(0, subpartlog.indexOf(flag)));
                                subpartlog = subpartlog.substring(subpartlog.indexOf("},") + 1);
                                System.out.println(resultlog);
                                arrayListLog.add(resultlog);
                                subpartID = subpartID.substring(subpartID.indexOf(valueID) + valueID.length());
                                resultID = Integer.parseInt(subpartID.substring(0, subpartID.indexOf(flag)));
                                subpartID= subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartID.add(resultID);
                                LatLng latLng = null;
                                try {
                                    latLng = new LatLng(resultlat,resultlog);
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        adapterSubpart = new ArrayAdapter<String>(Authorized_VolunteerRequest2.this, android.R.layout.simple_spinner_dropdown_item, arrayListSubpart);
                        subpartSpinner.setAdapter(adapterSubpart);
                        subpartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                dataSupartName = adapterSubpart.getItem(position).toString();
                               if(dataSupartName.equals("Seçilmedi")){
                                    Toast.makeText(getApplicationContext(), "Bir alt parça seçiniz!", Toast.LENGTH_SHORT).show();
                                }else{
                                    index = position;
                                    dataSupartID=arrayListSubpartID.get(index);
                                   lat=(arrayListLat.get(index-1));
                                   longt=(arrayListLog.get(index-1));
                                   LatLng latLng = null;
                                   try {
                                       latLng = new LatLng(lat,longt);
                                       Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(dataSupartName));
                                       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        System.out.println(response.toString());
                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        controlresponce=false;
                        Toast.makeText(getApplicationContext(), Authorized_VolunteerRequest.afetBolgesi+"de, gönüllüler için açık bir altparça mevcut değildir! ", Toast.LENGTH_SHORT).show();
                        finish();
                       // redirectActivity(Authorized_VolunteerRequest2.this,Authorized_VolunteerRequest.class);
                    }
                });
        if(controlresponce=false) {
            Toast.makeText(getApplicationContext(), "Seçili afet bölgesinde, gönüllüler için açık bir altparça mevcut değildir! ", Toast.LENGTH_SHORT).show();
            finish();
        }
        queue.add(request);
    }


    private void signOut() {
        LogoutHandler lout = new LogoutHandler(getApplicationContext());
        lout.updateUser();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Cikis basarili");
                    }
                });
    }
    @Override
    public void onBackPressed() {
        redirectActivity(this,Authorized_Anasayfa.class);
    }

    public void ClickMenu(View view) {
        //open drawer
        openDrawer(drawerLayout);
    }

    static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer Layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //when driver is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }
    /*
     *************************************** ASAGIDAKI KISIMLAR YONLENDIRMELERI AYARLAR
     */
    // IHBARLAR
    public void ClickAuthorizedNotice(View view) {
        redirectActivity(this, Authorized_Notification.class);
    }
    // AKTIF AFET
    public void ClickAuthorizeActiveDisaster(View view) {
        redirectActivity(this, Authorized_ActiveDisasters.class);
    }
    // PERSONEL KAYIT
    public void ClickAuthorizedPersonelRegistration(View view) {
        redirectActivity(this, Authorized_PersonelRegister.class);
    }
    // GONULLU ISTEKLERI
    public void ClickAuthrizedVolunteerRequest(View view) {
         redirectActivity(this, Authorized_VolunteerRequest.class);
    }
    //MESAJ

    // CIKIS
    public void ClickAuthorizedExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class );
    }
    public void ClickNotificationSend(View view) {
        redirectActivity(this, Authorized_Send_Notification.class );
    }

    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        redirectActivity(this, Authorized_Anasayfa.class );
    }


    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public void ClickAuthorizedSmartAssign(View view) {
        redirectActivity(this, Authorized_SmartAssign_Subpart.class );
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Assign_Team.class );
    }
}