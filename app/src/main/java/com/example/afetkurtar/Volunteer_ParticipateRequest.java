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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Volunteer_ParticipateRequest extends AppCompatActivity {
    ArrayList<String> arrayListAfet = new ArrayList<>();
    ArrayAdapter<String> adapterAfet;
    Button submit;
    String urlAfet = "https://afetkurtar.site/api/disasterEvents/search.php";
    String url1 = "https://afetkurtar.site/api/volunteerUser/update.php";
    DrawerLayout drawerLayout;
    RequestQueue queue;
    Spinner afetSpinner;
    int index = 0;
    static String responceStringAfet = "", afetBolgesi = "";
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_volunteer_participate_request);
        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        afetSpinner = (Spinner) findViewById(R.id.spinnerAfet);
        arrayListAfet.add("Seçilmedi");
        loadSpinnerDataAfet(urlAfet);


        submit = findViewById(R.id.gdrBtnAfet);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // setContentView(R.layout.activity_volunteer__participate_request2);
                if (afetBolgesi.equals("Seçilmedi")) {
                    Toast.makeText(getApplicationContext(), "Afet bölgesi seçiniz!", Toast.LENGTH_SHORT).show();
                } else
                    redirectActivity(Volunteer_ParticipateRequest.this, Volunteer_ParticipateRequest2.class);
            }
        });


    }

    private void loadSpinnerDataAfet(String url) {

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
                                String subpart = responceStringAfet.substring(responceStringAfet.indexOf("{"), responceStringAfet.indexOf("}") + 1);
                                if (responceStringAfet.contains("},"))
                                    responceStringAfet = responceStringAfet.substring(responceStringAfet.indexOf("}") + 2);
                                else {
                                    responceStringAfet = responceStringAfet.substring(responceStringAfet.indexOf("}") + 1);
                                }
                                String value = "\"disasterName\":\"";
                                String flag = "\"";
                                String result = "";
                                subpart = subpart.substring(subpart.indexOf(value) + value.length());
                                result = subpart.substring(0, subpart.indexOf(flag));
                                subpart = subpart.substring(subpart.indexOf("},") + 1);
                                arrayListAfet.add(result);
                            }

                        }

                        adapterAfet = new ArrayAdapter<String>(Volunteer_ParticipateRequest.this, android.R.layout.simple_spinner_dropdown_item, arrayListAfet);
                        afetSpinner.setAdapter(adapterAfet);
                        afetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                afetBolgesi = adapterAfet.getItem(position).toString();
                                if (afetBolgesi.equals("Seçilmedi")) {
                                    Toast.makeText(getApplicationContext(), "Afet bölgesi seçiniz!", Toast.LENGTH_SHORT).show();
                                } else {
                                    index = position;
                                    Toast.makeText(getApplicationContext(), afetBolgesi, Toast.LENGTH_SHORT).show();
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