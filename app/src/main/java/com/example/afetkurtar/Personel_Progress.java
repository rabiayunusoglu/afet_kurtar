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

import java.util.HashMap;
import java.util.Map;

public class Personel_Progress extends AppCompatActivity {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * BU SAYFANIN GOSTERIM SARTI PERSONEL USER VE TAKIM KAPTANI OLMAK
         */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel__progress);

        findViewById(R.id.UpdateButton).setOnClickListener(this::onClick);

        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        queue = Volley.newRequestQueue(this);
    }
    JSONObject tmp;

    private void jsonRequest(String url,JSONObject params){
        //String url = "https://afetkurtar.site/api/users/search.php";
        //JSONObject tmp;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                params, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        tmp = response;
                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  addUser(account);
                    }
                });
        queue.add(request);

    }
    /*
    public void create(int subID, String status, int manpow, int equip) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedSubpartID", subID);
            obj.put("status", status);
            obj.put("needManPower",manpow);
            obj.put("needEquipment",equip);

        }catch (Exception e){
            System.out.println(e);
        }
        jsonRequest("https://afetkurtar.site/api/team/create.php",obj);
        String cevap = tmp.toString();


    }
     */

    public int getTeamID(){
        String id = account.getId();
        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", id);
        }catch (Exception e){
            System.out.println(e);
        }
        jsonRequest("https://afetkurtar.site/api/personnelUser/search.php",obj);
        String cevap = tmp.toString();
        String k = cevap.substring(cevap.indexOf(id));
        k = k.substring(k.indexOf(':')+2,k.indexOf(',')-1);
        return Integer.parseInt(k);
    }
    public void update(JSONObject obj){
        int id = getTeamID();
     // jsonRequest("https://afetkurtar.site/api/personnelUser/update.php",obj);
        /*
         *
         *  UPDATE KISMI DAHA PHP OLARAK EKLENMEDI
         *
         *
         */
    }

    public void HandleUpdate(){

        /*
         * 
         *
         */

        String progress = null;
        int equipment = 0, manPower = 0;
        boolean p = true,e = true,m = true;
        try {
            EditText prog = findViewById(R.id.updateProgress);
            progress = prog.getText().toString();
        }catch (Exception ex){
            p = false;
        }
        try {
            EditText equip = findViewById(R.id.updateEquipment);
            equipment = Integer.parseInt(equip.getText().toString());
        }catch (Exception ex){
            e = false;
        }
        try {
            EditText manPow = findViewById(R.id.updateManpower);
            manPower = Integer.parseInt(manPow.getText().toString());
        } catch (Exception ex){
            m = false;
        }

        if(p == true){
            System.out.println(progress);
        }
        if(e == true){
            System.out.println(equipment);
        }
        if(m == true){
            System.out.println(manPower);
        }


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UpdateButton:
                HandleUpdate();

                break;

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


    public void ClickAnasayfa(View view) {
        //redirect activity to dashboard
        redirectActivity(this, Volunteer_Anasayfa.class );
    }


    public void ClickParticipateRequest(View view) {
        //redirect activity to volunter
        redirectActivity(this, Volunteer_ParticipateRequest.class );
    }

    public void ClickEmergency(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_Emergency.class );
    }
    public void ClickParticipateForm(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_ParticipateForm.class );
    }
    public void ClickExit(View view) {
        //redirect activity to main screen
        signOut();
        redirectActivity(this, MainActivity.class );
    }
    public void ClickPersonel(View view) {
        //redirect activity to main screen
        redirectActivity(this, Personel_Progress.class );
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