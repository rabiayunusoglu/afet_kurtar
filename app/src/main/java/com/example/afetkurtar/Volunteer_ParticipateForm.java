package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;
import android.app.Activity;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Looper;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Volunteer_ParticipateForm extends AppCompatActivity {
    RequestQueue queue;
    RadioGroup radioGroupExperience, radioGroupFirstAid;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private RadioButton experience, firstAid;
    DrawerLayout drawerLayout;
    String url = "https://afetkurtar.site/api/volunteerUser/create.php";
    private EditText ad, soyad, adres, tc, dateBirth;
    private Button btn_gonder;
    Double latitude, longtitude;
    int tcNo;
    Date birthDate;
    private static boolean answerExperienced = false, answerFirstAid = false, controlmessage = false;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_participate_form);
        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Volunteer_ParticipateForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            controlmessage = true;
        } else {
            controlmessage = true;
            getCurrentLocation();
        }

        ad = (EditText) findViewById(R.id.participate_ad);
        soyad = (EditText) findViewById(R.id.participate_soyad);
        adres = (EditText) findViewById(R.id.participate_addres);
        tc = (EditText) findViewById(R.id.participate_tc);
        dateBirth = (EditText) findViewById(R.id.participate_date);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");


        radioGroupExperience = findViewById(R.id.groupexperienced);
        radioGroupFirstAid = findViewById(R.id.groupfistaid);

        btn_gonder = (Button) findViewById(R.id.participate_btn);
        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // tcNo = Integer.parseInt(String.valueOf(tc));
                int radioId = radioGroupExperience.getCheckedRadioButtonId();
                experience = findViewById(radioId);

                if (experience.getText().toString().equals("EVET")) {
                    answerExperienced = true;
                } else {
                    answerExperienced = false;
                }
                int radioId1 = radioGroupFirstAid.getCheckedRadioButtonId();
                firstAid = findViewById(radioId1);
                if (firstAid.getText().toString().equals("EVET")) {
                    answerFirstAid = true;
                } else {
                    answerFirstAid = false;
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("volunteerID",2);
                    obj.put("volunteerName", ad.getText().toString() + " " + soyad.getText().toString());
                   /* obj.put("address", adres.getText().toString());
                    obj.put("isExperienced", answerExperienced);
                    obj.put("haveFirstAidCert", answerFirstAid);
                    obj.put("requestedSubpart",null);
                    obj.put("responseSubpart",null);
                    obj.put("assignedTeamID",null);
                    obj.put("role",null);
                    obj.put("latitude", latitude);
                    obj.put("longitude", longtitude);
                    obj.put("locationTime",null);
                    obj.put("tc", null);
                    obj.put("birthDate", null);*/

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

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        LocationServices.getFusedLocationProviderClient(Volunteer_ParticipateForm.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Volunteer_ParticipateForm.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestlocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                    longtitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                }

            }
        }, Looper.getMainLooper());

    }


    public void ClickRegisterInfo() {
        redirectActivity(this, Volunteer_RegisterInfo.class);
    }

    private void signOut() {
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
        redirectActivity(this, Volunteer_ParticipateForm.class);
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