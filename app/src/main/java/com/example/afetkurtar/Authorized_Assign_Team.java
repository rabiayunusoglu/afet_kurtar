package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Authorized_Assign_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Assign_Team";
    RequestQueue queue;
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;

    Spinner teamSpinner;
    Spinner subpartSpinner;
    ArrayAdapter adapter;

    NotificationSender notificationSender;
    //
    ArrayList<String> subpartStringList = new ArrayList<String>();

    ArrayList<String> teamStringList = new ArrayList<String>();
    ArrayList<JSONObject> jsonObjectListForTeam = new ArrayList<JSONObject>();
    ArrayList<JSONObject> jsonObjectListForSubpart = new ArrayList<JSONObject>();

    //private ArrayList<JSONObject> teamMemberPersonnelObjectList = new ArrayList<JSONObject>();
    public static String teamID = "";
    public static String needManPower = "0";
    public static String needEquipment = "0";
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
        notificationSender = new NotificationSender(getApplicationContext());

        drawerLayout = findViewById(R.id.authorized_assign_team_drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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

                    for (JSONObject x : jsonObjectListForTeam) {
                        try {
                            if (x.getString("teamID").equals(((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString())) {
                                teamID = (((Spinner) findViewById(R.id.assignTeamSpinner)).getSelectedItem().toString());
                                calaculateSelectedTeamInformations();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
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

                    for (JSONObject x : jsonObjectListForSubpart) {
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
        setSubpartToSelectedSubpartID();
    }//onCreate end


    //setSubpartToSelectedSubpartID methodu sonradan silinecek
    public void setSubpartToSelectedSubpartID(){
        this.selectedSubpartID = "Personel Seçin";
        System.out.println("selectedS id : " + selectedSubpartID);
        try{
            this.selectedSubpartID = Disaster_Details.selectedSubpartID;
            System.out.println("before set spinner selectedS id : " + selectedSubpartID);
            setSpinnerValue(selectedSubpartID,subpartSpinner);
        }catch (Exception e){
            e.getMessage();
        }
    }

    //private method of your class
    private int getIndexFromSpinner(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                System.out.println("indexi neymiş :" + i);
                return i;
            }
        }
        return 0;
    }

    public void calaculateSelectedTeamInformations(){
        for(int i = 0; i<jsonObjectListForTeam.size(); i++){
            if(teamStringList.get(i).equalsIgnoreCase(teamID)){
                try {
                    needManPower = jsonObjectListForTeam.get(i).getString("needManPower").toString();
                    System.out.println("needmanpower value ? : " + needManPower);
                    needEquipment = jsonObjectListForTeam.get(i).getString("needEquipment").toString();
                    System.out.println("needEquipment value ? : " + needEquipment);
                    status = jsonObjectListForTeam.get(i).getString("status").toString();
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
                if(isTeamSelected()){
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
                    jsonObjectListForTeam.add(tmp);
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
                    jsonObjectListForSubpart.add(tmp);
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
    public void setSpinnerValue(String spinnerValue, Spinner mSpinner){
        /*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        if (spinnerValue != null) {
            int spinnerPosition = adapter.getPosition(spinnerValue);
            mSpinner.setSelection(spinnerPosition);
        }

         */
        System.out.println("getIndexFromSpinner(subpartSpinner,selectedSubpartID) : " + getIndexFromSpinner(subpartSpinner,selectedSubpartID));
        //((Spinner) findViewById(R.id.assignSubpartSpinner)).setSelection(getIndexFromSpinner(subpartSpinner,selectedSubpartID));
        //mSpinner.setSelection(getIndex(mSpinner, spinnerValue));
        ((Spinner) findViewById(R.id.assignSubpartSpinner)).setSelection(getIndex(mSpinner,spinnerValue));
        //private method of your class
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
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
                refreshActivity();
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
        //findUsersListsForSelectedTeamID(); //for xreate lists
        removeTeamFromDB();

    }
/*
    public void resetPersonnelTeamMemberLists(){
        teamMemberPersonnelObjectList.clear();
    }

 */
    //*************************************************************************************090909
    public void findUsersInPersonnelUserTable() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("teamID", teamID.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ArrayList<JSONObject> personnelObjectsList = handlePersonnelObject(response);
                            for(int i = 0; i< personnelObjectsList.size(); i++){
                                updateTeamIDInPersonnelUserTable(personnelObjectsList.get(i));
                            }

                            findUsersInVolunteerUserTable();

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

    public ArrayList<JSONObject> handlePersonnelObject(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<JSONObject> teamMembersInPersonnelList = new ArrayList<JSONObject>();
        try {

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
                    teamMembersInPersonnelList.add(tmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return teamMembersInPersonnelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }// handle response end
    public void updateTeamIDInPersonnelUserTable(JSONObject personnelObject) {

        JSONObject obj = new JSONObject();
        try {

            obj.put("personnelID", personnelObject.getString("personnelID"));
            obj.put("personnelName", personnelObject.getString("personnelName"));
            obj.put("personnelEmail", personnelObject.getString("personnelEmail"));
            obj.put("personnelRole", "Belirlenmedi");
            obj.put("teamID", "0"); // teamID set to 0 to clear assignment on team
            obj.put("latitude", personnelObject.getString("latitude"));
            obj.put("longitude", personnelObject.getString("longitude"));
            obj.put("institution", personnelObject.getString("institution"));
            obj.put("locationTime", personnelObject.getString("locationTime"));


        } catch (Exception e) {
            e.getMessage();
        }
        System.out.println("-----------------------Our personnel user update object: " + obj.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                try {
                    notificationSender.sendNotification("Takımdan Çıkarıldınız",
                            "Yetkili yöneticilerden birisi sizi bulunduğunuz takımdan çıkardı.",personnelObject.getString("personnelID").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        //Toast.makeText(this, "Personelin lokasyonu güncellendi.", Toast.LENGTH_SHORT).show();

    }
    //*************************************************************************************090909
    /*
   public void findUsersListsForSelectedTeamID() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());

                            //handleResponseFindUserListForSelectedTeamID(response);
                            //updateTeamIDForPersonnelsInTeam(); // for update users in lists
                            //findUsersInVolunteerUserTable();




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
     */
    ////////////////////////////////////////////////////////////-------------------------------------------
    public void findUsersInVolunteerUserTable() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("teamID", teamID.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ArrayList<JSONObject> volunteerObjectsList = handleVolunteerObject(response);
                            for(int i = 0; i< volunteerObjectsList.size(); i++){
                                updateTeamIDInVolunteerUserTable(volunteerObjectsList.get(i));
                            }
                            //removeTeamFromDB(); // remove team from DB
                            //refreshActivity();// refresh the activity
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

    public ArrayList<JSONObject> handleVolunteerObject(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<JSONObject> teamMembersInVolunteerList = new ArrayList<JSONObject>();
        try {

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
                    teamMembersInVolunteerList.add(tmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return teamMembersInVolunteerList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }// handle response end
    public void updateTeamIDInVolunteerUserTable(JSONObject volunteerObject) {

        JSONObject obj = new JSONObject();
        try {

            obj.put("volunteerID", volunteerObject.getString("volunteerID"));
            obj.put("volunteerName", volunteerObject.getString("volunteerName"));
            obj.put("address", volunteerObject.getString("address"));
            obj.put("isExperienced", volunteerObject.getString("isExperienced"));
            obj.put("haveFirstAidCert", volunteerObject.getString("haveFirstAidCert"));
            obj.put("requestedSubpart", volunteerObject.getString("requestedSubpart"));
            obj.put("responseSubpart", volunteerObject.getString("responseSubpart"));
            obj.put("assignedTeamID", "0"); // reset Team id
            obj.put("role", volunteerObject.getString("role"));
            obj.put("latitude", volunteerObject.getString("latitude"));
            obj.put("longitude", volunteerObject.getString("longitude"));
            obj.put("locationTime", volunteerObject.getString("locationTime"));
            obj.put("tc", volunteerObject.getString("tc"));
            obj.put("tel", volunteerObject.getString("tel"));
            obj.put("birthDate", volunteerObject.getString("birthDate"));

        } catch (Exception e) {
            e.getMessage();
        }
        System.out.println("-----------------------Our volunteer user update object: " + obj.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                try {
                    notificationSender.sendNotification("Takımdan Çıkarıldınız",
                            "Yetkili yöneticilerden birisi sizi bulunduğunuz takımdan çıkardı.",volunteerObject.getString("volunteerID").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        //Toast.makeText(this, "Personelin lokasyonu güncellendi.", Toast.LENGTH_SHORT).show();

    }
    ////////////////////////////////////////////////////////////--------------------------------------------------------
    /*
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
                    }
                    //jsonObjectList.add(tmp);
                } catch (Exception e) {
                     e.printStackTrace();
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
            public void onResponse(JSONObject response)
            {
                System.out.println(response.toString());
                try {
                    System.out.println("############################################## personel ID : " + myObject.getString("personnelID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    notificationSender.sendNotification("Takımdan Çıkarıldınız",
                            "Yetkili yöneticilerden birisi sizi bulunduğunuz takımdan çıkardı.",myObject.getString("personnelID").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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

     */
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
                            findUsersInPersonnelUserTable();
                            //resetPersonnelTeamMemberLists();
                            //ReturnBack();
                            refreshActivity();
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
    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    ///////////////////////////////////drawer işlemleri////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        redirectActivity(this, Authorized_VolunteerRequest.class);
    }
    //MESAJ
    public void ClickAuthorizedMessage(View view) {
        redirectActivity(this, MessageActivity.class);
    }
    // CIKIS
    public void ClickAuthorizedExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class );
    }
    public void ClickNotificationSend(View view) {
        redirectActivity(this, Authorized_Send_Notification.class );
    }

    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        redirectActivity(this, Authorized_Anasayfa.class );
    }

    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public void ClickAuthorizedEkipman(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Assign_Team.class );
    }
}// class end