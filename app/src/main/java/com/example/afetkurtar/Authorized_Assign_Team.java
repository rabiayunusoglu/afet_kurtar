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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Authorized_Assign_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Assign_Team";
    RequestQueue queue;
    JSONObject data = new JSONObject();
    //static yapılabilir belki, create team içinden erişmek için ama kontrol et düzenle
    Spinner teamSpinner;
    Spinner subpartSpinner;
    ArrayAdapter adapter;
    //
    ArrayList<String> subpartStringList = new ArrayList<String>();

    ArrayList<String> teamStringList = new ArrayList<String>();
    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

    private ArrayList<JSONObject> teamMemberPersonnelObjectList = new ArrayList<JSONObject>();
/*
    ArrayList<String> personnel_ID_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_Name_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_Email_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_Role_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_Latitude_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_Longitude_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_Institution_TeamMemberStringList = new ArrayList<String>();
    ArrayList<String> personnel_LocationTime_TeamMemberStringList = new ArrayList<String>();

*/

    public static String teamID = "";
    //public static String subpartID = "";
    public static String needManPower = "";
    public static String needEquipment = "";
    public static String status = "";


    public static String selectedSubpartID = "0";

    static int teamStringListSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_assign_team);
        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btn_assignTeamComplete).setOnClickListener(this::onClick);
        findViewById(R.id.btn_team_create).setOnClickListener(this::onClick);
        findViewById(R.id.btn_team_edit).setOnClickListener(this::onClick);
        findViewById(R.id.btn_team_delete).setOnClickListener(this::onClick);


        teamSpinner = (Spinner) findViewById(R.id.assignTeamSpinner);
        subpartSpinner = (Spinner) findViewById(R.id.assignSubpartSpinner);
        setTeamToSpinner();
        setSubpartToSpinner();

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        subpartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (((Spinner) findViewById(R.id.assignSubpartSpinner)).getSelectedItem().toString().equalsIgnoreCase("Subpart Seçin")) {
                    //textAfetId.setText("seçilmedi");
                } else {

                    for (JSONObject x : jsonObjectList) {
                        try {
                            if (x.getString("subpartID").equals(((Spinner) findViewById(R.id.assignSubpartSpinner)).getSelectedItem().toString())) {
                                selectedSubpartID = (((Spinner) findViewById(R.id.assignSubpartSpinner)).getSelectedItem().toString());
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

        //if user come to this page from subpart page than auto selected
        //bu methodu ekleme sorun çıkartıyor
        //setSubpartToSelectedSubpartID();

    }//onCreate end

    //setSubpartToSelectedSubpartID methodu sonradan silinecek
    public void setSubpartToSelectedSubpartID(){
        this.selectedSubpartID = "Personel Seçin";
        try{
            this.selectedSubpartID = Disaster_Details.selectedSubpartID;
        }catch (Exception e){
            e.getMessage();
        }
        ((Spinner) findViewById(R.id.assignSubpartSpinner)).setSelection(getIndexFromSpinner(subpartSpinner,selectedSubpartID));
        /*
        try{
            this.selectedSubpartID = "Personel Seçin";
            try{
                this.selectedSubpartID = Disaster_Details.selectedSubpartID;
            }catch (Exception e){
                e.getMessage();
            }
            //System.out.println("selectedSubpart ID is what :  " + subpartSpinner.getSelectedItem().toString());
            int spinnerCount = subpartSpinner.getCount();
            System.out.println("spinnerCount = " + spinnerCount);
            for(int i = 0; i<spinnerCount; i++){
                subpartSpinner.setSelection(i);
                System.out.println("out of if subpart selection =?????? " + subpartSpinner.getSelectedItem().toString());
                if(this.selectedSubpartID.equalsIgnoreCase(subpartSpinner.getSelectedItem().toString())){
                    System.out.println("in if subpart selection =?????? " + subpartSpinner.getSelectedItem().toString());
                    return;
                }
            }
            System.out.println("after for subpart selection =?????? " + subpartSpinner.getSelectedItem().toString());
        }catch (Exception e){
            e.getMessage();
        }
        */
    }


    //private method of your class
    private int getIndexFromSpinner(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }



    public void calaculateSelectedTeamInformations(){
        for(int i = 0; i<teamStringList.size(); i++){
            if(teamStringList.get(i).equalsIgnoreCase(teamID)){
                try {
                    needManPower = jsonObjectList.get(i).getString("needManPower").toString();
                    System.out.println("needmanpower value ? : " + needManPower);
                    needEquipment = jsonObjectList.get(i).getString("needEquipment").toString();
                    System.out.println("needEquipment value ? : " + needEquipment);
                    status = jsonObjectList.get(i).getString("status").toString();
                    System.out.println("status value ? : " + status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_assignTeamComplete:
                if(isTeamSelected() && isSubpartSelected()){
                    assignSelectedTeam();
                }

                break;
            case R.id.btn_team_create:
                openTeamCreatePage();
                break;
            case R.id.btn_team_edit:
                if(isTeamSelected() && isSubpartSelected()){
                    openTeamEditPage();
                }
                break;
            case R.id.btn_team_delete:
                if(isTeamSelected()){
                    deleteSelectedTeam();
                }
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
            teamSpinner.setAdapter(adapter);
            teamStringListSize = teamStringList.size();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    /////////////////////////////////
    public void setSubpartToSpinner() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/subpart/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForSubpartTable(response);
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

    public void handleResponseForSubpartTable(JSONObject a) {
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

            subpartStringList.add("Subpart Seçin");
            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    subpartStringList.add(tmp.getString("subpartID"));
                    jsonObjectList.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subpartStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subpartSpinner.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    /////////////////////////////////
    public boolean isTeamSelected(){
        String teamString = (((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString());
        if(!teamString.equalsIgnoreCase("Takım Seçin")){
            return true;
        }else{
            Toast.makeText(this, "Takım seçiniz", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public boolean isSubpartSelected(){
        String subpartString = (((Spinner) findViewById(R.id.assignSubpartSpinner)).getSelectedItem().toString());
        if(!subpartString.equalsIgnoreCase("Subpart Seçin")){
            return true;
        }else{
            Toast.makeText(this, "Subpart seçiniz", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void assignSelectedTeam(){
        if(!isTeamSelected()){
            return;
        }
        //else change assignedSubpartID
        updateTeamID();
    }
    public void updateTeamID(){
        JSONObject obj = new JSONObject();
        try {

            obj.put("teamID", teamID);
            obj.put("assignedSubpartID", selectedSubpartID);
            obj.put("status", status);
            obj.put("needManPower", needManPower);
            obj.put("needEquipment", needEquipment);

        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/team/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        Toast.makeText(this, "Takım Atandı.", Toast.LENGTH_SHORT).show();
        //finish();
    }
    public void deleteSelectedTeam(){
        if(!isTeamSelected()){
            return;
        }

        findUsersListsForSelectedTeamID(); //for xreate lists

        /*
        Dikkat burası silme
        işleminden sonra spinnerın resetlenmesi için düşündüm ancak istenildiği gibi çalışmıyor burayı sonra incele
        teamStringList.clear();
        setTeamToSpinner();

        */

    }
    public void resetPersonnelTeamMemberLists(){
        teamMemberPersonnelObjectList.clear();
    }
    public void findUsersListsForSelectedTeamID() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseFindUserListForSelectedTeamID(response);

                            updateTeamIDForPersonnelsInTeam(); // for update users in lists

                            removeTeamFromDB(); // remove team from DB

                            //after delete reset String list

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
    public void handleResponseFindUserListForSelectedTeamID(JSONObject a) {
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

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    if(tmp.getString("teamID").equals(teamID)){
                        teamMemberPersonnelObjectList.add(tmp);
                        /*
                        personnel_ID_TeamMemberStringList.add(tmp.getString("personnelID").trim());
                        personnel_Name_TeamMemberStringList.add(tmp.getString("personnelName").trim());
                        personnel_Email_TeamMemberStringList.add(tmp.getString("personnelEmail").trim());
                        personnel_Role_TeamMemberStringList.add(tmp.getString("personnelRole").trim());
                        personnel_Latitude_TeamMemberStringList.add(tmp.getString("latitude").trim());
                        personnel_Longitude_TeamMemberStringList.add(tmp.getString("longitude").trim());
                        personnel_Institution_TeamMemberStringList.add(tmp.getString("institution").trim());
                        personnel_LocationTime_TeamMemberStringList.add(tmp.getString("locationTime").trim());

                         */

                    }
                    //jsonObjectList.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    public void updateTeamIDForPersonnelsInTeam(){
        for(int i = 0 ; i<teamMemberPersonnelObjectList.size() ; i++){
            changeTeamIDFromPersonel(teamMemberPersonnelObjectList.get(i));
        }
    }
    public void changeTeamIDFromPersonel(JSONObject myObject){

        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", myObject.getString("personnelID"));
            obj.put("personnelName", myObject.getString("personnelName"));
            obj.put("personnelEmail", myObject.getString("personnelEmail"));
            obj.put("personnelRole", "Belirlenmedi");
            obj.put("teamID", "0"); // teamID set to 0 to clear assignment on team
            obj.put("latitude", myObject.getString("latitude"));
            obj.put("longitude", myObject.getString("longitude"));
            obj.put("institution", myObject.getString("institution"));
            obj.put("locationTime", myObject.getString("locationTime"));

        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        Toast.makeText(this, "Takım elemanlarının teamID leri güncellendi.", Toast.LENGTH_SHORT).show();
        //finish();

    }
    public void removeTeamFromDB(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID", teamID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/team/delete.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());
                            resetPersonnelTeamMemberLists();
                            //ReturnBack();
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
        Toast.makeText(this, "TakımID" + teamID + " silindi.", Toast.LENGTH_SHORT).show();
    }


}// class end