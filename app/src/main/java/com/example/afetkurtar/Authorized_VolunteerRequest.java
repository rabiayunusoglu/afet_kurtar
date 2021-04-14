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

public class Authorized_VolunteerRequest extends AppCompatActivity  implements OnMapReadyCallback{

    ArrayList<String> arrayListAfet = new ArrayList<>();
    ArrayAdapter<String> adapterAfet;
    ArrayList<Double> arrayListLat = new ArrayList<>();
    ArrayList<Double> arrayListLog = new ArrayList<>();
    ArrayAdapter<String> adapterlat;
    ArrayAdapter<String> adapterlog;
    Button submit;
    private GoogleMap mMap;
    private Geocoder geocoder;
    Spinner subpartSpinner;
    static int dataSupartID = 0;
    String urlAfet = "https://afetkurtar.site/api/disasterEvents/search.php";
    String url1 = "https://afetkurtar.site/api/volunteerUser/update.php";
    DrawerLayout drawerLayout;
    RequestQueue queue;
    Spinner afetSpinner;
    int index = 0;
    static double lat=0.0,longt=0.0;
    static boolean controlresponce = false;
    static String responceStringAfet = "", afetBolgesi = "";
    static int afetID;
    String urlSubpart = "https://afetkurtar.site/api/subpart/search.php";
    static String responceStringSubpart = "", dataSupartName = "";
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_authorized__volunteer_request);
        drawerLayout = findViewById(R.id.drawer_layoutautreg);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        afetSpinner = (Spinner) findViewById(R.id.spinnerAfet);
        loadSpinnerDataAfet(urlAfet);
        findViewById(R.id.listVOLBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Authorized_VolunteerRequest.this, Authorized_Vol_Req_List.class);
            }
        });

        submit = findViewById(R.id.gdrBtnAfet);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // setContentView(R.layout.activity_volunteer__participate_request2);
                if (afetBolgesi.equals("Seçilmedi")) {
                    Toast.makeText(getApplicationContext(), "Afet bölgesi seçiniz." + "", Toast.LENGTH_SHORT).show();
                } else
                    redirectActivity(Authorized_VolunteerRequest.this, Authorized_VolunteerRequest2.class);
            }
        });

        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.aut_disaster_Detail_map);
        mapFragment.getMapAsync((OnMapReadyCallback) Authorized_VolunteerRequest.this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    private void loadSpinnerDataAfet(String url) {
        arrayListAfet.add("Seçilmedi");
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // the request method
                url, // the URL
                null, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                        responceStringAfet = response.toString();
                        responceStringAfet = responceStringAfet.substring(responceStringAfet.indexOf("[") + 1, responceStringAfet.length() - 2);

                        while (responceStringAfet.indexOf("}") > 1) {
                            if (responceStringAfet.contains("{")) {

                                String subpartlat = responceStringAfet.substring(responceStringAfet.indexOf("{"), responceStringAfet.indexOf("}") + 1);
                                String subpartlog= responceStringAfet.substring(responceStringAfet.indexOf("{"), responceStringAfet.indexOf("}") + 1);

                                String subpart = responceStringAfet.substring(responceStringAfet.indexOf("{"), responceStringAfet.indexOf("}") + 1);
                                if (responceStringAfet.contains("},"))
                                    responceStringAfet = responceStringAfet.substring(responceStringAfet.indexOf("}") + 2);
                                else {
                                    responceStringAfet = responceStringAfet.substring(responceStringAfet.indexOf("}") + 1);
                                }
                                String value = "\"disasterName\":\"";
                                String valuelat = "\"latitude\":\"";
                                String valuelog = "\"longitude\":\"";
                                String flag = "\"";
                                String result = "";
                                Double resultlat =0.0;
                                Double resultlog = 0.0;
                                subpart = subpart.substring(subpart.indexOf(value) + value.length());
                                result = subpart.substring(0, subpart.indexOf(flag));
                                subpart = subpart.substring(subpart.indexOf("},") + 1);
                                System.out.println("result");
                                arrayListAfet.add(result);
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
                        adapterAfet = new ArrayAdapter<String>(Authorized_VolunteerRequest.this, android.R.layout.simple_spinner_dropdown_item, arrayListAfet);
                        afetSpinner.setAdapter(adapterAfet);
                        afetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                // afetID = Integer.parseInt(adapterAfet.getItem(position).toString());
                                afetBolgesi = adapterAfet.getItem(position).toString();
                                if (afetBolgesi.equals("Seçilmedi")) {
                                    Toast.makeText(getApplicationContext(), "Afet bölgesi seçiniz." + "", Toast.LENGTH_SHORT).show();
                                } else{
                                    index = position;
                                    lat=(arrayListLat.get(index-1));
                                    longt=(arrayListLog.get(index-1));
                                    LatLng latLng = null;
                                    try {
                                        latLng = new LatLng(lat,longt);
                                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(afetBolgesi));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //Toast.makeText(getApplicationContext(), afetBolgesi + "", Toast.LENGTH_SHORT).show();

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
                        System.out.println(error);
                    }
                });

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
        //redirectActivity(this, Authorized_VolunteerRequest.class);
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

        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Personeller.class );
    }
    public void ClickAuthorizedSmartAssign(View view) {
        redirectActivity(this, Authorized_SmartAssign_Subpart.class );
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Assign_Team.class );
    }

}