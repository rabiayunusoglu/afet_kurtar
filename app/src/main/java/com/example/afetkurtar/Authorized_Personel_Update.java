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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Authorized_Personel_Update extends AppCompatActivity {
    RequestQueue queue, queue1, queue2, queue3, queue5;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    DrawerLayout drawerLayout;
    Double latitude, longtitude;
    String url = "https://afetkurtar.site/api/personnelUser/update.php";
    GoogleSignInClient mGoogleSignInClient;
    private EditText email, name, kurum, rol;
    Button register, delete;
    long locationTime;
    static JSONObject perInfo, volInfo;
    static int perID;
    static boolean temp = false;
    static String emergencyins, adrres, exp, aid, bdate, lat, log, tc, tel, lotime;
    static String disasterTeamid, disasterrole;
    private static boolean controlmessage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        queue1 = Volley.newRequestQueue(this);
        queue2 = Volley.newRequestQueue(this);
        queue3 = Volley.newRequestQueue(this);
        queue5 = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized__personel__update);
        drawerLayout = findViewById(R.id.drawer_layout_per_updat);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Authorized_Personel_Update.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            controlmessage = true;
        } else {
            controlmessage = true;
            getCurrentLocation();
        }
        getPersonel();
        email = findViewById(R.id.per_email);
        email.setText(Authorized_Personeller.disasteremail);
        name = findViewById(R.id.per_name);
        name.setText(Authorized_Personeller.disasterName);
        kurum = findViewById(R.id.per_kurum);
        kurum.setText(Authorized_Personeller.emergencyins);
        rol = findViewById(R.id.per_rol);
        rol.setText(Authorized_Personeller.disasterrole);
        delete = findViewById(R.id.delete_per_Btn);
        register = findViewById(R.id.kayit_btn);
        checkVOL();
        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (temp) {
                    deleteTurnType();//perden silinip,  userdaki type vol yapıcaz,volun req ve response tema ıd ve role sıfırlancak
                    updateVol();
                    // deletePer();
                } else {
                    deletePer();//perden silicez, userdanda silicez
                    deleteUser();
                }
                Toast.makeText(getApplicationContext(), "Silindi.", Toast.LENGTH_SHORT).show();
                redirectActivity(Authorized_Personel_Update.this, Authorized_Anasayfa.class);
            }


        });
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                updatePersonel();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateVol() {
        JSONObject obj = new JSONObject();
        try {
//Get current date time

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formatDateTime1 = now.format(formatter);
            System.out.println("*********************************************");
            obj.put("volunteerID", Authorized_Personeller.disasterID);
            System.out.println(Authorized_Personeller.disasterID);
            System.out.println("*********************************************");
            //req ve res yazılmadı yazılmayınca değişiklik olmuypr
            obj.put("volunteerName", name.getText().toString());
            obj.put("locationTime", formatDateTime);
            obj.put("latitude", latitude);
            obj.put("longitude", longtitude);
            obj.put("role", "Belirlenmedi");
            obj.put("address", volInfo.getString("address"));
            obj.put("isExperienced", volInfo.getString("isExperienced"));
            obj.put("haveFirstAidCert", volInfo.getString("haveFirstAidCert"));
            obj.put("tc", volInfo.getString("tc"));
            obj.put("tel", volInfo.getString("tel"));
            obj.put("birthDate", volInfo.getString("birthDate"));
            obj.put("requestedSubpart", "0");
            obj.put("responseSubpart", "0");
            obj.put("assignedTeamID", "0");


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "hata", Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //userda type değiştir, pere id ile kayıt et
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "hata", Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);
    }

    private void deleteUser() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("userID", Authorized_Personeller.disasterID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/users/delete.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());

                            //redirectActivity(Authorized_Personel_Update.this, Authorized_Personeller.class);

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
        queue1.add(jsonObjectRequest);
    }

    public void deleteTurnType() {
        updateUser();

    }

    public void updateUser() {
//vole çevircez artik personel değli
        JSONObject tmp = new JSONObject();
        try {
            tmp.put("userID", Authorized_Personeller.disasterID);
            tmp.put("userType", "volunteerUser");
            tmp.put("userName", name.getText().toString());
            tmp.put("email", email.getText().toString());
            tmp.put("createTime", Authorized_Personeller.time);
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
            queue3.add(request);
        } catch (Exception e) {
            System.out.println("eror");
        }
    }

    private void checkVOL() {
        JSONObject obj = new JSONObject();

        //JSONObject personnelObject = new JSONObject();

        try {
            obj.put("volunteerID", Authorized_Personeller.disasterID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            temp = true;
                            System.out.println(response.toString());


                            String type = "";
                            try {
                                String cevap = response.getString("records");
                                cevap = cevap.substring(1, cevap.length() - 1);
                                JSONObject tmpJson = new JSONObject(cevap);
                                volInfo = new JSONObject(cevap);
                                adrres = volInfo.getString("address");
                                exp = volInfo.getString("isExperienced");
                                aid = volInfo.getString("haveFirstAidCert");
                                lat = volInfo.getString("latitude");
                                log = volInfo.getString("longitude");
                                lotime = volInfo.getString("locationTime");
                                tc = volInfo.getString("tc");
                                tel = volInfo.getString("tel");
                                bdate = volInfo.getString("birthDate");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        temp = false;
                        System.out.println(error);
                    }
                });
        queue2.add(jsonObjectRequest);
    }

    public void deletePer() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", Authorized_Personeller.disasterID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/delete.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());

                            //redirectActivity(Authorized_Personel_Update.this, Authorized_Personeller.class);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePersonel() {
        try {
            if (name.length() == 0 || !(email.getText().toString().contains("@gmail.com")) || email.length() == 0 || kurum.length() == 0 || rol.length() == 0)
                throw new Exception("");
            String url1 = "https://afetkurtar.site/api/users/update.php";
            LocalDateTime now1 = LocalDateTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime1 = now1.format(formatter1);
            Map<String, String> params1 = new HashMap<String, String>();
            params1.put("userID", Authorized_Personeller.disasterID + "");
            params1.put("userType", "personnelUser");
            params1.put("userName", name.getText().toString());
            params1.put("email", email.getText().toString());
            params1.put("createTime", formatDateTime1);
            JsonObjectRequest request1 = new JsonObjectRequest(
                    Request.Method.POST, // the request method
                    url1, // the URL
                    new JSONObject(params1), // the parameters for the php
                    new Response.Listener<JSONObject>() { // the response listener

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            String cevap = response.toString().substring(0, response.toString().lastIndexOf("\""));
                            cevap = cevap.substring(cevap.toString().lastIndexOf("\"") + 1);
                            try {
                                //Get current date time
                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                String formatDateTime = now.format(formatter);
                                if (name.length() == 0 || !(email.getText().toString().contains("@gmail.com")) || email.length() == 0 || kurum.length() == 0 || rol.length() == 0)
                                    throw new Exception("");
                                JSONObject obj = new JSONObject();
                                try {
                                    getPersonel();
                                    obj.put("personnelID", Authorized_Personeller.disasterID);
                                    obj.put("personnelName", name.getText().toString());
                                    obj.put("personnelEmail", email.getText().toString());
                                    obj.put("personnelRole", rol.getText().toString());
                                    obj.put("latitude", latitude);
                                    obj.put("teamID", disasterTeamid);
                                    obj.put("longitude", longtitude);
                                    obj.put("locationTime", formatDateTime);
                                    obj.put("institution", kurum.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Authorized_Personel_Update.this, "Güncellemede user hata oldu.", Toast.LENGTH_SHORT).show();
                                }
                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response.toString());
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Authorized_Personel_Update.this, "Güncellemede hata oldu.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);
                                Toast.makeText(Authorized_Personel_Update.this, "Güncelleme Başarıyla Gerçekleşti.", Toast.LENGTH_SHORT).show();
                                redirectActivity(Authorized_Personel_Update.this, Authorized_Anasayfa.class);
                            } catch (Exception e) {
                                if (email.length() == 0)
                                    Toast.makeText(Authorized_Personel_Update.this, "Personel emalini girmelisiniz!", Toast.LENGTH_SHORT).show();
                                else if (!(email.getText().toString().contains("@gmail.com")))
                                    Toast.makeText(Authorized_Personel_Update.this, "Personel emalini doğru girmelisiniz!", Toast.LENGTH_SHORT).show();
                                else if (name.length() == 0)
                                    Toast.makeText(Authorized_Personel_Update.this, "Personel ad ve soyadı tamamen girmelisiniz!", Toast.LENGTH_SHORT).show();
                                else if (kurum.length() == 0)
                                    Toast.makeText(Authorized_Personel_Update.this, "Personel krumunu girmelisiniz!", Toast.LENGTH_SHORT).show();
                                else if (rol.length() == 0)
                                    Toast.makeText(Authorized_Personel_Update.this, "Personel rolünü girmelisiniz!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() { // the error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getStackTrace());

                        }
                    });
            queue1.add(request1);
        } catch (Exception e) {
            System.out.println("hata burda");
            if (email.length() == 0)
                Toast.makeText(Authorized_Personel_Update.this, "Personel emalini girmelisiniz!", Toast.LENGTH_SHORT).show();
            else if (!(email.getText().toString().contains("@")))
                Toast.makeText(Authorized_Personel_Update.this, "Personel emalini doğru girmelisiniz!", Toast.LENGTH_SHORT).show();
            else if (name.length() == 0)
                Toast.makeText(Authorized_Personel_Update.this, "Personel ad ve soyadı tamamen girmelisiniz!", Toast.LENGTH_SHORT).show();
            else if (kurum.length() == 0)
                Toast.makeText(Authorized_Personel_Update.this, "Personel krumunu girmelisiniz!", Toast.LENGTH_SHORT).show();
            else if (rol.length() == 0)
                Toast.makeText(Authorized_Personel_Update.this, "Personel rolünü girmelisiniz!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPersonel() {
        JSONObject obj = new JSONObject();

        //JSONObject personnelObject = new JSONObject();

        try {
            obj.put("personnelID", Authorized_Personeller.disasterID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());


                            String type = "";
                            try {
                                String cevap = response.getString("records");
                                cevap = cevap.substring(1, cevap.length() - 1);
                                JSONObject tmpJson = new JSONObject(cevap);
                                perInfo = new JSONObject(cevap);
                                emergencyins = perInfo.getString("institution");
                                disasterTeamid = perInfo.getString("teamID");
                                disasterrole = perInfo.getString("personnelRole");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        temp = false;
                        System.out.println(error);
                    }
                });
        queue5.add(jsonObjectRequest);

    }


    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(Authorized_Personel_Update.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Authorized_Personel_Update.this).removeLocationUpdates(this);
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


    // CIKIS
    public void ClickAuthorizedExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class);
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
        redirectActivity(this, Authorized_Assign_Team.class);
    }

}