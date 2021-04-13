package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Volunteer_RegisterInfo extends AppCompatActivity {
    RequestQueue queue, queuecheck;
    RadioGroup radioGroupExperience, radioGroupFirstAid;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private RadioButton experience, firstAid;
    DrawerLayout drawerLayout;
    String url = "https://afetkurtar.site/api/volunteerUser/update.php";
    private static EditText ad;
    private static EditText soyad;
    private EditText adres;
    public static JSONObject volInfo;
    private EditText tc,tel;
    long locationTime;
    private static EditText dateBirth;
    private Button btn_gonder, getir;
    Double latitude, longtitude;
    private static boolean answerExperienced = false, answerFirstAid = false, controlmessage = false;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        queuecheck = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_volunteer__register_info);
        drawerLayout = findViewById(R.id.drawer_layoutreg);
        try {
            checkUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Volunteer_RegisterInfo.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            controlmessage = true;
        } else {
            controlmessage = true;
            getCurrentLocation();
        }

        ad = (EditText) findViewById(R.id.participate_name);
        adres = (EditText) findViewById(R.id.participate_addres);
        tc = (EditText) findViewById(R.id.participate_tc);
        tel = (EditText) findViewById(R.id.participate_tel);
        dateBirth = (EditText) findViewById(R.id.participate_date);
        radioGroupExperience = findViewById(R.id.groupexperienced);
        radioGroupFirstAid = findViewById(R.id.groupfistaid);

        getir = findViewById(R.id.buttonGetir);
        getir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkUser();
                    ad.setText(volInfo.get("volunteerName").toString());
                    adres.setText(volInfo.get("address").toString());
                    tc.setText(volInfo.get("tc").toString());
                    tel.setText(volInfo.get("tel").toString());
                    dateBirth.setText(volInfo.get("birthDate").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_gonder = (Button) findViewById(R.id.participate_Guncelbtn);
        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                try {
                    //Get current date time
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatDateTime = now.format(formatter);

                    //Get isExperienced havefirstAid
                    int radioId = radioGroupExperience.getCheckedRadioButtonId();
                    experience = findViewById(radioId);
                    int radioId1 = radioGroupFirstAid.getCheckedRadioButtonId();
                    firstAid = findViewById(radioId1);

                    if (ad.length() == 0  || tel==null|| tel.getText().toString().length() != 11||tc.getText().toString().length() != 11 || dateBirth.getText().length() != 10 || dateBirth.getText().toString().substring(0, 4).contains("-") || dateBirth.getText().toString().contains(".") || dateBirth.getText().toString().contains("/") || controlAge() == false || experience == null || firstAid == null || adres.length() == 0 ) {
                        throw new Exception("");
                    }
                    //control experinece and first aid
                    if (experience.getText().toString().equals("EVET")) {
                        answerExperienced = true;
                    } else {
                        answerExperienced = false;
                    }
                    if (firstAid.getText().toString().equals("EVET")) {
                        answerFirstAid = true;
                    } else {
                        answerFirstAid = false;
                    }
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("volunteerID", MainActivity.userInfo.get("userID"));
                        obj.put("volunteerName", ad.getText().toString());
                        obj.put("address", adres.getText().toString());
                        obj.put("isExperienced", answerExperienced);
                        obj.put("haveFirstAidCert", answerFirstAid);
                        obj.put("latitude", latitude);
                        obj.put("longitude", longtitude);
                        obj.put("locationTime", formatDateTime);
                        obj.put("tc", tc.getText().toString());
                        obj.put("tel", tel.getText().toString());
                        obj.put("birthDate", dateBirth.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
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
                    Toast.makeText(Volunteer_RegisterInfo.this, "Güncelleme Başarıyla Gerçekleşti :)", Toast.LENGTH_SHORT).show();
                    redirectActivity(Volunteer_RegisterInfo.this, Volunteer_Anasayfa.class);
                } catch (Exception e) {
                    System.out.println("*******************" + controlmessage);
                    if (ad.length() == 0)
                        Toast.makeText(Volunteer_RegisterInfo.this, "Adınız ve soyadınız boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                   else if (tc.getText().toString().length() != 11)
                        Toast.makeText(Volunteer_RegisterInfo.this, "TC numaranızı doğru girdiğinizden emin olunuz!", Toast.LENGTH_SHORT).show();
                    else if (dateBirth == null)
                        Toast.makeText(Volunteer_RegisterInfo.this, "Doğum Tarihi kısmı boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                    else if (dateBirth.getText().length() != 10 || dateBirth.getText().toString().contains(".") || dateBirth.getText().toString().contains("/") || dateBirth.getText().toString().substring(0, 4).contains("-"))
                        Toast.makeText(Volunteer_RegisterInfo.this, "Doğum Tarihi kısmı yyyy-mm-dd formatında giriniz!", Toast.LENGTH_SHORT).show();
                    else if (controlAge() == false) {
                        Toast.makeText(Volunteer_RegisterInfo.this, "18 yaş altındaki vatandaşlar gönüllü olamaz!", Toast.LENGTH_SHORT).show();
                    }
                    else if (experience == null)
                        Toast.makeText(Volunteer_RegisterInfo.this, "Daha önce arama kurtarma olayına katılma sorgusu boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                    else if (firstAid == null)
                        Toast.makeText(Volunteer_RegisterInfo.this, "İlk yardımı eğitimi sorgusu boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                    else if (tel == null)
                        Toast.makeText(Volunteer_RegisterInfo.this, "Telefon numarası sorgusu boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                    else if (tel.getText().toString().length() != 11)
                        Toast.makeText(Volunteer_RegisterInfo.this, "Telefon numaranızı doğru girdiğinizden emin olunuz!", Toast.LENGTH_SHORT).show();
                    else if (adres.length() == 0)
                        Toast.makeText(Volunteer_RegisterInfo.this, "Adres boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Volunteer_RegisterInfo.this, "Tekrar Deneyiniz..", Toast.LENGTH_LONG).show();
                    System.out.println(e.toString());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            checkUser();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void checkUser() throws JSONException {
        String url = "https://afetkurtar.site/api/volunteerUser/search.php";

        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("volunteerID", MainActivity.userID);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                new JSONObject(params), // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                        //  System.out.println("response dönüyor");
                        System.out.println(response.toString());


                        String type = "";
                        try {
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            JSONObject tmpJson = new JSONObject(cevap);
                            volInfo = new JSONObject(cevap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
        queuecheck.add(request);
    }

    private boolean controlName() {
        String name = ad.getText().toString();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == 231 || name.charAt(i) == 305 || name.charAt(i) == 287 || name.charAt(i) == 246 || name.charAt(i) == 351 || name.charAt(i) == 252 || name.charAt(i) == 199 || name.charAt(i) == 304 || name.charAt(i) == 286 || name.charAt(i) == 214 || name.charAt(i) == 350 || name.charAt(i) == 220 ||
                    (name.charAt(i) >= 65 && name.charAt(i) <= 90) ||
                    (name.charAt(i) >= 97 && name.charAt(i) <= 122)) {

            } else
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean controlAge() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int byear = Integer.parseInt(dateBirth.getText().toString().substring(0, 4));
        int bmounth = Integer.parseInt(dateBirth.getText().toString().substring(5, 7));
        int bday = Integer.parseInt(dateBirth.getText().toString().substring(8));

        int y = year - byear;

        if (y > 18 && bmounth<=12 && bday<=31)
            return true;
        else if (y == 18) {
            if (bmounth >= now.getMonthValue() && bmounth<=12 && bday<=31) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(Volunteer_RegisterInfo.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Volunteer_RegisterInfo.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestlocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                    longtitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                    locationTime = locationResult.getLastLocation().getTime();
                }

            }
        }, Looper.getMainLooper());

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

    public void ClickRegisterInfo() {
        redirectActivity(this, Volunteer_RegisterInfo.class);
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


    public void ClickAnasayfa(View view) {
        //redirect activity to dashboard
        redirectActivity(this, Volunteer_Anasayfa.class);
    }


    public void ClickParticipateRequest(View view) {
        //redirect activity to volunter
        redirectActivity(this, Volunteer_ParticipateRequest.class);
    }

    public void ClickEmergency(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_Emergency.class);
    }

    public void ClickParticipateForm(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_RegisterInfo.class);
    }

    public void ClickExit(View view) {
        //redirect activity to main screen
        signOut();
        redirectActivity(this, MainActivity.class);
    }

    public void ClickPersonel(View view) {
        //redirect activity to main screen
        redirectActivity(this, Personel_Progress.class);
    }

    public void ClickAfetBolgesi(View view) {
        //redirect activity to main screen
        redirectActivity(this, Afet_Bolgesi.class);
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