package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.SignInButton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;

    RequestQueue queue;
    public static int userID;
    public static JSONObject userInfo;
    Bundle bundle;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        queue = Volley.newRequestQueue(this);
        try {
            if (!lastSignedInAccount.getDisplayName().equals("")) {
                try {
                    bundle = getIntent().getExtras();
                } catch (Exception e) {
                }
                checkUser(lastSignedInAccount);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Giriş Yapınız", Toast.LENGTH_LONG).show();
        }


        System.out.println();
        System.out.println(FirebaseInstanceId.getInstance().getToken() + "**********************************");
        /*
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println(s + "*****************************************************************");
            }
        });
        
         */

        // Get request permission for location information
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }


    }


    private void checkUser(GoogleSignInAccount account) {
        String url = "https://afetkurtar.site/api/users/search.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", account.getEmail());

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
                            userInfo = new JSONObject(cevap);
                            userID=Integer.parseInt(userInfo.getString("userID"));
                            type = tmpJson.getString("userType");


                            /// burada bir timer ile user'ın location bilgileri güncellenicek

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                try {
                                    if(!userInfo.equals(null))
                                        updateLocationsInMinutes(300); // every 5 minute, update the location data to DB

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            if(tmpJson.getString("userToken").equals(FirebaseInstanceId.getInstance().getToken())){
                                // ****************************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI
                                //type = "authorizedUser";

                               // type = "personnelUser";

                                // ****************************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI
                                Intent intentLogin = null;
                                try {
                                    if ((bundle.getString("title")).equals("Yeni Mesaj") && type.equals("personnelUser")) {
                                        intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                                        intentLogin.putExtra("title",bundle.getString("title"));
                                    }
                                    else{
                                        if (type.equals("authorizedUser")) {
                                            intentLogin = new Intent(MainActivity.this, Authorized_Anasayfa.class);
                                        } else if (type.equals("personnelUser")) {
                                            intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                                        } else {
                                            intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
                                        }
                                    }
                                }catch (Exception e){
                                    if (type.equals("authorizedUser")) {
                                        intentLogin = new Intent(MainActivity.this, Authorized_Anasayfa.class);
                                    } else if (type.equals("personnelUser")) {
                                        intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                                    } else {
                                        intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
                                    }
                                }
                                startActivity(intentLogin);
                            }
                            else{
                                updateUser(tmpJson,account);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            printLoginError();
                        }

                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        addUser(account);
                    }
                });
        queue.add(request);
    }

    public void updateUser(JSONObject obj,GoogleSignInAccount account){

    JSONObject tmp = new JSONObject();
        try {
            tmp.put("userID", obj.getString("userID"));
            tmp.put("userType", obj.getString("userType"));
            tmp.put("userName", obj.getString("userName"));
            tmp.put("email", obj.getString("email"));
            tmp.put("createTime", obj.getString("createTime"));
            tmp.put("userToken", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (tmp.getString("userToken").length() > 10) {
                //   System.out.println(tmp.toString() + "**********************************************************");
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST, // the request method
                        "https://afetkurtar.site/api/users/update.php", // the URL
                        tmp, // the parameters for the php
                        new Response.Listener<JSONObject>() { // the response listener
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                        /*
                        String type = "";
                        try {
                            type = obj.getString("userType");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // *************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI (UPDATE DURUMU)
                        type = "personnelUser";
                        // **************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI (UPDATE DURUMU)
                        Intent intentLogin;
                        if (type.equals("authorizedUser")) {
                            intentLogin = new Intent(MainActivity.this, Authorized_Anasayfa.class);
                        } else if (type.equals("personnelUser")) {
                            intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                        } else {
                            intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
                        }
                        startActivity(intentLogin);
                        */
                                checkUser(account);
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
            } else {
                printLoginError();
            }
        }catch (Exception e){
            printLoginError();
        }
    }

    public void printLoginError(){
        Toast.makeText(getApplicationContext(), "Giriş Yapma Başarısız Oldu, Tekrar Deneyin", Toast.LENGTH_LONG).show();
    }

    public void addUser(GoogleSignInAccount account) {
        String url = "https://afetkurtar.site/api/users/create.php";

        Map<String, String> params = new HashMap<String, String>();
        params.put("userType", "volunteerUser");
        params.put("userName", account.getDisplayName());
        params.put("email", account.getEmail());
        params.put("userToken", FirebaseInstanceId.getInstance().getToken());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                new JSONObject(params), // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                        /*
                        System.out.println(response.toString());
                        String cevap = response.toString().substring(0, response.toString().lastIndexOf("\""));
                        cevap = cevap.substring(cevap.toString().lastIndexOf("\"") + 1);
                        userID = Integer.parseInt(cevap);
                        Intent intentLogin = new Intent(MainActivity.this, Volunteer_ParticipateForm.class);
                        startActivity(intentLogin);
                        */
                        checkUser(account);  // *********************************************************************** TEST EDILECEK
                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       printLoginError();

                    }
                });
        queue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:

                signIn();
                break;

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount account = null;
        try {
            account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(true, account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("asd", "signInResult:failed code=" + e.getStatusCode());
            updateUI(false, account);
        }
    }

    private void updateUI(boolean isLogin, GoogleSignInAccount account) {
        if (isLogin) {
            checkUser(account);
            //  if(userCreatedSuccessfully) {
            // Yetkiye gore yonlendirmeler burada yapilcak ********************
            //      Intent intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
            //      startActivity(intentLogin);
            //  }
        } else {
            Toast.makeText(this, "Giris Hatası", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateLocationsInMinutes(int checkInHowManySeconds) {
        //int minuteInMiliseconds = specifiedMinute*5*1000;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                if (!(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                    //controlmessage = true;
                    getCurrentLocation();

                }

            }
        };
        // 1(one) second 1000 milisecond, period time type is milisecond
        timer.schedule(timerTask, 0, 1000*checkInHowManySeconds);
    }
    private void checkUserIsVolunteerOrPersonnel(String locationString){
        try {
            if(userInfo.getString("userType").toString().trim().equalsIgnoreCase("volunteerUser")){
                findUserObjectInVolunteerTable(locationString);
            }else if(userInfo.getString("userType").toString().trim().equalsIgnoreCase("personnelUser")){
                findUserObjectInPersonnelTable(locationString);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateUserLocationInPersonnelUserTable(JSONObject personnelObject, String locationString) {

        String latitude = locationString.substring(0,locationString.indexOf(",")).trim();
        String longitude = locationString.substring(locationString.indexOf(",")+1).trim();
        System.out.println("from get location my location latitude personnel  : " + latitude);
        System.out.println("from get location my location longitude personnel : " + longitude);
        JSONObject obj = new JSONObject();
        try {

            obj.put("personnelID", personnelObject.getString("personnelID"));
            obj.put("personnelName", personnelObject.getString("personnelName"));
            obj.put("personnelEmail", personnelObject.getString("personnelEmail"));
            obj.put("personnelRole", personnelObject.getString("personnelRole"));
            obj.put("teamID", personnelObject.getString("teamID"));
            obj.put("latitude", String.valueOf(latitude));
            obj.put("longitude", String.valueOf(longitude));
            obj.put("institution", personnelObject.getString("institution"));
            obj.put("locationTime", personnelObject.getString("locationTime"));
            System.out.println("updateUserLocationInPersonnelUserTable obj.toString in personnel : " + obj.toString());

        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
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
        //Toast.makeText(this, "Personelin lokasyonu güncellendi.", Toast.LENGTH_SHORT).show();
    }

    private void findUserObjectInPersonnelTable(String locationString) {
        JSONObject obj = new JSONObject();
        //JSONObject personnelObject = new JSONObject();
        try {
            obj.put("personnelID", userInfo.getString("userID").toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject personnelObject = handlePersonnelObject(response);
                            updateUserLocationInPersonnelUserTable(personnelObject, locationString);

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

    private JSONObject handlePersonnelObject(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        try {

            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);

            for (String x : list) {
                try {
                    return new JSONObject(x);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }// handle response end

    private void updateUserLocationInVolunteerUserTable(JSONObject volunteerObject, String locationString) {

        String latitude = locationString.substring(0,locationString.indexOf(",")).trim();
        String longitude = locationString.substring(locationString.indexOf(",")+1).trim();
        System.out.println("from get location my location latitude personnel  : " + latitude);
        System.out.println("from get location my location longitude personnel : " + longitude);

        JSONObject obj = new JSONObject();
        try {

            obj.put("volunteerID", volunteerObject.getString("volunteerID"));
            obj.put("volunteerName", volunteerObject.getString("volunteerName"));
            obj.put("address", volunteerObject.getString("address"));
            obj.put("isExperienced", volunteerObject.getString("isExperienced"));
            obj.put("haveFirstAidCert", volunteerObject.getString("haveFirstAidCert"));
            obj.put("requestedSubpart", volunteerObject.getString("requestedSubpart"));
            obj.put("responseSubpart", volunteerObject.getString("responseSubpart"));
            obj.put("assignedTeamID", volunteerObject.getString("assignedTeamID"));
            obj.put("role", volunteerObject.getString("role"));
            obj.put("latitude", String.valueOf(latitude));
            obj.put("longitude", String.valueOf(longitude));
            obj.put("locationTime", volunteerObject.getString("locationTime"));
            obj.put("tc", volunteerObject.getString("tc"));
            obj.put("tel", volunteerObject.getString("tel"));
            obj.put("birthDate", volunteerObject.getString("birthDate"));
            System.out.println("updateUserLocationInVolunteerUserTable obj.toString in volunteer : " + obj.toString());
        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/update.php", obj, new Response.Listener<JSONObject>() {
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
        //Toast.makeText(this, "Personelin lokasyonu güncellendi.", Toast.LENGTH_SHORT).show();

    }

    private void findUserObjectInVolunteerTable(String locationString) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("volunteerID", userInfo.getString("userID").toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject personnelObject = handleVolunteerObject(response);
                            updateUserLocationInVolunteerUserTable(personnelObject, locationString);

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

    private JSONObject handleVolunteerObject(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        try {

            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);

            for (String x : list) {
                try {
                    return new JSONObject(x);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }// handle response end


    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("PERMISSON DENIEDDDDDDDD");
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestlocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                    //System.out.println("from get location my location latitude : " + latitude);
                    double longitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                    //System.out.println("from get location my location longitude: " + longitude);
                    //locationTime = locationResult.getLastLocation().getTime();

                    checkUserIsVolunteerOrPersonnel(String.valueOf(latitude) + "," + String.valueOf(longitude));
                }

            }
        }, Looper.getMainLooper());

    }

}//class end