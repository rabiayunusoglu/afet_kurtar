package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

public class Personel_Information extends AppCompatActivity {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel__information);

        drawerLayout = findViewById(R.id.personel_info_layout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        queue = Volley.newRequestQueue(this);

        ((TextView)findViewById(R.id.Team_my_role)).setText(Personel_Anasayfa.Yetki);
        try {
            ((TextView)findViewById(R.id.Team_info_no)).setText(Personel_Anasayfa.PersonelInfo.getString("teamID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setData();

    }


    public void setData(){
        JSONObject obj = new JSONObject();
        String id = "0";
        try {
            id = Personel_Anasayfa.PersonelInfo.getString("teamID");
            obj.put("teamID", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI
        if(!id.equals("0")) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "https://afetkurtar.site/api/team/search.php", obj, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //  System.out.println(response.toString());
                                String cevap = response.getString("records");
                                cevap = cevap.substring(1, cevap.length() - 1);
                                JSONObject tmpJson = new JSONObject(cevap);

                                ((TextView) findViewById(R.id.Team_info_subpartno)).setText(tmpJson.getString("assignedSubpartID"));
                                ((TextView) findViewById(R.id.personel_team_status)).setText(tmpJson.getString("status"));
                                getSubpart(tmpJson.getString("assignedSubpartID"));
                                getTeamNames();
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
        else{
            printError();
        }
    }
    public void printError(){
        Toast.makeText(getApplicationContext(), "Takım Bulunamadı", Toast.LENGTH_LONG).show();
    }
    public void getSubpart(String part){
        JSONObject obj = new JSONObject();
        try {
            obj.put("subpartID", part);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/subpart/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            try {
                                String cevap = response.getString("records");
                                cevap = cevap.substring(1, cevap.length() - 1);
                                JSONObject tmpJson = new JSONObject(cevap);


                                ((TextView) findViewById(R.id.personnel_address)).setText(tmpJson.getString("address"));
                                ((TextView) findViewById(R.id.Rescued_Person_info)).setText(tmpJson.getString("rescuedPerson"));
                                ((TextView) findViewById(R.id.Missing_Person_info)).setText(tmpJson.getString("missingPerson"));
                            }catch (Exception e){
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
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void getTeamNames(){
        JSONObject obj = new JSONObject();
        try {
                obj.put("teamID",Personel_Anasayfa.PersonelInfo.getString("teamID"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("KAFAYI YIYECEM ULAAAAAA");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            addtext(response);
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
    public void addtext(JSONObject obj) {
        String cevap = "";
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        try {
            //    System.out.println(response.toString());
            cevap = obj.getString("records");
            cevap = cevap.substring(1,cevap.length()-1);

            while(cevap.indexOf(",{") >-1){
                list.add(cevap.substring(0,cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{")+1);
            }
            list.add(cevap);
            for(int x = list.size()-1; x>=0;x--){
                try {
                    JSONObject tmp = new JSONObject(list.get(x));
                    list2.add(tmp.getString("personnelName"));
                }catch (Exception e){
                    // e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        for(String x:list2){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_personel_history_addlayout, null);
            LinearLayout scroll = findViewById(R.id.PersonnelNames);
            TextView linear = (TextView) view.findViewById(R.id.HistoryText);
            linear.setText(x);

            scroll.addView(view);
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

    public void ClickTeamManagement(View view) {

        if( Personel_Anasayfa.Yetki.equalsIgnoreCase("kaptan"))
           redirectActivity(this, Personel_Progress.class);
        else
            Toast.makeText(getApplicationContext(), "Gerekli Yetkiye Sahip Değilsiniz", Toast.LENGTH_LONG).show();
    }

    public void ClickPersonelNotification(View view) {
        //  redirectActivity(this, Authorized_Notification.class);
    }

    public void ClickPersonelInfo(View view) {
        // redirectActivity(this, Authorized_PersonelRegister.class);
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
        //redirectActivity(this, Authorized_Anasayfa.class );
    }
}