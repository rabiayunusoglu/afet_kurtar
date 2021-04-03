package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class Authorized_Assign_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Assign_Team";
    RequestQueue queue;
    JSONObject data = new JSONObject();
    Spinner spinner;
    ArrayAdapter adapter;
    ArrayList<String> teamStringList = new ArrayList<String>();
    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
    public static String teamID = "";
    public static String needManPower = "";
    public static String needEquipment = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_assign_team);
        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btn_assignTeamComplete).setOnClickListener(this::onClick);
        findViewById(R.id.btn_team_create).setOnClickListener(this::onClick);
        findViewById(R.id.btn_team_edit).setOnClickListener(this::onClick);


        spinner = (Spinner) findViewById(R.id.assignTeamSpinner);
        setTeamToSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString().equalsIgnoreCase("Takım Seçin")) {
                    //textAfetId.setText("seçilmedi");
                } else {

                    for (JSONObject x : jsonObjectList) {
                        try {
                            if (x.getString("teamID").equals(((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString())) {
                                teamID = (((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString());
                                calaculateSelectedTeamInformations();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                    //textAfetId.setText(disasterID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void calaculateSelectedTeamInformations(){
        for(int i = 0; i<teamStringList.size(); i++){
            if(teamStringList.get(i).equalsIgnoreCase(teamID)){
                try {
                    needManPower = jsonObjectList.get(i).getString("needManPower").toString();
                    needEquipment = jsonObjectList.get(i).getString("needEquipment").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_assignTeamComplete:

                break;
            case R.id.btn_team_create:
                openTeamCreatePage();
                break;
            case R.id.btn_team_edit:
                if(isTeamSelected())
                openTeamEditPage();
                break;
        }
    }
    public void openTeamEditPage(){
        Intent intent = new Intent(this, Authorized_Edit_Team.class);
        startActivity(intent);
    }
    public void openTeamCreatePage(){
        Intent intent = new Intent(this, Authorized_Create_Team.class);
        startActivity(intent);
    }

    public void setTeamToSpinner() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/team/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForTeamTable(response);
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

    public void handleResponseForTeamTable(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            //    System.out.println(response.toString());
            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);

            teamStringList.add("Takım Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    teamStringList.add(tmp.getString("teamID"));
                    jsonObjectList.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, teamStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    public boolean isTeamSelected(){
        String teamString = (((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString());
        if(!teamString.equalsIgnoreCase("Takım Seçin")){
            return true;
        }else{
            Toast.makeText(this, "Takım seçiniz", Toast.LENGTH_SHORT).show();
            return false;
        }

    }



}// class end