package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.SignInButton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    RequestQueue queue;
    public static int userID;
    public static JSONObject userInfo;
    Bundle bundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInAccount lastSignedInAccount= GoogleSignIn.getLastSignedInAccount(this);
        queue = Volley.newRequestQueue(this);
        try{
            if(!lastSignedInAccount.getDisplayName().equals("")){
                try {
                    bundle = getIntent().getExtras();
                }catch (Exception e){
                }
                checkUser(lastSignedInAccount);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Giriş Yapınız", Toast.LENGTH_LONG).show();
        }


        System.out.println();
        System.out.println( FirebaseInstanceId.getInstance().getToken() + "**********************************");
        /*
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println(s + "*****************************************************************");
            }
        });
        
         */
    }

    private void checkUser(GoogleSignInAccount account) {
        String url = "https://afetkurtar.site/api/users/search.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", account.getEmail());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                new JSONObject(params), // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                        //  System.out.println("response dönüyor");
                        System.out.println(response.toString());

                        JSONObject tmpJson = null;
                        String type = "";
                        try {
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            tmpJson = new JSONObject(cevap);
                            userInfo = new JSONObject(cevap);
                            userID=Integer.parseInt(userInfo.getString("userID"));
                            type = tmpJson.getString("userType");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            if(tmpJson.getString("userToken").equals(FirebaseInstanceId.getInstance().getToken())){
                                // ****************************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI
                                type = "authorizedUser";
                                // ****************************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI
                                Intent intentLogin = null;
                                try {
                                    if ((bundle.getString("title")).equals("Yeni Mesaj")) {
                                        intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                                        intentLogin.putExtra("title",bundle.getString("title"));
                                    }
                                    else{
                                        if (type.equals("authorizedUser")) {
                                            intentLogin = new Intent(MainActivity.this, Authorized_Anasayfa.class);
                                        } else if (type.equals("personnelUser")) {
                                            intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                                        } else {
                                            intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
                                        }
                                    }
                                }catch (Exception e){
                                    if (type.equals("authorizedUser")) {
                                        intentLogin = new Intent(MainActivity.this, Authorized_Anasayfa.class);
                                    } else if (type.equals("personnelUser")) {
                                        intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                                    } else {
                                        intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
                                    }
                                }
                                startActivity(intentLogin);
                            }
                            else{
                                updateUser(tmpJson,account);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            printLoginError();
                        }

                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        addUser(account);
                    }
                });
        queue.add(request);
    }

    public void updateUser(JSONObject obj,GoogleSignInAccount account){

    JSONObject tmp = new JSONObject();
        try {
            tmp.put("userID", obj.getString("userID"));
            tmp.put("userType", obj.getString("userType"));
            tmp.put("userName", obj.getString("userName"));
            tmp.put("email", obj.getString("email"));
            tmp.put("createTime", obj.getString("createTime"));
            tmp.put("userToken", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (tmp.getString("userToken").length() > 10) {
                //   System.out.println(tmp.toString() + "**********************************************************");
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST, // the request method
                        "https://afetkurtar.site/api/users/update.php", // the URL
                        tmp, // the parameters for the php
                        new Response.Listener<JSONObject>() { // the response listener
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                        /*
                        String type = "";
                        try {
                            type = obj.getString("userType");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // *************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI (UPDATE DURUMU)
                        type = "personnelUser";
                        // **************************************************************************** TEST ICIN USER TYPE AYARLAMA YERI (UPDATE DURUMU)
                        Intent intentLogin;
                        if (type.equals("authorizedUser")) {
                            intentLogin = new Intent(MainActivity.this, Authorized_Anasayfa.class);
                        } else if (type.equals("personnelUser")) {
                            intentLogin = new Intent(MainActivity.this, Personel_Anasayfa.class);
                        } else {
                            intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
                        }
                        startActivity(intentLogin);
                        */
                                checkUser(account);
                            }
                        },
                        new Response.ErrorListener() { // the error listener
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.getMessage());

                                error.printStackTrace();

                            }
                        });
                queue.add(request);
            } else {
                printLoginError();
            }
        }catch (Exception e){
            printLoginError();
        }
    }
    public void printLoginError(){
        Toast.makeText(getApplicationContext(), "Giriş Yapma Başarısız Oldu, Tekrar Deneyin", Toast.LENGTH_LONG).show();
    }

    public void addUser(GoogleSignInAccount account) {
        String url = "https://afetkurtar.site/api/users/create.php";

        Map<String, String> params = new HashMap<String, String>();
        params.put("userType", "volunteerUser");
        params.put("userName", account.getDisplayName());
        params.put("email", account.getEmail());
        params.put("userToken", FirebaseInstanceId.getInstance().getToken());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, // the request method
                url, // the URL
                new JSONObject(params), // the parameters for the php
                new Response.Listener<JSONObject>() { // the response listener
                    @Override
                    public void onResponse(JSONObject response) {
                        /*
                        System.out.println(response.toString());
                        String cevap = response.toString().substring(0, response.toString().lastIndexOf("\""));
                        cevap = cevap.substring(cevap.toString().lastIndexOf("\"") + 1);
                        userID = Integer.parseInt(cevap);
                        Intent intentLogin = new Intent(MainActivity.this, Volunteer_ParticipateForm.class);
                        startActivity(intentLogin);
                        */
                        checkUser(account);  // *********************************************************************** TEST EDILECEK
                    }
                },
                new Response.ErrorListener() { // the error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       printLoginError();

                    }
                });
        queue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:

                signIn();
                break;

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount account = null;
        try {
            account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(true, account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("asd", "signInResult:failed code=" + e.getStatusCode());
            updateUI(false, account);
        }
    }

    private void updateUI(boolean isLogin, GoogleSignInAccount account) {
        if (isLogin) {
            checkUser(account);
            //  if(userCreatedSuccessfully) {
            // Yetkiye gore yonlendirmeler burada yapilcak ********************
            //      Intent intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
            //      startActivity(intentLogin);
            //  }
        } else {
            Toast.makeText(this, "Giris Hatası", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}