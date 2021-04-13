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
    String SubPartID;
    RequestQueue queue;
    boolean TeamOrSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel__progress__history);
        Button but = findViewById(R.id.Refresh_History_Button);
        //  lin = findViewById(R.id.LinearLay);
        but.setOnClickListener(this::onClick);
        findViewById(R.id.history_geri_button).setOnClickListener(this::onClick);
        queue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        TeamID = bundle.getString("Team_id");
        SubPartID = bundle.getString("Sub_id");
      //  TeamID = "1";   // ******************************************************************* TEST AMACLI / SILINECEK
      //  SubPartID = "2"; // ******************************************************************* TEST AMACLI / SILINECEK
        TeamOrSub = true;
        getData(TeamID, TeamOrSub);

    }
    public void getData(String ID,boolean bool){
        JSONObject obj = new JSONObject();
        try {
            if(bool)
            obj.put("teamID",ID); //DEGISECEK
            else{
                obj.put("subpartID",SubPartID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/status/search.php", obj, new Response.Listener<JSONObject>() {

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

                    list2.add("Tarih   : "+tmp.getString("statusTime") + "\nDurum : "+tmp.getString("statusMessage"));
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
            linear.setText(x);
            linear.setOnClickListener(this::onClick);
            k++;
            scroll.addView(view);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Refresh_History_Button:

                if(TeamOrSub){
                    TeamOrSub = false;
                    ((TextView)findViewById(R.id.History_change_view)).setText("Afet Durum Geçmişi");
                    ((LinearLayout)findViewById(R.id.layoutScroll)).removeAllViews();
                    getData(SubPartID,TeamOrSub);
                }else{
                    TeamOrSub = true;
                    ((TextView)findViewById(R.id.History_change_view)).setText("Takım Durum Geçmişi");
                    ((LinearLayout)findViewById(R.id.layoutScroll)).removeAllViews();
                    getData(TeamID,TeamOrSub);
                }

                break;

            case R.id.history_geri_button:
                finish();
                break;

        }

    }
}