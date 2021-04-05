package com.example.afetkurtar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Authorized_Create_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Create_Team";

    //delete this tempString
    String tmpString ="";
    LinearLayout scroll;
    String howManyNumberText = "";
    RequestQueue queue;
    RadioGroup radioGroupRoleOfTeamMembers;
    String createdTeamID = "0";
    private RadioButton radioButtonRoleOfTeamMembers;
    private ArrayList<String> selectedPersonnelListWithID = new ArrayList<String>();
    private ArrayList<String> selectedPersonnelListWithRole = new ArrayList<String>();

    private ArrayList<String> afterCreateTeamStringList = new ArrayList<String>();

    private ArrayList<JSONObject> availablePersonnelObjectList = new ArrayList<JSONObject>();

    JSONObject data = new JSONObject();
    ArrayAdapter adapter;
    private static String personnelID = "";

    ArrayList<String> personnelAvailableStringList = new ArrayList<String>();
    ArrayList<View> viewList = new ArrayList<View>();
    ArrayList<String> personnelTeamMemberStringList = new ArrayList<String>();


    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
    //ArrayList<JSONObject> jsonObjectListForTeam = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_create_team);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn_createTeam_calculateHowManyMember).setOnClickListener(this::onClick);
        findViewById(R.id.btn_createTeam_Create).setOnClickListener(this::onClick);
        findViewById(R.id.btn_returnTo_assignTeam).setOnClickListener(this::onClick);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createTeam_calculateHowManyMember:
                try{
                    resetScrollElements();
                    viewList.clear();
                    if(isEditTextNumberOfMemberEnteredCorrectly()){
                        calculateHowManyViewAdd();

                    }
                }catch (Exception e){
                    e.getMessage();
                }

                break;
            case R.id.btn_createTeam_Create:
                try{

                    //deneme();
                    //deneme2();
                    //deneme3();
                    //deneme4();
                    //deneme5();
                    //deneme6();
                    createTeamWithSelectedPersonnels();
                }catch (Exception e){
                    e.getMessage();
                }
                break;
            case R.id.btn_returnTo_assignTeam:
                try{
                    //burada team id yi assign sayfasındaki spinner a bir şekilde ekleyelim
                    openTeamAssignPage();
                }catch (Exception e){
                    e.getMessage();
                }
                break;
        }
    }
    /*
    public void deneme(){
        for(int i = 0; i < viewList.size(); i++){
            tmpString = "";
            View v = viewList.get(i);
            ((TextView) v.findViewById(R.id.tv_availablePersonnel_create_team)).setText("ASD" + i );




            Spinner spinner = (Spinner) viewList.get(i).findViewById(R.id.create_team_availablePersonnelSpinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                        //textAfetId.setText("seçilmedi");
                    } else {

                        for (JSONObject x : jsonObjectList) {
                            try {
                                String personelIDPart = ((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString();
                                personelIDPart = findPersonnelID(personelIDPart);
                                //System.out.println("Personnel id " + personelIDPart);
                                if (x.getString("personelID").equals(personelIDPart)) {
                                    personnelID = (((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString());
                                    System.out.println("selectedPersonel ID: " + personnelID);
                                    return;
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


        }


    }*/
    /*
    public void deneme2(){

        System.out.println("LinearLayout scroll child at 0 : " + scroll.getChildAt(0));
        System.out.println("LinearLayout scroll child at 0 with find by wiew id: " + (scroll.getChildAt(0).findViewById(R.id.tv_availablePersonnel_create_team)));
        TextView tvDeneme = (TextView) scroll.getChildAt(0).findViewById(R.id.tv_availablePersonnel_create_team);
        tvDeneme.setText("acb");
        System.out.println("LinearLayout scroll child at 0 get id : " + scroll.getChildAt(0).getId());
        System.out.println("LinearLayout scroll child at 1 : " + scroll.getChildAt(1));
        TextView tvDeneme2 = (TextView) scroll.getChildAt(1).findViewById(R.id.tv_availablePersonnel_create_team);
        tvDeneme2.setText("glb");
        System.out.println("LinearLayout scroll child at 1 get id : " + scroll.getChildAt(0).getId());
        System.out.println("LinearLayout scroll child at 2 : " + scroll.getChildAt(2));
        System.out.println("LinearLayout scroll child at 0 (toString) : " + scroll.getChildAt(0).toString());


    }
    public void deneme3(){
        System.out.println("How many child does scroll have : " + scroll.getChildCount());
    }
    public void deneme4(){
        int count = scroll.getChildCount();
        for(int i = 0; i< count ; i++){
            Spinner linearSpinner = (Spinner) scroll.getChildAt(i).findViewById(R.id.create_team_availablePersonnelSpinner);
            String tmpLS = linearSpinner.getSelectedItem().toString();

            linearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //System.out.println("Test Authorized Team onItemSelected");
                    if (((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                        //System.out.println("Test Authorized Team : seçilmedi");
                        //textAfetId.setText("seçilmedi");
                    } else {
                        // System.out.println("Test Authorized Team else");
                        //System.out.println(jsonObjectList.toString());

                        for (JSONObject x : jsonObjectList) {
                            try {
                                String personelIDPart = ((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString();
                                personelIDPart = findPersonnelID(personelIDPart);
                                //System.out.println("Personnel id " + personelIDPart);
                                //System.out.println("Personnel id ???????????????????????????????????????????????????????999" + personelIDPart);
                                if (x.getString("personelID").equalsIgnoreCase(personelIDPart)) {
                                    personnelID = (((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString());
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

            System.out.println("tmpLS in deneme4() : " + tmpLS);
            System.out.println("Personel ID in deneme4() : " + personnelID);
        }
    }
    public void deneme5(){
        int count = scroll.getChildCount();
        for(int i = 0; i< count ; i++) {
            radioGroupRoleOfTeamMembers = (RadioGroup) scroll.getChildAt(i).findViewById(R.id.groupForTeamRole);
            int radioId = radioGroupRoleOfTeamMembers.getCheckedRadioButtonId();
            radioButtonRoleOfTeamMembers = (RadioButton) scroll.getChildAt(i).findViewById(radioId);

            String role = radioButtonRoleOfTeamMembers.getText().toString();

            System.out.println("index"+ i+ " role : " +role );
        }

    }
    */
    public void createTeamWithSelectedPersonnels(){
        createdTeamID = "0";
        selectedPersonnelListWithID.clear();
        selectedPersonnelListWithRole.clear();

        int count = scroll.getChildCount();

        for(int i = 0; i< count ; i++) {
            Spinner linearSpinner = (Spinner) scroll.getChildAt(i).findViewById(R.id.create_team_availablePersonnelSpinner);
            String selectedSpinnerText = linearSpinner.getSelectedItem().toString();
            String selectedPersonnelID = "";
            selectedPersonnelID = findPersonnelID(selectedSpinnerText).trim();

            radioGroupRoleOfTeamMembers = (RadioGroup) scroll.getChildAt(i).findViewById(R.id.groupForTeamRole);
            int radioId = radioGroupRoleOfTeamMembers.getCheckedRadioButtonId();
            radioButtonRoleOfTeamMembers = (RadioButton) scroll.getChildAt(i).findViewById(radioId);

            String selectedPersonnelRole = "";
            selectedPersonnelRole = radioButtonRoleOfTeamMembers.getText().toString();

            if(!(selectedPersonnelID.equalsIgnoreCase("") || selectedPersonnelID.equalsIgnoreCase("Personel Seçin"))){
                selectedPersonnelListWithID.add(selectedPersonnelID);
                if(selectedPersonnelRole.equalsIgnoreCase("Kaptan")){
                    selectedPersonnelListWithRole.add("Kaptan");
                }else{
                    selectedPersonnelListWithRole.add("Normal");
                }
            }

            //System.out.println("index"+ i+ " role : " + selectedPersonnelRole );
        }//end of for

        //controlPersonnelListWithID();
        if(!controlPersonnelListWithRole()){
            return;
        }
        if(!controlPersonnelListWithID()){
            return;
        }

        createTeamWithGivenInformationToDB();
        /*
        for(int i = 0; i< selectedPersonnelListWithID.size(); i++){
            System.out.println("for entry selectedID: " +selectedPersonnelListWithID.get(i));
        }
        for(int i = 0; i< selectedPersonnelListWithRole.size(); i++){
            System.out.println("for entry selectedRole: "+ selectedPersonnelListWithRole.get(i));
        }
        */

    }
    ////////////////////////////////////////////////
    public void findCreatedTeamID(){
        readFromTeamTable();



    }
    public void readFromTeamTable() {
        //System.out.println("is it in here Read From table ?");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/team/read.php", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForTeamTable(response);

                            if(Authorized_Assign_Team.teamStringListSize == afterCreateTeamStringList.size()){
                               // System.out.println("is it in here =????? Ifffff");
                               // System.out.println("teamStringListSize : " + Authorized_Assign_Team.teamStringListSize);
                                createdTeamID = "0";
                            }else{
                               // System.out.println("is it in here =????? Elseeeee");
                                // db de ıd ne olursa olsun hep son eleman
                                // olarak eklenicek gibi olduğundan okuduğum verilerde en son elemanın team id sini team id olarak belirlerim
                                createdTeamID = afterCreateTeamStringList.get(afterCreateTeamStringList.size()-1);
                                //System.out.println("createdTeamID was updated :" + createdTeamID);
                            }

                            /// team id üretildi şimdi bu team id kullanılarak üyelerin assigned team id lerini burada güncelle
                            updateNewPersonnelsTeamID();


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
        /*
        if(Authorized_Assign_Team.teamStringListSize == afterCreateTeamStringList.size()){
            System.out.println("is it in here =????? Ifffff");
            System.out.println("teamStringListSize : " + Authorized_Assign_Team.teamStringListSize);
            createdTeamID = "0";
        }else{
            System.out.println("is it in here =????? Elseeeee");
            // db de ıd ne olursa olsun hep son eleman
            // olarak eklenicek gibi olduğundan okuduğum verilerde en son elemanın team id sini team id olarak belirlerim
            createdTeamID = afterCreateTeamStringList.get(afterCreateTeamStringList.size()-1);
            System.out.println("createdTeamID was updated :" + createdTeamID);
        }

        */
    }

    public void handleResponseForTeamTable(JSONObject a) {
        //System.out.println("is it in here responsehandle teamtable ?");
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

            afterCreateTeamStringList.add("Takım Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    //System.out.println("tmp.getString ===============???? : " + tmp.getString("teamID"));
                    afterCreateTeamStringList.add(tmp.getString("teamID"));
                    //jsonObjectListForTeam.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    /////////////////////////////////////////////////////////////////////for find Team ID
    public void createTeamWithGivenInformationToDB(){
        //create a team
        sendTeamTableDataToDB();
        //to specify createdTeamID

    }
    public void updatePersonnelUserDataToDB(){

    }
    public void sendTeamTableDataToDB(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedSubpartID", "0");
            obj.put("status", "yeni guncelleme");
            obj.put("needManPower", "0");
            obj.put("needEquipment", "0");
            //// Dikkat oluşturulan team id nerden bilinecek burada onu güncelle bir şekilde
        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/team/create.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //rintln("OnResponse output : " + response.toString());
               // System.out.println("before findeCreated ?");
                findCreatedTeamID();

                //System.out.println("after findeCreated ?");

                //addTeamIDToAssignTeamSpinner();
                //after create team, we need to update every personnel in this team with new roles and new assigned team id
                //updatePersonnelUserDataToDB();

                //finish();
                //System.out.println("before openTeamAssignPage ?");
                //openTeamAssignPage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        queue.add(request);
        Toast.makeText(this, "Takım oluşturuldu.", Toast.LENGTH_LONG).show();
        //addTeamIDToAssignTeamSpinner();
        //finish();
    }
    /*
    public void addTeamIDToAssignTeamSpinner(){

    }
    */

    public void openTeamAssignPage(){
        //System.out.println("is it in openTeamAssignPage ?");
        Intent intent = new Intent(this, Authorized_Assign_Team.class);
        startActivity(intent);
    }
    public boolean controlPersonnelListWithRole(){
        int captanCount = 0;
        for(int i = 0 ; i<selectedPersonnelListWithRole.size(); i++){
            if(selectedPersonnelListWithRole.get(i).equalsIgnoreCase("Kaptan")){
                captanCount++;
            }
            if(captanCount > 1){
                Toast.makeText(this, "Birden Fazla kaptan seçilemez!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(captanCount == 0){
            Toast.makeText(this, "En az bir takım kaptanı seçilmelidir.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean controlPersonnelListWithID(){
        for(int i = 0 ; i<selectedPersonnelListWithID.size(); i++){
            for (int j = i+1; j<selectedPersonnelListWithID.size(); j++){
                if(selectedPersonnelListWithID.get(i).equalsIgnoreCase(selectedPersonnelListWithID.get(j))){
                    Toast.makeText(this, "Aynı personel tekrar seçilmemelidir!", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        return true;
    }


    public void resetScrollElements(){
        ((LinearLayout)findViewById(R.id.authorized_create_team_linearLayoutScroll)).removeAllViews();
    }
    public boolean isEditTextNumberOfMemberEnteredCorrectly(){
        howManyNumberText = ((EditText)findViewById(R.id.et_create_wantedNumberOfMember)).getText().toString();
        if(howManyNumberText.equalsIgnoreCase("")){
            Toast.makeText(this, "Belirlenecek üye sayısını giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void calculateHowManyViewAdd(){
        int howMany = 0;
        try{
            howMany = Integer.parseInt(howManyNumberText);
        }catch (Exception e){
            e.getMessage();
        }

        for(int i = 0; i< howMany; i++){
            addPersonnelAddViewToScroll();
        }

    }

     public void addPersonnelAddViewToScroll(){
         LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View view = inflater.inflate(R.layout.activity_authorized_create_team_addlayout, null);
         scroll = findViewById(R.id.authorized_create_team_linearLayoutScroll);
         TextView tv_Linear = findViewById(R.id.tv_availablePersonnel_create_team);
         Spinner linearSpinner = (Spinner) view.findViewById(R.id.create_team_availablePersonnelSpinner);

         setPersonnelAvailableToSpinner(linearSpinner);
         /*
         linearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 //System.out.println("Test Authorized Team onItemSelected");
                 if (((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                     //System.out.println("Test Authorized Team : seçilmedi");
                     //textAfetId.setText("seçilmedi");
                 } else {
                     // System.out.println("Test Authorized Team else");
                     //System.out.println(jsonObjectList.toString());

                     for (JSONObject x : jsonObjectList) {
                         try {
                             String personnelIDPart = ((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString();
                             personnelIDPart = findPersonnelID(personnelIDPart);
                             System.out.println("Personnel id " + personnelIDPart);
                             System.out.println("Personnel id ???????????????????????????????????????????????????????999" + personnelIDPart);
                             if (x.getString("personnelID").equals(personnelIDPart)) {
                                 personnelID = (((Spinner) findViewById(R.id.create_team_availablePersonnelSpinner)).getSelectedItem().toString());
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
         */
         //viewList.add(view);
         scroll.addView(view);
     }// deneme end
    public void setPersonnelAvailableToSpinner(Spinner availableMembersSpinner) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForPersonnelAvailableTable(response,availableMembersSpinner);
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

    public void handleResponseForPersonnelAvailableTable(JSONObject a,Spinner availableMembersSpinner) {
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
                        availablePersonnelObjectList.add(tmp);
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

    public String findPersonnelID(String line){
        if(line.indexOf("PersonnelID:") != -1){
            line = line.substring(12,line.indexOf(","));
            line.trim();
            System.out.println("Test Line : " + line);
            return line;
        }
        return "";
    }
    public void updateNewPersonnelsTeamID(){
        //burasi yeni personellerin team id lerinin güncellenmesi için
        //System.out.println("bu methoda girdi mi update new personnel xxx");
        //System.out.println("availablePersonnelObjectList.size():" + availablePersonnelObjectList.size());
        //System.out.println("selectedPersonnelListWithID.size():" + selectedPersonnelListWithID.size());
        for(int i = 0 ; i<availablePersonnelObjectList.size(); i++){
            for(int j = 0 ; j < selectedPersonnelListWithID.size(); j++){
                try {
                   // System.out.println("available personnel ID : " + availablePersonnelObjectList.get(i).getString("personnelID"));
                    //System.out.println("selected personnel ID : " + selectedPersonnelListWithID.get(j));
                    if(availablePersonnelObjectList.get(i).getString("personnelID").trim().equalsIgnoreCase(selectedPersonnelListWithID.get(j).trim())){
                      //  System.out.println("Update New Personnel team id and role icin for a girdi");
                        updateTeamIDAndRole(availablePersonnelObjectList.get(i),selectedPersonnelListWithRole.get(j));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /////////////////////////////
    public void updateTeamIDAndRole(JSONObject myJsonObject,String role){
        //System.out.println("jsonobject oluşturdu.");
        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", myJsonObject.getString("personnelID"));
            obj.put("personnelName", myJsonObject.getString("personnelName"));
            obj.put("personnelEmail", myJsonObject.getString("personnelEmail"));
            obj.put("personnelRole", role);
            obj.put("teamID", createdTeamID); // teamID set to 0 to clear assignment on team
            obj.put("latitude", myJsonObject.getString("latitude"));
            obj.put("longitude", myJsonObject.getString("longitude"));
            obj.put("institution", myJsonObject.getString("institution"));
            obj.put("locationTime", myJsonObject.getString("locationTime"));
            //System.out.println("oluşturduğumuz objenin to stringi : " + obj.toString());

        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response.toString());
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


}//class end
