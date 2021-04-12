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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Authorized_VolunteerRequest2 extends AppCompatActivity {
    ArrayList<String> arrayListSubpart = new ArrayList<>();
    ArrayList<Integer> arrayListSubpartID = new ArrayList<Integer>();
    ArrayAdapter<String> adapterSubpart;
    Button submits;
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
    public void ClickAuthorizedEkipman(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Assign_Team.class );
    }
}