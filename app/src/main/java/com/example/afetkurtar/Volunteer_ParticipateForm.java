package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;



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



public class Volunteer_ParticipateForm extends AppCompatActivity {
    RequestQueue queue;
    RadioGroup radioGroupExperience, radioGroupFirstAid;
    private RadioButton experience,firstAid;
    DrawerLayout drawerLayout;
    String url = "https://afetkurtar.site/api/volunteerUser/create.php";
    private EditText ad, soyad, adres;
    private Button btn_gonder;
    Double enlem, boylam;
    private static boolean answerExperienced = false, answerFirstAid = false;
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


        ad = (EditText) findViewById(R.id.participate_ad);
        soyad = (EditText) findViewById(R.id.participate_soyad);
        adres = (EditText) findViewById(R.id.participate_addres);

        radioGroupExperience=findViewById(R.id.groupexperienced);
        radioGroupFirstAid=findViewById(R.id.groupfistaid);

        btn_gonder = (Button) findViewById(R.id.participate_btn);
        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId=radioGroupExperience.getCheckedRadioButtonId();
                experience=findViewById(radioId);

                if(experience.getText().toString().equals("EVET")){
                    answerExperienced=true;
                }
                else{
                    answerExperienced=false;
                }
                int radioId1=radioGroupFirstAid.getCheckedRadioButtonId();
                firstAid=findViewById(radioId1);
                if(firstAid.getText().toString().equals("EVET")){
                    answerFirstAid=true;
                }
                else{
                    answerFirstAid=false;
                }
                JSONObject obj = new JSONObject();
                try {
                    obj.put("volunteerName", ad.getText().toString() + " " + soyad.getText().toString());
                    obj.put("address", adres.getText().toString());
                    obj.put("isExperienced", answerExperienced);
                    obj.put("haveFirstAidCert", answerFirstAid);
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
        redirectActivity(this, Personel_Progress.class );
    }
    public void ClickAfetBolgesi(View view) {
        //redirect activity to main screen
        redirectActivity(this, Afet_Bolgesi.class );
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