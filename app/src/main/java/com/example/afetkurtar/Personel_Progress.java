package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


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


public class Personel_Progress extends AppCompatActivity  {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    RequestQueue queue;
    Spinner spinner;
    ArrayAdapter adapter;
    ArrayList<String> list2 = new ArrayList<String>();
    String TeamID = "0";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * BU SAYFANIN GOSTERIM SARTI PERSONEL USER VE TAKIM KAPTANI OLMAK
         */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel__progress);

        queue = Volley.newRequestQueue(this);

        findViewById(R.id.textHata).setVisibility(View.INVISIBLE);

        spinner = (Spinner) findViewById(R.id.listOfEquipment);
        setEquipmentToSpinner();

        findViewById(R.id.UpdateButton).setOnClickListener(this::onClick);
        findViewById(R.id.HistoryButton).setOnClickListener(this::onClick);

        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

        getTeamID();
    }

    JSONObject Equip;
    public void handleResponse(JSONObject a){
        ArrayList<String> list = new ArrayList<String>();
        try {
            //    System.out.println(response.toString());
            String cevap = a.getString("records");
            cevap = cevap.substring(1,cevap.length()-1);

            while(cevap.indexOf(",{") >-1){
                list.add(cevap.substring(0,cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{")+1);
            }
            list.add(cevap);
            for(String x:list){
                try {
                    JSONObject tmp = new JSONObject(x);
                    list2.add(tmp.getString("equipmentName"));
                }catch (Exception e){
                    // e.printStackTrace();
                }
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, list2);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setEquipmentToSpinner(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/equipment/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                          //  System.out.println(response.toString());
                            handleResponse(response);
                        }catch (Exception e){
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
    public void getTeamID(){
        String id = account.getId();
    //    id = "3";
        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", id);
        }catch (Exception e){
            System.out.println(e);
        }String cevap;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                "https://afetkurtar.site/api/personnelUser/search.php", // the URL
                obj, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            try {

                                String cevap = response.getString("records");
                                cevap = cevap.substring(1,cevap.length()-1);
                                JSONObject tmp = new JSONObject(cevap);
                                cevap = tmp.getString("personnelID");
                                TeamID = cevap;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public void update(JSONObject obj){

     // jsonRequest("https://afetkurtar.site/api/personnelUser/update.php",obj);
        /*
         *
         *  UPDATE KISMI DAHA PHP OLARAK EKLENMEDI
         *
         *
         */
    }

    public void HandleUpdate(){
        System.out.println(((Spinner)findViewById(R.id.listOfEquipment)).getSelectedItem().toString());
        System.out.println(TeamID);


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
            case R.id.HistoryButton:
                Intent intent = new Intent(this, Personel_Progress_History.class);
                intent.putExtra("Team_id", TeamID);
                startActivity(intent);

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
        redirectActivity(this, Volunteer_RegisterInfo.class );
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