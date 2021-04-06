package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Personel_Anasayfa extends AppCompatActivity {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    RequestQueue queue;
    Bundle bundle;
    public static JSONObject PersonelInfo = null;
    static String Yetki = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_anasayfa);
        drawerLayout = findViewById(R.id.personel_drawer_layout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        queue = Volley.newRequestQueue(this);
        try {
            bundle = getIntent().getExtras();
        }
        catch (Exception e){
        }
        if(!(Yetki.length()>0))
        checkAuthority();


    }

    public void checkAuthority(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", MainActivity.userInfo.getString("userID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            JSONObject tmpJson = new JSONObject(cevap);
                           PersonelInfo = tmpJson;
                           Yetki = tmpJson.getString("personnelRole");
                            try {
                                if((bundle.getString("title")).equals("Yeni Mesaj")){
                                    messageRedirect();
                                }
                            }
                            catch (Exception e){

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
    public void messageRedirect(){
        redirectActivity(this,MessageActivity.class);
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
       Yetki = "kaptan";  //*************************************************************************** TEST AMACLI KALDIRILACAK
        if(Yetki.equalsIgnoreCase("kaptan"))
        redirectActivity(this, Personel_Progress.class);
        else
            Toast.makeText(getApplicationContext(), "Gerekli Yetkiye Sahip Değilsiniz", Toast.LENGTH_LONG).show();
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
        //redirectActivity(this, Authorized_Anasayfa.class );
    }


}