package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Authorized_Team_Equipment_Requests extends AppCompatActivity {
    RequestQueue queue;
    String TeamID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized__team__equipment__requests);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.auth_geri_button).setOnClickListener(this::onClick);

        Bundle bundle = getIntent().getExtras();
        TeamID = bundle.getString("Team_id");

        list2 = new ArrayList<JSONObject>();
        getData(TeamID);
    }
    protected void onRestart() {
        super.onRestart();

        ((LinearLayout)findViewById(R.id.auth_equip_scroll)).removeAllViews();
        list2 = new ArrayList<JSONObject>();
        getData(TeamID);
    }
    public void getData(String ID) {
        JSONObject tmp = new JSONObject();

        try {
            tmp.put("teamRequestID",ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/equipmentRequest/search.php", tmp, new Response.Listener<JSONObject>() {

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

    static int k = 0;
    ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
    public void addtext(JSONObject obj) {
        String cevap = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            //    System.out.println(response.toString());
            cevap = obj.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            JSONObject tjson = new JSONObject(cevap);

            if (tjson.getString("teamRequestID").equals(TeamID)) {

                while (cevap.indexOf(",{") > -1) {
                    list.add(cevap.substring(0, cevap.indexOf(",{")));
                    cevap = cevap.substring(cevap.indexOf(",{") + 1);
                }
                list.add(cevap);
                for (int x = list.size() - 1; x >= 0; x--) {
                    try {
                        JSONObject tmp = new JSONObject(list.get(x));

                        list2.add(tmp);
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
                for (JSONObject x : list2) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.activity_personel_history_addlayout, null);
                    LinearLayout scroll = findViewById(R.id.auth_equip_scroll);
                    TextView linear = (TextView) view.findViewById(R.id.HistoryText); // personel_history ile ayni addlayout'u kullaniyor
                    linear.setTextSize(20);




                    try {

                        linear.setText("ID: "+ x.getString("equipmentRequestID") +"\n"+"Ekipman ID : "+ x.getString("equipmentID") +"\n"+"Adet : "+ x.getString("quantity"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    linear.setOnClickListener(this::onClick);
                    k++;
                    scroll.addView(view);
                }
            }
            else{
                printError();
            }
            } catch(Exception e){
                e.printStackTrace();
                printError();
            }





    }
    public void printError(){
        Toast.makeText(getApplicationContext(), "Ekipman İsteği Bulunamadı", Toast.LENGTH_LONG).show();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HistoryText:

                TextView linear = (TextView) v.findViewById(R.id.HistoryText);
                String tmp = (String) linear.getText();
                tmp = tmp.substring(tmp.indexOf(" ")+1,tmp.indexOf("Ekipman")).trim();
                // tmp = tmp.substring(0,tmp.indexOf(" ")).trim(); // **************************************** MESAJI DEGISTIRIRSEN BURAYI AYARLA
                // System.out.println(tmp);

                Intent asd = new Intent(this, Authorized_Team_Equipment_Request_Details.class);
                JSONObject json = new JSONObject();
                for (JSONObject x : list2) {
                    try {
                        if(x.getString("equipmentRequestID").equals(tmp.trim())){
                            json = x;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                asd.putExtra("json",json.toString());
                startActivity(asd);


                break;
            case R.id.auth_geri_button:
                finish();
                break;
        }

    }
}