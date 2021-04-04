package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONObject;

import java.util.ArrayList;

public class Authorized_Edit_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Edit_Team";
    RequestQueue queue;
    JSONObject data = new JSONObject();
    Spinner availableMembersSpinner;
    Spinner teamMembersSpinner;
    ArrayAdapter adapter;
    ArrayList<String> personnelAvailableStringList = new ArrayList<String>();
    ArrayList<String> personnelTeamMemberStringList = new ArrayList<String>();
    JSONObject selectedAvailablePersonnelUser = new JSONObject();
    JSONObject selectedTeamMemberPersonnelUser = new JSONObject();

    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
    private static String personnelID;
    private String selectedSubpartID = "";
    private String isSelectedSubpartIsOpenForVolunteers = "";
    private String selectedTeamID = "";
    private String ourManPower = "";
    private String ourEquipment = "";

    //ArrayList<JSONObject> mySubparts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_edit_team);
        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btn_editTeam_addPersonnel).setOnClickListener(this::onClick);
        findViewById(R.id.btn_editTeam_removePersonnel).setOnClickListener(this::onClick);


        availableMembersSpinner = (Spinner) findViewById(R.id.edit_team_available_personnelSpinner);
        teamMembersSpinner = (Spinner) findViewById(R.id.edit_team_personnelMembersSpinner);

        try{
            this.selectedSubpartID = Authorized_Assign_Team.selectedSubpartID;
            this.isSelectedSubpartIsOpenForVolunteers = Disaster_Details.isOpenForVolunteer;
            this.selectedTeamID = Authorized_Assign_Team.teamID;
            this.ourManPower = Authorized_Assign_Team.needManPower;
            this.ourEquipment = Authorized_Assign_Team.needEquipment;
        }catch (Exception e){
            e.getMessage();
        }

        initTextViews();

        setPersonnelAvailableToSpinner();
        availableMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                   // System.out.println("Test Authorized Team else");
                    //System.out.println(jsonObjectList.toString());

                    for (JSONObject x : jsonObjectList) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString();
                            personnelIDPart = findPersonnelID(personnelIDPart);
                            //System.out.println("Personnel id " + personnelIDPart);
                            if (x.getString("personnelID").trim().equals(personnelIDPart.trim())) {
                                personnelID = (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailablePersonnelUser = x;
                                return;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });

        setTeamMemberPersonnelToSpinner();
        teamMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    //System.out.println("Test Authorized Team else");
                    System.out.println(jsonObjectList.toString());

                    for (JSONObject x : jsonObjectList) {
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString();
                            personnelIDPart = findPersonnelID(personnelIDPart);
                            //System.out.println("Personnel id " + personnelIDPart);
                            if (x.getString("personnelID").equals(personnelIDPart)) {
                                personnelID = (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString());
                                selectedTeamMemberPersonnelUser = x;
                                return;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });
    }
    public void initTextViews(){
        ((TextView)(findViewById(R.id.tv_team_manpower))).setText(ourManPower);
        ((TextView)(findViewById(R.id.tv_team_equipment))).setText(ourEquipment);
        ((TextView)(findViewById(R.id.tv_team_subpartID))).setText(selectedSubpartID);
        ((TextView)(findViewById(R.id.tv_selected_teamID))).setText(selectedTeamID);

    }

    public String findPersonnelID(String line){
         if(line.indexOf("PersonnelID:") != -1){
             line = line.substring(12,line.indexOf(","));
             line.trim();
             System.out.println("Test Line : " + line);
             return line;
         }
         return null;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editTeam_addPersonnel:
                addPersonnelToTeam();

                break;
            case R.id.btn_editTeam_removePersonnel:
                removePersonnelFromTeam();


                break;
        }
    }

    public void setPersonnelAvailableToSpinner() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForPersonnelAvailableTable(response);
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

    public void handleResponseForPersonnelAvailableTable(JSONObject a) {
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

            personnelAvailableStringList.add("Personel Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    if(tmp.getString("teamID").equals("0")){
                        personnelAvailableStringList.add("PersonnelID: " + tmp.getString("personnelID") +", Personnel İsmi: "+ tmp.getString("personnelName"));
                    }
                    jsonObjectList.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, personnelAvailableStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //((Spinner)findViewById(R.id.edit_team_available_personnelSpinner));
            //availableMembersSpinner.removeAllViews();
            availableMembersSpinner.setAdapter(adapter);
            personnelAvailableStringList = new ArrayList<String>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setTeamMemberPersonnelToSpinner() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForTeamMemberPersonnelTable(response);
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
    public void handleResponseForTeamMemberPersonnelTable(JSONObject a) {
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

            personnelTeamMemberStringList.add("Personel Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    if(tmp.getString("teamID").equals(selectedTeamID)){
                        personnelTeamMemberStringList.add("PersonnelID: " + tmp.getString("personnelID") +", Personnel İsmi: "+ tmp.getString("personnelName"));
                    }
                    jsonObjectList.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, personnelTeamMemberStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //((Spinner)findViewById(R.id.edit_team_personnelMembersSpinner)).removeAllViews();
            //teamMembersSpinner.removeAllViews();
            teamMembersSpinner.setAdapter(adapter);
            personnelTeamMemberStringList = new ArrayList<String>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end

    public void addPersonnelToTeam(){
        //JSONObject myObject = selectedAvailablePersonnelUser;
        JSONObject obj = new JSONObject();
        try {
            System.out.println("before putting " + obj.toString());
            obj.put("personnelID", selectedAvailablePersonnelUser.getString("personnelID"));
            obj.put("personnelName", selectedAvailablePersonnelUser.getString("personnelName"));
            obj.put("personnelEmail", selectedAvailablePersonnelUser.getString("personnelEmail"));
            obj.put("personnelRole", "Normal");
            obj.put("teamID", selectedTeamID); // teamID set to 0 to clear assignment on team
            obj.put("latitude", selectedAvailablePersonnelUser.getString("latitude"));
            obj.put("longitude", selectedAvailablePersonnelUser.getString("longitude"));
            obj.put("institution", selectedAvailablePersonnelUser.getString("institution"));
            obj.put("locationTime", selectedAvailablePersonnelUser.getString("locationTime"));

            //System.out.println("after putting " + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("jsobject öncesi  :" + obj.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTeamMemberPersonnelToSpinner();
                setPersonnelAvailableToSpinner();
                System.out.println(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        Toast.makeText(this, "Takım elemanı eklendi", Toast.LENGTH_SHORT).show();
        //finish();


    }

    public void removePersonnelFromTeam(){
        JSONObject myObject = selectedTeamMemberPersonnelUser;
        JSONObject obj = new JSONObject();
        try {
            //System.out.println("before putting " + obj.toString());
            obj.put("personnelID", myObject.getString("personnelID"));
            obj.put("personnelName", myObject.getString("personnelName"));
            obj.put("personnelEmail", myObject.getString("personnelEmail"));
            obj.put("personnelRole", "Belirlenmedi");
            obj.put("teamID", "0"); // teamID set to 0 to clear assignment on team
            obj.put("latitude", myObject.getString("latitude"));
            obj.put("longitude", myObject.getString("longitude"));
            obj.put("institution", myObject.getString("institution"));
            obj.put("locationTime", myObject.getString("locationTime"));
            System.out.println("after putting :" + obj.toString());
        } catch (Exception e) {
            e.getMessage();
        }
        //System.out.println("jsobject öncesi  :" + obj.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTeamMemberPersonnelToSpinner();
                setPersonnelAvailableToSpinner();
                System.out.println(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        Toast.makeText(this, "Takım elemanı çıkarıldı.", Toast.LENGTH_SHORT).show();
    }




}// class end