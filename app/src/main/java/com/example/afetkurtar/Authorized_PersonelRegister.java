package com.example.afetkurtar;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Authorized_PersonelRegister extends AppCompatActivity {
    RequestQueue queue;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    DrawerLayout drawerLayout;
    Double latitude, longtitude;
    String url = "https://afetkurtar.site/api/personnelUser/create.php";
    GoogleSignInClient mGoogleSignInClient;
    private EditText email, name, kurum, rol;
    Button register;
    long locationTime;
    private static boolean controlmessage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_register);

        drawerLayout = findViewById(R.id.auth_drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Authorized_PersonelRegister.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            controlmessage = true;
        } else {
            controlmessage = true;
            getCurrentLocation();
        }
        email = findViewById(R.id.per_email);
        name = findViewById(R.id.per_name);
        kurum = findViewById(R.id.per_kurum);
        rol = findViewById(R.id.per_rol);

        register = findViewById(R.id.kayit_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    //Get current date time
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatDateTime = now.format(formatter);
                    if (name.length() == 0 || email.length() == 0 || kurum.length() == 0 || rol.length() == 0)
                        throw new Exception("");
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("personnelID", 7);
                        obj.put("personnelName", name.getText().toString());
                        obj.put("personnelEmail", email.getText().toString());
                        obj.put("personnelRole", rol.getText().toString());
                        obj.put("latitude", latitude);
                        obj.put("longitude", longtitude);
                        obj.put("locationTime", formatDateTime);
                        obj.put("institution", kurum.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("RESPONSE:********************" + response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                        }
                    });
                    queue.add(request);
                    Toast.makeText(Authorized_PersonelRegister.this, "Kaydınız Başarıyla Gerçekleşti :)", Toast.LENGTH_SHORT).show();
                    redirectActivity(Authorized_PersonelRegister.this, Authorized_Anasayfa.class);
                } catch (Exception e) {
                    if (email.length() == 0)
                        Toast.makeText(Authorized_PersonelRegister.this, "Personel emalini girmelisiniz!", Toast.LENGTH_SHORT).show();
                    else if (name.length() == 0)
                        Toast.makeText(Authorized_PersonelRegister.this, "Personel ad ve soyadı tamamen girmelisiniz!", Toast.LENGTH_SHORT).show();
                    else if (kurum.length() == 0)
                        Toast.makeText(Authorized_PersonelRegister.this, "Personel krumunu girmelisiniz!", Toast.LENGTH_SHORT).show();
                    else if (rol.length() == 0)
                        Toast.makeText(Authorized_PersonelRegister.this, "Personel rolünü girmelisiniz!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(Authorized_PersonelRegister.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Authorized_PersonelRegister.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestlocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                    longtitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                    locationTime = locationResult.getLastLocation().getTime();
                }

            }
        }, Looper.getMainLooper());

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
        //  redirectActivity(this, Authorized_Notification.class);
    }

    // PERSONEL KAYIT
    public void ClickAuthorizedPersonelRegistration(View view) {
        redirectActivity(this, Authorized_PersonelRegister.class);
    }

    // GONULLU ISTEKLERI
    public void ClickAuthrizedVolunteerRequest(View view) {
        // redirectActivity(this, Authorized_Notification.class);
    }

    //MESAJ
    public void ClickAuthorizedMessage(View view) {

    }

    // CIKIS
    public void ClickAuthorizedExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class);
    }

    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        //redirectActivity(this, Authorized_Anasayfa.class );
    }


}