package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Authorized_VolunteerRequest3 extends AppCompatActivity  implements OnMapReadyCallback{

    ArrayList<String> arrayListSubpart = new ArrayList<>();
    ArrayList<String> arrayListSubpartAdres = new ArrayList<>();
    ArrayList<String> arrayListSubpartRole = new ArrayList<>();
    ArrayList<Integer> arrayListSubpartID = new ArrayList<Integer>();
    ArrayList<Double> arrayListSubpartlatitude = new ArrayList<Double>();
    ArrayList<Double> arrayListSubpartlongtitude = new ArrayList<Double>();
    ArrayList<Integer> arrayListSubpartTEAMID = new ArrayList<Integer>();
    ArrayList<Boolean> arrayListSubpartEXP = new ArrayList<Boolean>();
    ArrayList<Boolean> arrayListSubpartAid = new ArrayList<Boolean>();

    ArrayList<String> arrayListSubpartTC = new ArrayList<>();
    ArrayList<String> arrayListSubpartTEL = new ArrayList<>();
    ArrayList<String> arrayListSubpartBIRTHDATE = new ArrayList<>();

    ArrayAdapter<String> adapterSubpart;
    Button submits;
    public static JSONObject volInfo;
    String urlvol = "https://afetkurtar.site/api/volunteerUser/search.php";
    DrawerLayout drawerLayout;
    RequestQueue queuev, queue, queuecheck, queue1, queue2;
    static EditText afet, sbp;
    Spinner subpartSpinner;
    int index = 0;
    static double latitude, logtitude;
    static int dataSupartID = 0, dataSupartteamID = 0;
    static boolean dataSupartEXP = false;
    static boolean dataSupartAid = false;
    static String responceStringSubpart = "", dataSupartName = "", dataSupartAddress = "", dataSupartrole = "", dataSuparttel = "", dataSuparttc = "", dataSupartbirthdate = "";
    GoogleSignInClient mGoogleSignInClient;
    private GoogleMap mMap;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queuev = Volley.newRequestQueue(this);
        queue = Volley.newRequestQueue(this);
        queuecheck = Volley.newRequestQueue(this);
        queue1 = Volley.newRequestQueue(this);
        queue2 = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_authorized__volunteer_request3);
        drawerLayout = findViewById(R.id.drawer_layout3autreg);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        subpartSpinner = (Spinner) findViewById(R.id.spinnervol);
        afet = (EditText) findViewById(R.id.editAfet);
        afet.setText(String.valueOf(Authorized_VolunteerRequest.afetBolgesi));
        afet.setFocusableInTouchMode(false);
        sbp = (EditText) findViewById(R.id.editAfet2);
        sbp.setText(Authorized_VolunteerRequest2.dataSupartName);
        sbp.setFocusableInTouchMode(false);
        arrayListSubpart.add("Seçilmedi");
        arrayListSubpartID.add(0);
        loadSpinnerDataREquest(urlvol);

        submits = findViewById(R.id.gdrBtn);
        submits.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (dataSupartName.equals("Seçilmedi")) {
                    Toast.makeText(getApplicationContext(), "Bir gönüllü seçiniz!", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject obj = new JSONObject();
                    try {
//Get current date time
                        checkUser();
                        System.out.println(arrayListSubpartTEAMID.toString());
                        System.out.println(arrayListSubpartRole.toString());
                        System.out.println(arrayListSubpart.toString());
                        System.out.println(arrayListSubpartAdres.toString());
                        System.out.println(arrayListSubpartAid.toString());
                        System.out.println(arrayListSubpartEXP.toString());
                        System.out.println(arrayListSubpartBIRTHDATE.toString());
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formatDateTime = now.format(formatter);
                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formatDateTime1 = String.format(dataSupartbirthdate);
                        obj.put("volunteerID", dataSupartID);
                        obj.put("requestedSubpart", Authorized_VolunteerRequest2.dataSupartID);
                        obj.put("responseSubpart", Authorized_VolunteerRequest2.dataSupartID);
                        obj.put("volunteerName", dataSupartName.substring(0,dataSupartName.indexOf(",")));
                        obj.put("locationTime", formatDateTime);
                        obj.put("isExperienced", dataSupartEXP);
                        obj.put("haveFirstAidCert", dataSupartAid);
                        obj.put("address", dataSupartAddress);
                        obj.put("latitude", latitude);
                        obj.put("longitude", logtitude);
                        obj.put("role", "belirlenmedi");
                        obj.put("tc", "12345678912");
                        obj.put("tel", "12345678912");
                        obj.put("birthDate", formatDateTime1);


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "hata", Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/update.php", obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //userda type değiştir, (pere id ile kayıt et)artık etmiyozpere kayıt
                            System.out.println("YAZZZZZ++++++++++++++++++++");
                            checkUser();
                            updateUserTable();
                          //  addPersonel();
                            System.out.println(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(arrayListSubpart.toString());
                            System.out.println(arrayListSubpartID.toString());
                            System.out.println(arrayListSubpartAdres.toString());
                            System.out.println(arrayListSubpartAid.toString());
                            System.out.println(arrayListSubpartEXP.toString());
                            System.out.println(arrayListSubpartBIRTHDATE.toString());
                            System.out.println(arrayListSubpartlatitude.toString());
                            System.out.println(arrayListSubpartlongtitude.toString());
                            System.out.println(arrayListSubpartRole.toString());
                            System.out.println(arrayListSubpartTC.toString());
                            System.out.println(arrayListSubpartTEAMID.toString());
                            System.out.println(arrayListSubpartTEL.toString());
                            Toast.makeText(getApplicationContext(), "HATA-KABUL EDİLMEDİ", Toast.LENGTH_SHORT).show();

                        }
                    });
                    queue2.add(request);
                    redirectActivity(Authorized_VolunteerRequest3.this, Authorized_Anasayfa.class);
                }

            }


        });


        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.aut_subpart_Detail_map);
        mapFragment.getMapAsync((OnMapReadyCallback) Authorized_VolunteerRequest3.this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = null;
        try {
            latLng = new LatLng(Authorized_VolunteerRequest2.lat,Authorized_VolunteerRequest2.longt);
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(Authorized_VolunteerRequest2.dataSupartName));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void loadSpinnerDataREquest(String url) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("requestedSubpart" + Authorized_VolunteerRequest2.dataSupartID);
        System.out.println("--------------------------------------------------------------------------------");
        JSONObject obj = new JSONObject();
        try {

            obj.put("requestedSubpart", Authorized_VolunteerRequest2.dataSupartID);
            obj.put("responseSubpart", 0);
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
                        responceStringSubpart = response.toString();
                        responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("[") + 1, responceStringSubpart.length() - 2);

                        while (responceStringSubpart.indexOf("}") > 1) {
                            if (responceStringSubpart.contains("{")) {
                                String subpart = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartID = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartexp = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartaid = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartaddress = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartrole = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartlatitude = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartlongitude = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartTeamID = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);

                                String subpartTC = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartTEL = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);
                                String subpartDATE = responceStringSubpart.substring(responceStringSubpart.indexOf("{"), responceStringSubpart.indexOf("}") + 1);


                                if (responceStringSubpart.contains("},"))
                                    responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("}") + 2);
                                else {
                                    responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("}") + 1);
                                }
                                String value = "\"volunteerName\":\"";
                                String valueID = "\"volunteerID\":\"";
                                String valueExperienced = "\"isExperienced\":\"";
                                String valuefirstAid = "\"haveFirstAidCert\":\"";
                                String valueadr = "\"address\":\"";
                                String valuerole = "\"role\":\"";
                                String valuelat = "\"latitude\":\"";
                                String valueTeamID = "\"assignedTeamID\":\"";
                                String valuelong = "\"longitude\":\"";

                                String valueTC = "\"tc\":\"";
                                String valueTEL = "\"tel\":\"";
                                String valueDATE = "\"birthDate\":\"";

                                String flag = "\"";
                                String result = "", resultadr = "", resultRol = "", resultTc = "", resulttel = "", resultdate = "";
                                int resultID = 0, resultteamID = 0;
                                boolean resultExp = false, resultfirstAid = false;
                                double resultlat = 0, resultlong = 0;

                                subpartID = subpartID.substring(subpartID.indexOf(valueID) + valueID.length());
                                resultID = Integer.parseInt(subpartID.substring(0, subpartID.indexOf(flag)));
                                subpartID = subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartID.add(resultID);
                                subpartexp = subpartexp.substring(subpartexp.indexOf(valueExperienced) + valueExperienced.length());
                                if (Integer.parseInt(subpartexp.substring(0, subpartexp.indexOf(flag))) == 0)
                                    resultExp = false;
                                else
                                    resultExp = true;
                                subpartexp = subpartexp.substring(subpartexp.indexOf("},") + 1);
                                arrayListSubpartEXP.add(resultExp);
                                subpartaid = subpartaid.substring(subpartaid.indexOf(valuefirstAid) + valuefirstAid.length());
                                if (Integer.parseInt(subpartaid.substring(0, subpartaid.indexOf(flag))) == 0)
                                    resultfirstAid = false;
                                else
                                    resultfirstAid = true;
                                subpartaid = subpartaid.substring(subpartaid.indexOf("},") + 1);
                                arrayListSubpartAid.add(resultfirstAid);
                                subpart = subpart.substring(subpart.indexOf(value) + value.length());
                                result = subpart.substring(0, subpart.indexOf(flag));
                                subpart = subpart.substring(subpart.indexOf("},") + 1);
                                if (resultExp == true && resultfirstAid == true)
                                    arrayListSubpart.add(result + ", Deneyim:Var" + ", İlkyardım eğitim:Var");
                                else if (resultExp == true && resultfirstAid == false)
                                    arrayListSubpart.add(result + ", Deneyim:Var" + ", İlkyardım eğitim:Yok");
                                else if (resultExp == false && resultfirstAid == false)
                                    arrayListSubpart.add(result + ", Deneyim:Yok" + ", İlkyardım eğitim:Yok");
                                else if (resultExp == false && resultfirstAid == true)
                                    arrayListSubpart.add(result + ", Deneyim:Yok" + ", İlkyardım eğitim:Var");
                                subpartTeamID = subpartTeamID.substring(subpartTeamID.indexOf(valueTeamID) + valueTeamID.length());
                                resultteamID = Integer.parseInt(subpartTeamID.substring(0, subpartTeamID.indexOf(flag)));
                                //  subpartTeamID = subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartTEAMID.add(resultteamID);
                                subpartaddress = subpartaddress.substring(subpartaddress.indexOf(valueadr) + valueadr.length());
                                resultadr = (subpartaddress.substring(0, subpartaddress.indexOf(flag)));
                                //  subpartTeamID = subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartAdres.add(resultadr);
                                subpartrole = subpartrole.substring(subpartrole.indexOf(valuerole) + valuerole.length());
                                resultRol = (subpartrole.substring(0, subpartrole.indexOf(flag)));
                                //  subpartTeamID = subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartRole.add(resultRol);
                                subpartlatitude = subpartlatitude.substring(subpartlatitude.indexOf(valuelat) + valuelat.length());
                                resultlat = Double.parseDouble(subpartlatitude.substring(0, subpartlatitude.indexOf(flag)));
                                //  subpartTeamID = subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartlatitude.add(resultlat);
                                subpartlongitude = subpartlongitude.substring(subpartlongitude.indexOf(valuelong) + valuelong.length());
                                resultlong = Double.parseDouble(subpartlongitude.substring(0, subpartlongitude.indexOf(flag)));
                                //  subpartTeamID = subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartlongtitude.add(resultlong);
                                subpartTC = subpartTC.substring(subpartTC.indexOf(valueTC) + valueTC.length());
                                resultTc = (subpartTC.substring(0, subpartTC.indexOf(flag)));
                                //  subpartTeamID = subpartID.substring(subpartID.indexOf("},") + 1);

                            }

                        }

                        adapterSubpart = new ArrayAdapter<String>(Authorized_VolunteerRequest3.this, android.R.layout.simple_spinner_dropdown_item, arrayListSubpart);
                        subpartSpinner.setAdapter(adapterSubpart);
                        subpartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                dataSupartName = adapterSubpart.getItem(position).toString();
                                if (dataSupartName.equals("Seçilmedi")) {
                                    Toast.makeText(getApplicationContext(), "Bir gönüllü seçiniz!", Toast.LENGTH_SHORT).show();
                                } else {
                                    index = position;
                                    dataSupartID = arrayListSubpartID.get(index);
                                    dataSupartAid = arrayListSubpartAid.get(index - 1);
                                    dataSupartEXP = arrayListSubpartEXP.get(index - 1);
                                    dataSupartrole = arrayListSubpartRole.get(index - 1);
                                    dataSupartAddress = arrayListSubpartAdres.get(index - 1);
                                    latitude = arrayListSubpartlatitude.get(index - 1);
                                    logtitude = arrayListSubpartlongtitude.get(index - 1);
                                    dataSupartteamID = arrayListSubpartTEAMID.get(index - 1);
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

                        Toast.makeText(getApplicationContext(), Authorized_VolunteerRequest.afetBolgesi + "nin " + Authorized_VolunteerRequest2.dataSupartName + "ni tercih eden bir gönüllü yoktur!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        queue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addPersonel() {
        try {
            //Get current date time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            JSONObject obj = new JSONObject();
            try {
                obj.put("personnelID", dataSupartID);
                obj.put("personnelName", dataSupartName.substring(0, dataSupartName.indexOf(",")));
                obj.put("personnelEmail", email);
                obj.put("personnelRole", "Belirlenmedi");
                obj.put("latitude", latitude);
                obj.put("teamID", 0);
                obj.put("longitude", logtitude);
                obj.put("locationTime", formatDateTime);
                obj.put("institution", "gönüllü katıldı");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("user da hata*********************************************");
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/create.php", obj, new Response.Listener<JSONObject>() {
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
            Toast.makeText(Authorized_VolunteerRequest3.this, "Gönüllü isteği kabul edildi.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    private void updateUserTable() {

//vole çevircez artik personel değli
        JSONObject tmp = new JSONObject();
        try {
            tmp.put("userID", dataSupartID);
            tmp.put("userType", "personnelUser");
            tmp.put("userName", dataSupartName.substring(0, dataSupartName.indexOf(",")));
            tmp.put("userEmail", email);
            tmp.put("userToken", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            //   System.out.println(tmp.toString() + "**********************************************************");
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, // the request method
                    "https://afetkurtar.site/api/users/update.php", // the URL
                    tmp, // the parameters for the php
                    new Response.Listener<JSONObject>() { // the response listener
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },
                    new Response.ErrorListener() { // the error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                            error.printStackTrace();

                        }
                    });
            queue.add(request);
        } catch (Exception e) {
            System.out.println("eror");
        }
    }

    static String email = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkUser() {

        String url = "https://afetkurtar.site/api/users/search.php";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("userID", dataSupartID);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                new JSONObject(params), // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                        //  System.out.println("response dönüyor");
                        System.out.println(response.toString());

                        JSONObject tmpJson = null;
                        String type = "";
                        try {
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            tmpJson = new JSONObject(cevap);
                            email = tmpJson.getString("userEmail");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        email = "";
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
        redirectActivity(this, MainActivity.class);
    }

    public void ClickNotificationSend(View view) {
        redirectActivity(this, Authorized_Send_Notification.class);
    }

    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        redirectActivity(this, Authorized_Anasayfa.class);
    }

    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Anasayfa.class);
    }

    public void ClickAuthorizedEkipman(View view) {
        redirectActivity(this, Authorized_Anasayfa.class);
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Assign_Team.class );
    }
}