package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Authorized_Send_Notification extends AppCompatActivity {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized__send__notification);

        queue = Volley.newRequestQueue(this);





        findViewById(R.id.SendNotificationButton).setOnClickListener(this::onClick);


        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void sendButton(){
        /*
        NotificationSender send = new NotificationSender(getApplicationContext());
        try {
            System.out.println("SEND NOTIFICATION");
            send.sendNotificationWithData(
                    "Notification Title",
                    "Notification Body",
                    "Data Title",
                    "DataBody",
                    MainActivity.userInfo.getString("userToken"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        String title = ((EditText)findViewById(R.id.NotificationHeader)).getText().toString();
        String body = ((EditText)findViewById(R.id.NotificationBody)).getText().toString();

        NotificationSender send = new NotificationSender(getApplicationContext());
        if(title.length()>0 && body.length()>0) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "https://afetkurtar.site/api/users/read.php", null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String cevap;
                            ArrayList<JSONObject> list = new ArrayList<JSONObject>();
                            try {
                                cevap = response.getString("records");
                                cevap = cevap.substring(1, cevap.length() - 1);

                                while (cevap.indexOf(",{") > -1) {
                                    list.add(new JSONObject(cevap.substring(0, cevap.indexOf(",{"))));
                                    cevap = cevap.substring(cevap.indexOf(",{") + 1);
                                }
                                list.add(new JSONObject(cevap));

                                for (JSONObject x : list) {
                                    if (!x.getString("userType").equals("authorizedUser") && x.getString("userToken").length()>5) {
                                      //  System.out.println(x.getString("userID"));
                                        send.sendNotification(title,body,x.getString("userID"));
                                    }
                                }
                                showToast(true);
                                ((EditText)findViewById(R.id.NotificationHeader)).setText("");
                                ((EditText)findViewById(R.id.NotificationBody)).setText("");
                            } catch (Exception e) {

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
            showToast(false);
        }
    }
    public void showToast(boolean a){
        if(a){
            Toast.makeText(getApplicationContext(), "Tum Kullanicilara Bildirim Gönderildi", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Lütfen Bilgileri Doldurunuz", Toast.LENGTH_LONG).show();
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.SendNotificationButton:
                sendButton();
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


    // CIKIS
    public void ClickAuthorizedExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class );
    }

    public void ClickNotificationSend(View view) {
        //redirectActivity(this, Authorized_Send_Notification.class );
    }
    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }

}