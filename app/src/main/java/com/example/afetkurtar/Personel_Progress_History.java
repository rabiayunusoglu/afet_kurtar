package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

public class Personel_Progress_History extends AppCompatActivity {
    String TeamID;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel__progress__history);
        Button but = findViewById(R.id.gonderbutton);
        //  lin = findViewById(R.id.LinearLay);
        but.setOnClickListener(this::onClick);
        findViewById(R.id.history_geri_button).setOnClickListener(this::onClick);
        queue = Volley.newRequestQueue(this);

        TeamID = getIntent().getStringExtra("Team_id");
        getData(TeamID);

    }
    public void getData(String ID){
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID",ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", obj, new Response.Listener<JSONObject>() {

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

    static int k = 0;
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

                    list2.add(tmp.getString("institution")); // Degisecek
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
            LinearLayout scroll = findViewById(R.id.layoutScroll);
            TextView linear = (TextView) view.findViewById(R.id.HistoryText);
            linear.setText(x +" " + k);
            linear.setOnClickListener(this::onClick);
            k++;
            scroll.addView(view);
        }

      //  LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  View view = inflater.inflate(R.layout.activity_personel_history_addlayout, null);

        //  LinearLayout scroll = findViewById(R.id.layoutScroll);

      //  TextView linear = (TextView) view.findViewById(R.id.HistoryText);
      //  linear.setText("Bu :" + k);
      //  linear.setOnClickListener(this::onClick);

      //  k++;

       // scroll.addView(view);
        //  LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
        //          LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //  TextView tv=new TextView(this);
        //  tv.setLayoutParams(lparams);
        //  tv.setText(edtxt.getText());
        //  this.lin.addView(tv);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HistoryText:
                /*
                Burada Sunucuya sorgu ile login olan kisinin hangi yetkiye sahip oldugunu bulup
                ona göre yonlendirme yapılacak.
                 */
                TextView linear = (TextView) v.findViewById(R.id.HistoryText);
                System.out.println(linear.getText());
                // signIn ile asagidaki update UI kısmında oluyor (startActivity)

                break;

            case R.id.history_geri_button:
                Intent intent = new Intent(this, Personel_Progress.class);
                startActivity(intent);
                break;
            case R.id.gonderbutton:
               // addtext();
                break;
        }

    }
}