package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import java.util.List;
import java.util.Locale;

public class Disaster_Details extends AppCompatActivity implements OnMapReadyCallback {
    RequestQueue queue;
    public static int subpartID,disID;
    GoogleSignInClient mGoogleSignInClient;
    public static String isOpenForVolunteer;
    public static String subpartName,disname,missingPerson,rescuedPerson,status,level,
            subpartAdres,
            subpartlatitude,
            subpartlongitude;
    JSONObject data;
    private GoogleMap mMap;
    private Geocoder geocoder;
    static int k = 0;
    ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
    static ArrayList<JSONObject> subpartList = new ArrayList<JSONObject>();
    static String selectedSubpartID = "";
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster__details);
        drawerLayout = findViewById(R.id.detail_dis);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        queue = Volley.newRequestQueue(this);
        // findViewById(R.id.Create_new_Subpart).setOnClickListener(this::onClick);
        queue = Volley.newRequestQueue(this);

        findViewById(R.id.afetolusturBTN).setOnClickListener(this::onClick);

        list2 = new ArrayList<JSONObject>();
        getData("");
        // System.out.println(data.toString());


        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.disaster_Detail_map);
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

        LatLng latLng = null;
        try {
            latLng = new LatLng(Double.parseDouble(Authorized_ActiveDisasters.disasterlatitude), Double.parseDouble(Authorized_ActiveDisasters.disasterlongitude));
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
            case R.id.afetolusturBTN:
                Intent intent = new Intent(this, Create_Subpart_On_Map.class);
                JSONObject disDetail = new JSONObject();
                try {
                    System.out.println("DISASTER ID NEEEEE : *" + Authorized_ActiveDisasters.disasterID + "*");
                    System.out.println("DISASTER NAME NEEEEE : *" + Authorized_ActiveDisasters.disasterName + "*");
                    disDetail.put("disID", Authorized_ActiveDisasters.disasterID);
                    disDetail.put("disName", Authorized_ActiveDisasters.disasterName);
                }catch (Exception e){
                    System.out.println("CATH e MI GIDIYOOOOOOOOOOOOOO");
                    e.printStackTrace();
                }
                System.out.println("ALLAH ICIN OL LAAAAAAAAAAAAAAAAAAN");
                intent.putExtra("disaster_detail", disDetail.toString());
                startActivity(intent);
                //  finish();
                break;
            case R.id.HistoryText:
                TextView linear = (TextView) v.findViewById(R.id.HistoryText);
                //System.out.println("What is in HistoryText ,Linear: " + linear.getText().toString());
                String tmp = (String) linear.getText();
                //System.out.println("What is in HistoryText ,tmp: " + tmp.toString());
                tmp = tmp.substring(tmp.indexOf(" ") + 1, tmp.indexOf("İsim")).trim();
                // tmp = tmp.substring(0,tmp.indexOf(" ")).trim(); // **************************************** MESAJI DEGISTIRIRSEN BURAYI AYARLA
                // System.out.println(tmp);
                try{
                    selectedSubpartID = tmp.trim();
                }catch (Exception e){
                    e.getMessage();
                }
                //System.out.println("What is in HistoryText After String compute,tmp: " + tmp.toString());
                Intent asd = new Intent(this, Disaster_Subpart.class);
                JSONObject json = new JSONObject();
                for (JSONObject x : list2) {
                    try {
                        System.out.println(tmp.trim()+"**************************************************");
                        if (x.getString("subpartID").equals(tmp.trim())) {
                            subpartID=x.getInt("subpartID");
                            disID=Authorized_ActiveDisasters.disasterID;
                            subpartlatitude=x.getString("latitude");
                            subpartlongitude=x.getString("longitude");
                            subpartAdres=x.getString("address");
                            missingPerson=x.getString("missingPerson");
                            rescuedPerson=x.getString("rescuedPerson");
                            isOpenForVolunteer=x.getString("isOpenForVolunteers");
                            subpartName=x.getString("subpartName");
                            status=x.getString("status");
                            level=x.getString("emergencyLevel");
                            disname=Authorized_ActiveDisasters.disasterName;
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

        //this method creates subparts on map
       subpartList = list2;
        initSubpartsOnMap(subpartList);
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
        Intent intent = new Intent(this, Create_Subpart_On_Map.class);
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
    public void initSubpartsOnMap(ArrayList<JSONObject> myList){
        createSubpartMarkerOnMap(myList);
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
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Disaster_Details.this));
                markerList.add(subMarker);



            }catch (Exception e){
                e.getMessage();
            }
        }
        try{
                /*
                double avarageLatitude = (latitudeStart + latitudeEnd)/2;
                double avarageLongitude = (longitudeStart + longitudeEnd)/2;
                */

            LatLng forZoomLatlng = new LatLng(Authorized_ActiveDisasters.latitude,Authorized_ActiveDisasters.longtitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(forZoomLatlng,5));
        }catch (Exception e){
            e.getMessage();
        }

        return markerList;
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
        System.out.println("logo");
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


    /*
     *************************************** ASAGIDAKI KISIMLAR YONLENDIRMELERI AYARLAR
     */
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
        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public void ClickAuthorizedEkipman(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }
}