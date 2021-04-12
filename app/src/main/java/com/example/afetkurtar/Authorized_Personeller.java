package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.List;
import java.util.Locale;

public class Authorized_Personeller extends AppCompatActivity {
    RequestQueue queue,queue5;
    GoogleSignInClient mGoogleSignInClient;
    public static int disasterID;
    public static String disasterName, time, emergencyins, disasterTeamid, disasterrole,
            disasteremail;

    public static Double latitude, longtitude;
    static int k = 0;
    JSONObject perInfo;
    ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized__personeller);
        drawerLayout = findViewById(R.id.per);
        queue = Volley.newRequestQueue(this);
        queue5 = Volley.newRequestQueue(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        list2 = new ArrayList<JSONObject>();
        getData("");
    }

    protected void onRestart() {
        super.onRestart();

        ((LinearLayout) findViewById(R.id.auth_lay_perScroll)).removeAllViews();
        list2 = new ArrayList<JSONObject>();
        getData("");
    }

    public void getData(String ID) {
        JSONObject obj = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/users/read.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            addtext(response);
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

    public void addtext(JSONObject obj) {
        String cevap = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            //    System.out.println(response.toString());
            cevap = obj.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);
            for (int x = list.size() - 1; x >= 0; x--) {
                try {
                    JSONObject tmp = new JSONObject(list.get(x));
                    if (tmp.getString("userType").equals("personnelUser"))
                        list2.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        for (JSONObject x : list2) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_personel_history_addlayout, null);
            LinearLayout scroll = findViewById(R.id.auth_lay_perScroll);
            TextView linear = (TextView) view.findViewById(R.id.HistoryText); // personel_history ile ayni addlayout'u kullaniyor
            linear.setTextSize(20);


            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(x.getString("latitude")), Double.parseDouble(x.getString("longitude")), 1);
                latitude = Double.parseDouble(x.getString("latitude"));
                longtitude = Double.parseDouble(x.getString("longitude"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String addres = "Koordinatlardan Adres Bilgisi Alınamadı";
                try {
                    addres = addresses.get(0).getAddressLine(0);
                } catch (Exception e) {
                }
                linear.setText("ID: " + x.getString("userID") + "\n" + "İsim : " + x.getString("userName") + "\n" + "Mail : " + x.getString("email"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            linear.setOnClickListener(this::onClick);
            k++;
            scroll.addView(view);
        }

    }

    public void ClickPerCreate(View view) {
        redirectActivity(this, Authorized_PersonelRegister.class);
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
                        System.out.println(error);
                    }
                });
        queue5.add(jsonObjectRequest);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HistoryText:

                TextView linear = (TextView) v.findViewById(R.id.HistoryText);
                String tmp = (String) linear.getText();
                tmp = tmp.substring(tmp.indexOf(" ") + 1, tmp.indexOf("İsim")).trim();
                // tmp = tmp.substring(0,tmp.indexOf(" ")).trim(); // **************************************** MESAJI DEGISTIRIRSEN BURAYI AYARLA
                // System.out.println(tmp);

                Intent asd = new Intent(this, Authorized_Personel_Update.class);
                JSONObject json = new JSONObject();
                for (JSONObject x : list2) {
                    try {
                        if (x.getString("userID").equals(tmp.trim())) {
                            disasterID = Integer.parseInt(x.getString("userID"));
                            disasteremail = x.getString("email");
                            disasterName = x.getString("userName");
                            getPersonel();
                            json = x;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                asd.putExtra("json", json.toString());
                startActivity(asd);


                break;

        }

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
        System.out.println("logo");
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


    /*
     *************************************** ASAGIDAKI KISIMLAR YONLENDIRMELERI AYARLAR
     */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Cikis basarili");
                    }
                });
    }

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
        redirectActivity(this, MainActivity.class);
    }

    public void ClickNotificationSend(View view) {
        redirectActivity(this, Authorized_Send_Notification.class);
    }

    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        redirectActivity(this, Authorized_Anasayfa.class);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }

    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Personeller.class);
    }

    public void ClickAuthorizedEkipman(View view) {
        redirectActivity(this, Authorized_Anasayfa.class);
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Anasayfa.class);
    }

}