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
import android.widget.TextView;
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


public class Personel_Progress extends AppCompatActivity  {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    RequestQueue queue;
    Spinner spinner;
    ArrayAdapter adapter;
    ArrayList<String> EquipmentName = new ArrayList<String>();  // Spinner icin equipment Listesi
    ArrayList<JSONObject> EquimentData = new ArrayList<JSONObject>(); //Equipment ID ye buradan eris
    JSONObject TeamInfo;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * BU SAYFANIN GOSTERIM SARTI PERSONEL USER VE TAKIM KAPTANI OLMAK
         */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel__progress);

        queue = Volley.newRequestQueue(this);
      //  findViewById(R.id.personel_current_status).setMovementMethod(new ScrollingMovementMethod());

        spinner = (Spinner) findViewById(R.id.listOfEquipment);
        setEquipmentToSpinner();

        findViewById(R.id.UpdateButton).setOnClickListener(this::onClick);
        findViewById(R.id.HistoryButton).setOnClickListener(this::onClick);

        drawerLayout = findViewById(R.id.personnel_screen_drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        getTeamID();
    }
    @Override
    public void onBackPressed() {
        redirectActivity(this,Personel_Anasayfa.class);
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
                    EquipmentName.add(tmp.getString("equipmentName"));
                    EquimentData.add(tmp);
                }catch (Exception e){
                    // e.printStackTrace();
                }
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, EquipmentName);
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
                ******************************** GET TEM ID YERINE PERSONEL ANASAYFADA PERSONEL BILGILERI PUBLIC OLARAK TUTULACAK( GUNCELLENECEK)
     */
    public void getTeamID(){
        /*
        String id = "";
    //    id = "3";
        JSONObject obj = new JSONObject();
        try {
            id = MainActivity.userInfo.getString("userID");
            obj.put("personnelID", id);
        }catch (Exception e){
            System.out.println(e);
        }

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
                                if(tmp.getString("personnelID").equals(MainActivity.userInfo.getString("userID"))) {
                                    cevap = tmp.getString("teamID");
                                    getTeamInfo(cevap);
                                }
                                else{
                                  printError();
                                }
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

         */
        try {

                getTeamInfo(Personel_Anasayfa.PersonelInfo.getString("teamID"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printError(){
        Toast.makeText(getApplicationContext(), "Bir Hata İle Karşılaşıldı Lütfen Tekrar Deneyin", Toast.LENGTH_LONG).show();
    }

    public void getTeamInfo(String id){

        JSONObject obj = new JSONObject();
        try {
           // obj.put("teamID", id);
            obj.put("teamID", id); // TEST ETMEK ICIN KULLANILABILIR
        }catch (Exception e){
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                "https://afetkurtar.site/api/team/search.php", // the URL
                obj, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1,cevap.length()-1);
                            TeamInfo = new JSONObject(cevap);
                            ((TextView)findViewById(R.id.personel_current_status)).setText(TeamInfo.getString("status"));
                            ((TextView)findViewById(R.id.personel_current_needManPower)).setText(TeamInfo.getString("needManPower"));

                            // ******************
                            getSubInfo(TeamInfo.getString("assignedSubpartID"));
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
    public void getSubInfo(String subID){
        JSONObject obj = new JSONObject();
        try {
            // obj.put("teamID", id);
            obj.put("subpartID", subID); // TEST AMACLI subID olarak degisecek *****************************************************
        }catch (Exception e){
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                "https://afetkurtar.site/api/subpart/search.php", // the URL
                obj, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1,cevap.length()-1);
                            JSONObject Subinfo = new JSONObject(cevap);
                            ((TextView)findViewById(R.id.Rescued_Person)).setText(Subinfo.getString("rescuedPerson"));
                            ((TextView)findViewById(R.id.Missing_Person)).setText(Subinfo.getString("missingPerson"));

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
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                "https://afetkurtar.site/api/team/update.php", // the URL
                obj, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            getTeamInfo(TeamInfo.getString("teamID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((EditText)findViewById(R.id.updateProgress)).setText(null);
                        ((EditText)findViewById(R.id.updateManpower)).setText(null);
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
    public void HandleUpdate(){
      //  System.out.println(((Spinner)findViewById(R.id.listOfEquipment)).getSelectedItem().toString());
        String selectedItem = "";
        boolean statusUpdated = false;
        String progress = (String) ((TextView)findViewById(R.id.personel_current_status)).getText();
        String progresstmp = "";
        int equipment = 0,manPowertmp = -1 ,manPower = Integer.parseInt((String)((TextView)findViewById(R.id.personel_current_needManPower)).getText());
        boolean p = true,e = true,m = true;
        try {
            EditText prog = findViewById(R.id.updateProgress);
            progresstmp = prog.getText().toString();
        }catch (Exception ex){
            p = false;
        }
        try {
            selectedItem = ((Spinner)findViewById(R.id.listOfEquipment)).getSelectedItem().toString();
            EditText equip = findViewById(R.id.updateEquipment);
            equipment = Integer.parseInt(equip.getText().toString());
        }catch (Exception ex){
            e = false;
        }
        try {
            EditText manPow = findViewById(R.id.updateManpower);
            manPowertmp = Integer.parseInt(manPow.getText().toString());
        } catch (Exception ex){
            m = false;
        }

        if(p == true && !progresstmp.equals("")){
          //  System.out.println(progress);
            statusUpdated = true;
        }

        if(m == true){
        //     System.out.println(manPower);
        }


        // Status ve Manpower icin update
        JSONObject updateStatAndManpow = new JSONObject();
        try {
            updateStatAndManpow.put("teamID",TeamInfo.getString("teamID"));
            if(statusUpdated){
                updateStatAndManpow.put("status",progresstmp);
            }
            else {
                updateStatAndManpow.put("status", progress);
            }
            if(manPowertmp > -1) {
                updateStatAndManpow.put("needManPower", manPowertmp);
            }else{
                updateStatAndManpow.put("needManPower", manPower);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        update(updateStatAndManpow);

        if(statusUpdated){
            addNewStatus(progresstmp);
        }

        if(e == true){
            if(equipment > 0 && selectedItem.length()>0){
                equipmentRequest(selectedItem,equipment);
            }
        }

    }
    public void addNewStatus(String status){
        JSONObject obj = new JSONObject();
        try {
            obj.put("statusMessage",status);
            obj.put("teamID",TeamInfo.getString("teamID"));
            obj.put("subpartID",TeamInfo.getString("assignedSubpartID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                "https://afetkurtar.site/api/status/create.php", // the URL
                obj, // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response){
                        System.out.println(response.toString());
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
    public void equipmentRequest(String SelectedItem,int quantitiy){
        int equipmentid = -1;
        for(JSONObject x: EquimentData){
            try{
                if(x.getString("equipmentName").equals(SelectedItem)){
                    equipmentid = Integer.parseInt(x.getString("equipmentID"));
                }
            }catch (Exception e){

            }
        }
        if(equipmentid > -1) {
            JSONObject obj = new JSONObject();
            try {
                // obj.put("statusMessage",status);
                obj.put("quantity", quantitiy);
                obj.put("equipmentID", equipmentid);
                obj.put("teamRequestID",TeamInfo.getString("teamID"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, // the request method
                    "https://afetkurtar.site/api/equipmentRequest/create.php", // the URL
                    obj, // the parameters for the php
                    new Response.Listener<JSONObject>() { // the response listener
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            ((EditText)findViewById(R.id.updateEquipment)).setText(null);
                        }
                    },
                    new Response.ErrorListener() { // the error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //  addUser(account);
                        }
                    });
            queue.add(request);
        }else{
            Toast.makeText(this, "HATA : Ekipman ID si Tabloda Bulunamadi", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UpdateButton:
                try{
                    TeamInfo.getString("teamID");
                    HandleUpdate();
                }catch (Exception e){
                    printError();
                }

                break;
            case R.id.HistoryButton:
                try {
                    Intent intent = new Intent(this, Personel_Progress_History.class);
                    intent.putExtra("Team_id", TeamInfo.getString("teamID"));
                    intent.putExtra("Sub_id", TeamInfo.getString("assignedSubpartID"));
                    startActivity(intent);
                }catch (Exception e){
                    printError();
                }
                break;

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


    public void ClickTeamManagement(View view) {

    }

    public void ClickPersonelNotification(View view) {
        //  redirectActivity(this, Authorized_Notification.class);
    }

    public void ClickPersonelInfo(View view) {
        redirectActivity(this, Personel_Information.class);
    }

    public void ClickPersonelArea(View view) {
        redirectActivity(this, Team_Member_Locations.class);
    }
    public void ClickPersonelMessage(View view) {

        redirectActivity(this, MessageActivity.class);
    }

    // CIKIS
    public void ClickPersonelExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class );
    }
    // ANA SAYFA
    public void ClickPersonelAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        redirectActivity(this, Personel_Anasayfa.class );
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