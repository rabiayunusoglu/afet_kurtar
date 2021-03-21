package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Volunteer_ParticipateRequest2 extends AppCompatActivity {

    ArrayList<String> arrayListSubpart = new ArrayList<>();
    ArrayList<Integer> arrayListSubpartID = new ArrayList<Integer>();
    ArrayAdapter<String> adapterSubpart;
    Button submits;
    public static JSONObject volInfo;
    String urlSubpart = "https://afetkurtar.site/api/subpart/search.php";
    String url1 = "https://afetkurtar.site/api/volunteerUser/update.php";
    DrawerLayout drawerLayout;
    RequestQueue queuev,queue,queuecheck;
    static EditText afet;
    Spinner subpartSpinner;
    int index = 0;
    static int dataSupartID=0;
    static String responceStringSubpart = "", dataSupartName = "";
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queuev = Volley.newRequestQueue(this);
        queue = Volley.newRequestQueue(this);
        queuecheck = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_volunteer__participate_request2);
        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        subpartSpinner = (Spinner) findViewById(R.id.spinnersubpartnew);
        afet =(EditText) findViewById(R.id.editAfet);
        afet.setText(Volunteer_ParticipateRequest.afetBolgesi);
        afet.setFocusableInTouchMode(false);
        loadSpinnerDataSubpart(urlSubpart);
        try {
            checkUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submits = findViewById(R.id.gdrBtn);
        submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               try{
                   JSONObject obj = new JSONObject();
                   try {
                       obj.put("volunteerID",volInfo.get("volunteerID"));
                       obj.put("volunteerName", volInfo.get("volunteerName"));
                       obj.put("address", volInfo.get("address"));
                       obj.put("isExperienced",  volInfo.get("isExperienced"));
                       obj.put("haveFirstAidCert",  volInfo.get("haveFirstAidCert"));
                       obj.put("requestedSubpart",  dataSupartID);
                       obj.put("latitude",  volInfo.get("latitude"));
                       obj.put("longitude",  volInfo.get("longitude"));
                       obj.put("locationTime",  volInfo.get("locationTime"));
                       obj.put("tc", volInfo.get("tc"));
                       obj.put("tel", volInfo.get("tel"));
                       obj.put("birthDate", volInfo.get("birthDate"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   JsonObjectRequest request = new JsonObjectRequest(
                           Request.Method.POST, // the request method
                           url1, // the URL
                           obj, // the parameters for the php
                           new Response.Listener<JSONObject>() { // the response listener
                               @Override
                               public void onResponse(JSONObject response) {
                                   System.out.println(response.toString());
                               }
                           },
                           new Response.ErrorListener() { // the error listener
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   System.out.println(error);
                               }
                           });
                   queuev.add(request);
                   Toast.makeText(Volunteer_ParticipateRequest2.this, "Kaydınız Başarıyla Gerçekleşti :)", Toast.LENGTH_SHORT).show();
                   redirectActivity(Volunteer_ParticipateRequest2.this,Volunteer_Anasayfa.class);
               }catch (Exception e){
                   Toast.makeText(Volunteer_ParticipateRequest2.this, "Tekrar Deneyiniz...", Toast.LENGTH_SHORT).show();
                   redirectActivity(Volunteer_ParticipateRequest2.this,Volunteer_ParticipateRequest2.class);
               }
            }
        });


    }
    private void checkUser() throws JSONException {
        String url = "https://afetkurtar.site/api/volunteerUser/search.php";

        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("volunteerID", Integer.parseInt((String) MainActivity.userInfo.get("userID")));

        JsonObjectRequest  request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                new JSONObject(params), // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        //  System.out.println("response dönüyor");
                        System.out.println(response.toString());


                        String type = "";
                        try {
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            JSONObject tmpJson = new JSONObject(cevap);
                             volInfo= new JSONObject(cevap);
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


    private void loadSpinnerDataSubpart(String url) {
        JSONObject obj = new JSONObject();
        try {

            obj.put("isOpenForVolunteers", 1);
            obj.put("disasterName", afet.getText().toString());
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
                                if (responceStringSubpart.contains("},"))
                                    responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("}") + 2);
                                else {
                                    responceStringSubpart = responceStringSubpart.substring(responceStringSubpart.indexOf("}") + 1);
                                }
                                String value = "\"subpartName\":\"";
                                String valueID="\"subpartID\":\"";
                                String flag = "\"";
                                String result = "";
                                int resultID=0;
                                subpart = subpart.substring(subpart.indexOf(value) + value.length());
                                result = subpart.substring(0, subpart.indexOf(flag));
                                subpart = subpart.substring(subpart.indexOf("},") + 1);
                                arrayListSubpart.add(result);
                                subpartID = subpartID.substring(subpartID.indexOf(valueID) + valueID.length());
                                resultID = Integer.parseInt(subpartID.substring(0, subpartID.indexOf(flag)));
                                subpartID= subpartID.substring(subpartID.indexOf("},") + 1);
                                arrayListSubpartID.add(resultID);
                            }

                        }

                        adapterSubpart = new ArrayAdapter<String>(Volunteer_ParticipateRequest2.this, android.R.layout.simple_spinner_dropdown_item, arrayListSubpart);
                        subpartSpinner.setAdapter(adapterSubpart);
                        subpartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                dataSupartName = adapterSubpart.getItem(position).toString();
                                index = position;
                                dataSupartID=arrayListSubpartID.get(index);
                                Toast.makeText(getApplicationContext(), dataSupartName, Toast.LENGTH_SHORT).show();
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

    public void ClickRegisterInfo() {
        redirectActivity(this, Volunteer_RegisterInfo.class);
    }

    public void ClickAnasayfa(View view) {
        //redirect activity to dashboard
        redirectActivity(this, Volunteer_Anasayfa.class);
    }


    public void ClickParticipateRequest(View view) {
        //redirect activity to volunter
        redirectActivity(this, Volunteer_ParticipateRequest.class);
    }

    public void ClickAfetBölgesiGonder(View view) {
        //redirect activity to volunter
        redirectActivity(this, Volunteer_ParticipateRequest2.class);
    }

    public void ClickEmergency(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_Emergency.class);
    }

    public void ClickParticipateForm(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_RegisterInfo.class );
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