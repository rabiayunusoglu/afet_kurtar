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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Authorized_Notification extends AppCompatActivity {
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized__notification);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.auth_geri_button).setOnClickListener(this::onClick);


        list2 = new ArrayList<JSONObject>();
        getData("");
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        ((LinearLayout)findViewById(R.id.auth_lay_scroll)).removeAllViews();
        list2 = new ArrayList<JSONObject>();
        getData("");
    }
    public void getData(String ID) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/notice/read.php", null, new Response.Listener<JSONObject>() {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        for (JSONObject x : list2) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_personel_history_addlayout, null);
            LinearLayout scroll = findViewById(R.id.auth_lay_scroll);
            TextView linear = (TextView) view.findViewById(R.id.HistoryText); // personel_history ile ayni addlayout'u kullaniyor
            linear.setTextSize(20);


            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(x.getString("latitude")), Double.parseDouble(x.getString("longitude")), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String addres = "Koordinatlardan Adres Bilgisi Alınamadı";
                try {
                    addres = addresses.get(0).getAddressLine(0);
                }catch (Exception e){
                }
                linear.setText("ID: "+ x.getString("noticeID") + "\n"+"Tip : "+ x.getString("type") +"\n"+ "Adress : " + addres);
            } catch (Exception e) {
                e.printStackTrace();
            }
            linear.setOnClickListener(this::onClick);
            k++;
            scroll.addView(view);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HistoryText:

                TextView linear = (TextView) v.findViewById(R.id.HistoryText);
                String tmp = (String) linear.getText();
                tmp = tmp.substring(tmp.indexOf(" ")+1,tmp.indexOf("Tip")).trim();
               // tmp = tmp.substring(0,tmp.indexOf(" ")).trim(); // **************************************** MESAJI DEGISTIRIRSEN BURAYI AYARLA
               // System.out.println(tmp);

                Intent asd = new Intent(this, Notification_Details.class);
                JSONObject json = new JSONObject();
                for (JSONObject x : list2) {
                    try {
                        if(x.getString("noticeID").equals(tmp.trim())){
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