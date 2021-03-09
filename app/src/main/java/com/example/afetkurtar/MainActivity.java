package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.SignInButton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        String url = "https://afetkurtar.site/api/volunteerUser/read.php";

        // jsonArray icin RESP API ile cekme (Test Edilemedi SSL Bekleniyor)
        /*
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            TextView text = findViewById(R.id.textView2);
                            text.setText("Hello   " + response.getString(0));
                            text.setText(a.toString());

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        TextView text = findViewById(R.id.textView2);
                        text.setText("Hello ");
                    }
                }
        );
        */

        //Rest API ile get operasyonu (SSL Hatası veriyor, SSL bekleniyor)

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TextView text = findViewById(R.id.textView2);
                            text.setText("Hello   " + response.toString());
                            //    textView.setText("Response: " + response.toString());
                        }catch (Exception e){

                            TextView text = findViewById(R.id.textView2);
                            text.setText("HATA");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                        TextView text = findViewById(R.id.textView2);
                       // text.setText( "HATA");
                    }
                });
        queue.add(jsonObjectRequest);
        TextView text = findViewById(R.id.textView3);
        text.setText(" Deneme");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                /*
                Burada Sunucuya sorgu ile login olan kisinin hangi yetkiye sahip oldugunu bulup
                ona göre yonlendirme yapılacak.
                 */
               signIn();
               // signIn ile asagidaki update UI kısmında oluyor (startActivity)

              //  Intent intent=new Intent(this, Volunteer_Anasayfa.class);
              //  startActivity(intent);



                break;
           /* case R.id.button_sign_out:
                signOut();
                break;
            // ...*/
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
            updateUI(false,account);
        }
    }
    private void updateUI(boolean isLogin, GoogleSignInAccount account){
        if(isLogin){

            // Updake kisi yetkisine gore olacak

            /*
            Google Account information, AccountInformation Classı altında, Application extend edilerek global variable olarak atandı
            Asagidaki sekilde herhangi bir activity uzerinden cagirilabilir
            GoogleSignInAccount account = ((AccountInformation) this.getApplication()).getAccount();
             */
            ((AccountInformation) this.getApplication()).setAccount(account);

            Intent intentLogin = new Intent(MainActivity.this, Volunteer_Anasayfa.class);
            startActivity(intentLogin);
        }
        else
        {
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