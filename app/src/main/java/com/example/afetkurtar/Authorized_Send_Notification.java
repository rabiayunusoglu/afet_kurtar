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

        findViewById(R.id.textHata).setVisibility(View.INVISIBLE);



        findViewById(R.id.SendNotificationButton).setOnClickListener(this::onClick);


        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void sendNotification(){
        /*
        {
            "to" : "faek0BAUT26VTjq62mBs_v:APA91bEU0thQI4tGZ7hlt3HDYelRIlWTGYXswjsHBXfV3VVn2TLQyiI1vvjWNNG0VetwaUcWyzOjpV5x4uqObT45JxhGN0xqX_mPiO79hYgwuX5dKNvw7Ylxm93e0c5Jf9sEz1krK5iC",
                "notification" : {
            "body" : "Body of Your Notification",
                    "title": "Title of Your Notification"
        },
            "data" : {
            "body" : "Body of Your Notification in Data",
                    "title": "Title of Your Notification in Title"

        }
        }
*/
        try {
             String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject idler = new JSONObject();

            idler.put("title", "Android Test Title");
            idler.put("body", "Android Test Body");

            JSONObject data = new JSONObject();

            data.put("title", "Android Test Title");
            data.put("body", "Android Test Body");

            JSONObject data2 = new JSONObject();

            data2.put("title", " DATA TITLE");
            data2.put("body", "DATA BODY");

            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("data", data2);
            // notification_data.put("to",MainActivity.userInfo.getString("userToken"));
            notification_data.put("to",MainActivity.userInfo.getString("userToken"));

        JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                String api_key_header_value = "key=AAAA5VKNhzk:APA91bGhZd1yaXmsMFvgpRQFREmNFd92pPnFhdMfQuWe3WBnppWSFtFxZK9QmgLOqVz26gK9U507kZl9M7OAExkjaWJiNLj-NPHtF7JHKNBNFFWndBwvYrx4dcBKVE_chcZroUlqF_Jn";
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", api_key_header_value);
                return headers;
            }
        };

        queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.SendNotificationButton:
                System.out.println("BILDIRIM GONDERILIYOR");
                sendNotification();
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
        // redirectActivity(this, Authorized_Notification.class);
    }
    //MESAJ
    public void ClickAuthorizedMessage(View view) {

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