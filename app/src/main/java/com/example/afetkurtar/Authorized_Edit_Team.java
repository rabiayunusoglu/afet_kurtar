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
import android.widget.Button;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Authorized_Edit_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Edit_Team";
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    RequestQueue queue;
    JSONObject data = new JSONObject();
    Spinner availableMembersSpinner;
    Spinner teamMembersSpinner;

    ////****
    Spinner availableVolunteersSpinner;
    Spinner volunteersSpinner;
    ////****

    ArrayAdapter adapter;
    NotificationSender notificationSender;
    ArrayList<String> personnelAvailableStringList = new ArrayList<String>();
    ArrayList<String> personnelTeamMemberStringList = new ArrayList<String>();
    JSONObject selectedAvailablePersonnelUser = new JSONObject();
    JSONObject selectedTeamMemberPersonnelUser = new JSONObject();

    //////////****
    ArrayList<String> volunteerAvailableStringList = new ArrayList<String>();
    ArrayList<String> volunteerTeamMemberStringList = new ArrayList<String>();
    JSONObject selectedAvailableVolunteerUser = new JSONObject();
    JSONObject selectedTeamMemberVolunteerUser = new JSONObject();
    //////////****

    ArrayList<JSONObject> jsonObjectListForAvailableUser = new ArrayList<JSONObject>();
    ArrayList<JSONObject> jsonObjectListForTeamMember = new ArrayList<JSONObject>();


    ////****
    ArrayList<JSONObject> jsonObjectListForAvailableVolunteer = new ArrayList<JSONObject>();
    ArrayList<JSONObject> jsonObjectListForVolunteer = new ArrayList<JSONObject>();
    ////****


    private boolean isAssignedSuppartIsOpenForVolunteer = false;
    //private static String personnelID;

    ////****
    //private static String volunteerID;
    ////****


    //private String selectedSubpartID = "";
    //private String isSelectedSubpartIsOpenForVolunteers = "";
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

        ////****
        findViewById(R.id.btn_editTeam_addVolunteer).setOnClickListener(this::onClick);
        findViewById(R.id.btn_editTeam_removeVolunteer).setOnClickListener(this::onClick);

        findViewById(R.id.btn_equip_details).setOnClickListener(this::onClick);
        ////****

        findViewById(R.id.btn_returnTo_assignTeam_fromEditTeam).setOnClickListener(this::onClick);
        notificationSender = new NotificationSender(getApplicationContext());

        drawerLayout = findViewById(R.id.authorized_edit_team_drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        availableMembersSpinner = (Spinner) findViewById(R.id.edit_team_available_personnelSpinner);
        teamMembersSpinner = (Spinner) findViewById(R.id.edit_team_personnelMembersSpinner);

        ////****
        availableVolunteersSpinner = (Spinner) findViewById(R.id.edit_team_available_volunteerSpinner);
        volunteersSpinner = (Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner);
        ////****

        try{
            this.selectedTeamID = Authorized_Assign_Team.teamID;
            this.ourManPower = Authorized_Assign_Team.needManPower;
            this.ourEquipment = Authorized_Assign_Team.needEquipment;
        }catch (Exception e){
            e.getMessage();
        }

        initTextViews();


        //// Dikkat burdan sonra spinner ları invisibla yada visible yapcaz ...
        findIsAssignedSuppartOpenForVolunteers(); // for find isAssignedSuppartIsOpenForVolunteer
        //use isAssignedSuppartIsOpenForVolunteer
        //isAssignedSuppartIsOpenForVolunteer
        if(isAssignedSuppartIsOpenForVolunteer){
            initSpinners();
        }else{
            availableVolunteersSpinner.setVisibility(View.GONE);
            volunteersSpinner.setVisibility(View.GONE);
            ((TextView)(findViewById(R.id.tv_team_available_volunteerSpinner))).setVisibility(View.GONE);
            ((TextView)(findViewById(R.id.tv_team_volunteerMembersSpinner))).setVisibility(View.GONE);
            ((Button)(findViewById(R.id.btn_editTeam_addVolunteer))).setVisibility(View.GONE);
            ((Button)(findViewById(R.id.btn_editTeam_removeVolunteer))).setVisibility(View.GONE);
            initPersonnelSpinners();
        }
        
        /*
        try{
            setPersonnelAvailableToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        availableMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForAvailableUser) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString();
                            String personnelID = findPersonnelID(personnelIDPart);
                            if (x.getString("personnelID").trim().equals(personnelID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailablePersonnelUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });


        try{
            setTeamMemberPersonnelToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        teamMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");

                    System.out.println("Seçildi mi seçilmedi mi artık bi karar ver +%%%%%%+++");
                } else {
                    System.out.println(jsonObjectListForTeamMember.toString());

                    System.out.println("Eleman seçildiiiiiiii=======================================*0*0*0*0*0*0*0");

                    for (JSONObject x : jsonObjectListForTeamMember) {
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString();
                            String personnelID = findPersonnelID(personnelIDPart);
                            System.out.println("personnelID değerimiz nedir ????? : " + personnelID);

                            System.out.println("x.getSting değerimizzzzz: " +x.getString("personnelID").trim());

                              if (x.getString("personnelID").trim().equals(personnelID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().trim());
                                System.out.println("Niye atanmıyosunnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + x.toString());
                                selectedTeamMemberPersonnelUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });

        /////////////////////////????????????????????????????????????????Volunteer part start

        try{
            setVolunteerAvailableToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        availableVolunteersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString().equalsIgnoreCase("Gönüllü Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForAvailableVolunteer) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String volunteerIDPart = ((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString();
                            String volunteerID = findVolunteerID(volunteerIDPart);
                            if (x.getString("volunteerID").trim().equals(volunteerID.trim())) {
                                volunteerID = (((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailableVolunteerUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });


        try{
            setTeamMemberVolunteerToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }
        volunteersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Gönüllü Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    System.out.println(jsonObjectListForVolunteer.toString());

                    for (JSONObject x : jsonObjectListForVolunteer) {
                        try {
                            String volunteerIDPart  = ((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString();
                            String volunteerID = findVolunteerID(volunteerIDPart);
                            if (x.getString("volunteerID").trim().equals(volunteerID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner)).getSelectedItem().toString().trim());
                                //System.out.println("Niye atanmıyosunnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + x.toString());
                                selectedTeamMemberVolunteerUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });

        /////////////////////////????????????????????????????????????????Volunteer part end


         */

    }// on create end

    public void initPersonnelSpinners(){
        try{
            setPersonnelAvailableToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        availableMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForAvailableUser) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString();
                            String personnelID = findPersonnelID(personnelIDPart);
                            if (x.getString("personnelID").trim().equals(personnelID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailablePersonnelUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });


        try{
            setTeamMemberPersonnelToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        teamMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");

                    System.out.println("Seçildi mi seçilmedi mi artık bi karar ver +%%%%%%+++");
                } else {
                    System.out.println(jsonObjectListForTeamMember.toString());

                    System.out.println("Eleman seçildiiiiiiii=======================================*0*0*0*0*0*0*0");

                    for (JSONObject x : jsonObjectListForTeamMember) {
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString();
                            String personnelID = findPersonnelID(personnelIDPart);
                            System.out.println("personnelID değerimiz nedir ????? : " + personnelID);

                            System.out.println("x.getSting değerimizzzzz: " +x.getString("personnelID").trim());

                            if (x.getString("personnelID").trim().equals(personnelID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().trim());
                                System.out.println("Niye atanmıyosunnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + x.toString());
                                selectedTeamMemberPersonnelUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });
    }

    public void initVolunteerSpinners(){
        try{
            setVolunteerAvailableToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        availableVolunteersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString().equalsIgnoreCase("Gönüllü Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForAvailableVolunteer) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String volunteerIDPart = ((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString();
                            String volunteerID = findVolunteerID(volunteerIDPart);
                            if (x.getString("volunteerID").trim().equals(volunteerID.trim())) {
                                volunteerID = (((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailableVolunteerUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });


        try{
            setTeamMemberVolunteerToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }
        volunteersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Gönüllü Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    System.out.println(jsonObjectListForVolunteer.toString());

                    for (JSONObject x : jsonObjectListForVolunteer) {
                        try {
                            String volunteerIDPart  = ((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString();
                            String volunteerID = findVolunteerID(volunteerIDPart);
                            if (x.getString("volunteerID").trim().equals(volunteerID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner)).getSelectedItem().toString().trim());
                                //System.out.println("Niye atanmıyosunnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + x.toString());
                                selectedTeamMemberVolunteerUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });

    }

    public void initSpinners(){
        initPersonnelSpinners();
        initVolunteerSpinners();
        /*
        try{
            setPersonnelAvailableToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        availableMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForAvailableUser) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString();
                            String personnelID = findPersonnelID(personnelIDPart);
                            if (x.getString("personnelID").trim().equals(personnelID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_available_personnelSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailablePersonnelUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });


        try{
            setTeamMemberPersonnelToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        teamMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Personel Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");

                    System.out.println("Seçildi mi seçilmedi mi artık bi karar ver +%%%%%%+++");
                } else {
                    System.out.println(jsonObjectListForTeamMember.toString());

                    System.out.println("Eleman seçildiiiiiiii=======================================*0*0*0*0*0*0*0");

                    for (JSONObject x : jsonObjectListForTeamMember) {
                        try {
                            String personnelIDPart = ((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString();
                            String personnelID = findPersonnelID(personnelIDPart);
                            System.out.println("personnelID değerimiz nedir ????? : " + personnelID);

                            System.out.println("x.getSting değerimizzzzz: " +x.getString("personnelID").trim());

                            if (x.getString("personnelID").trim().equals(personnelID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_personnelMembersSpinner)).getSelectedItem().toString().trim());
                                System.out.println("Niye atanmıyosunnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + x.toString());
                                selectedTeamMemberPersonnelUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });

        /////////////////////////????????????????????????????????????????Volunteer part start

        try{
            setVolunteerAvailableToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }

        availableVolunteersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString().equalsIgnoreCase("Gönüllü Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForAvailableVolunteer) {
                        System.out.println("our X object : " + x.toString());
                        try {
                            String volunteerIDPart = ((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString();
                            String volunteerID = findVolunteerID(volunteerIDPart);
                            if (x.getString("volunteerID").trim().equals(volunteerID.trim())) {
                                volunteerID = (((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString().trim());
                                System.out.println("our X object inside if : " + x.toString());
                                selectedAvailableVolunteerUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });


        try{
            setTeamMemberVolunteerToSpinner();
        }catch (Exception e){
            e.printStackTrace();
        }
        volunteersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner)).getSelectedItem().toString().equalsIgnoreCase("Gönüllü Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    System.out.println(jsonObjectListForVolunteer.toString());

                    for (JSONObject x : jsonObjectListForVolunteer) {
                        try {
                            String volunteerIDPart  = ((Spinner) findViewById(R.id.edit_team_available_volunteerSpinner)).getSelectedItem().toString();
                            String volunteerID = findVolunteerID(volunteerIDPart);
                            if (x.getString("volunteerID").trim().equals(volunteerID.trim())) {
                                //personnelID = (((Spinner) findViewById(R.id.edit_team_volunteerMembersSpinner)).getSelectedItem().toString().trim());
                                //System.out.println("Niye atanmıyosunnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + x.toString());
                                selectedTeamMemberVolunteerUser = x;
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
                System.out.println("Test Authorized Team on Nothing selected ");
            }
        });

         */
    }





    public void findIsAssignedSuppartOpenForVolunteers(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID", selectedTeamID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/teamID/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            String assignedSubpartID = response.getString("assignedSubpartID");
                            findSubpartInformation(assignedSubpartID); // after this merhod isAssignedSubpartOpenForVolunteerBoolean value is known



                            /*
                            if(assignedSubpartID.equalsIgnoreCase("0")){
                                return
                            }else
                                return false;
                            }

                             */

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
        //return false; // if error than return false; --> is not open for volunteers
    }
    public void findSubpartInformation(String assignedSubpartID){
        JSONObject obj = new JSONObject();
        try {
            obj.put("subpartID", assignedSubpartID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/subpart/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            String isOpenForVolunteers = response.getString("isOpenForVolunteers").trim();

                            if(isOpenForVolunteers.equalsIgnoreCase("0")){
                                isAssignedSuppartIsOpenForVolunteer = false;
                            }else{
                                isAssignedSuppartIsOpenForVolunteer = true;
                            }


                            // edit spinners visible here Dikkat!...

                            ///Dikkat initSpinners methodu burada kullanılacak

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
    public void initTextViews(){
        ((TextView)(findViewById(R.id.tv_team_manpower))).setText(ourManPower);
        ((TextView)(findViewById(R.id.tv_team_equipment))).setText(ourEquipment);
        ((TextView)(findViewById(R.id.tv_selected_teamID))).setText(selectedTeamID);

    }
    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    public String findPersonnelID(String line){
         if(line.indexOf("PersonnelID:") != -1){
             line = line.substring(line.indexOf(":")+1,line.indexOf(","));
             line.trim();
             System.out.println("Personnel ID Test Line ?: " + line);
             return line;
         }
         return null;
    }
    public String findVolunteerID(String line){
        if(line.indexOf(":") != -1){
            line = line.substring(line.indexOf("volunteerID:")+1,line.indexOf(","));
            line.trim();
            System.out.println("Volunteer ID Test Line  ?: " + line);
            return line;
        }
        return null;
    }

    public void openTeamAssignPage(){
        Intent intent = new Intent(this, Authorized_Assign_Team.class);
        startActivity(intent);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editTeam_addPersonnel:
                addPersonnelToTeam();

                break;
            case R.id.btn_editTeam_removePersonnel:
                removePersonnelFromTeam();


                break;
            case R.id.btn_editTeam_addVolunteer:
                if(isAssignedSuppartIsOpenForVolunteer){
                    addVolunteerToTeam();
                }

                break;
            case R.id.btn_editTeam_removeVolunteer:
                if(isAssignedSuppartIsOpenForVolunteer){
                    removeVolunteerFromTeam();
                }

                break;
            case R.id.btn_returnTo_assignTeam_fromEditTeam:
                try{
                    //burada team id yi assign sayfasındaki spinner a bir şekilde ekleyelim
                    openTeamAssignPage();
                }catch (Exception e){
                    e.getMessage();
                }
                break;
            case R.id.btn_equip_details:
                //redirectActivity(this, Authorized_Team_Equipment_Requests.class );
                Intent intent = new Intent(this, Authorized_Team_Equipment_Requests.class);
                intent.putExtra("Team_id",selectedTeamID);
                startActivity(intent);
                break;
        }
    }

    public void setPersonnelAvailableToSpinner() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

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
                    //if(tmp.getString("teamID").equals("0")){
                        personnelAvailableStringList.add("PersonnelID: " + tmp.getString("personnelID") +", Personnel İsmi: "+ tmp.getString("personnelName"));
                    //}
                    jsonObjectListForAvailableUser.add(tmp);
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, personnelAvailableStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            availableMembersSpinner.setAdapter(adapter);
            personnelAvailableStringList = new ArrayList<String>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    public void setTeamMemberPersonnelToSpinner() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID", selectedTeamID.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

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
                    //if(tmp.getString("teamID").equals(selectedTeamID)){
                        personnelTeamMemberStringList.add("PersonnelID: " + tmp.getString("personnelID") +", Personnel İsmi: "+ tmp.getString("personnelName"));
                    //}
                    jsonObjectListForTeamMember.add(tmp);
                    System.out.println("Listeye eklendi............................ tmp : " + tmp.toString());
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, personnelTeamMemberStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teamMembersSpinner.setAdapter(adapter);
            personnelTeamMemberStringList = new ArrayList<String>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    //////////////////////////((((((((((((((((((((((((((((((((((VolunteerPart start



    public void setVolunteerAvailableToSpinner() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedTeamID", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest


                (Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/search.php", obj, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForVolunteerAvailableTable(response);
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

    public void handleResponseForVolunteerAvailableTable(JSONObject a) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);

            volunteerAvailableStringList.add("Gönüllü Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    //if(tmp.getString("assignedTeamID").trim().equals("0")){
                        volunteerAvailableStringList.add("VolunteerID: " + tmp.getString("volunteerID") +", Gönüllü İsmi: "+ tmp.getString("volunteerName"));
                    //}
                    jsonObjectListForAvailableVolunteer.add(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, volunteerAvailableStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            availableVolunteersSpinner.setAdapter(adapter);
            volunteerAvailableStringList = new ArrayList<String>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    public void setTeamMemberVolunteerToSpinner() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedTeamID", selectedTeamID.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForTeamMemberVolunteerTable(response);
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
    public void handleResponseForTeamMemberVolunteerTable(JSONObject a) {
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

            volunteerTeamMemberStringList.add("Gönüllü Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    //if(tmp.getString("assignedTeamID").trim().equals(selectedTeamID)){
                        volunteerTeamMemberStringList.add("VolunteerID: " + tmp.getString("volunteerID") +", Gönüllü İsmi: "+ tmp.getString("volunteerName"));
                    //}
                    jsonObjectListForVolunteer.add(tmp);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, volunteerTeamMemberStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            volunteersSpinner.setAdapter(adapter);
            volunteerTeamMemberStringList = new ArrayList<String>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    ///////////////////////////(((((((((((((((((((((((((((((((((VolunteerPart end

    public void addPersonnelToTeam(){
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

            System.out.println("after putting " + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTeamMemberPersonnelToSpinner();
                setPersonnelAvailableToSpinner();
                //System.out.println("############################################## personel ID : " + personnelID);
                try {
                    notificationSender.sendNotification(selectedTeamID + " takımına eklendiniz.",
                            "Yetkili yöneticilerden birisi sizi " + selectedTeamID + " takımına ekledi." , selectedAvailablePersonnelUser.getString("personnelID").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        Toast.makeText(this, "Takım elemanı eklendi", Toast.LENGTH_SHORT).show();
        //finish();
    }

    public void removePersonnelFromTeam(){
        System.out.println("smpu. to"   + selectedTeamMemberPersonnelUser.toString());
        JSONObject obj = new JSONObject();
        try {
            System.out.println("before putting rmv personnel " + obj.toString());
            obj.put("personnelID", selectedTeamMemberPersonnelUser.getString("personnelID"));
            obj.put("personnelName", selectedTeamMemberPersonnelUser.getString("personnelName"));
            obj.put("personnelEmail", selectedTeamMemberPersonnelUser.getString("personnelEmail"));
            obj.put("personnelRole", "Belirlenmedi");
            obj.put("teamID", "0"); // teamID set to 0 to clear assignment on team
            obj.put("latitude", selectedTeamMemberPersonnelUser.getString("latitude"));
            obj.put("longitude", selectedTeamMemberPersonnelUser.getString("longitude"));
            obj.put("institution", selectedTeamMemberPersonnelUser.getString("institution"));
            obj.put("locationTime", selectedTeamMemberPersonnelUser.getString("locationTime"));
            System.out.println("after putting rmv personnel:" + obj.toString());
        } catch (Exception e) {
            e.getMessage();
            System.out.println("yeter artık lutfen ");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTeamMemberPersonnelToSpinner();
                setPersonnelAvailableToSpinner();

                try {
                    notificationSender.sendNotification("Takımdan Çıkarıldınız",
                            "Yetkili yöneticilerden birisi sizi bulunduğunuz takımdan çıkardı.",selectedTeamMemberPersonnelUser.getString("personnelID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        Toast.makeText(this, "Takım elemanı çıkarıldı.", Toast.LENGTH_SHORT).show();
    }


    ////&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    public void addVolunteerToTeam(){
        JSONObject obj = new JSONObject();
        try {

            obj.put("volunteerID", selectedTeamMemberVolunteerUser.getString("volunteerID"));
            obj.put("volunteerName", selectedTeamMemberVolunteerUser.getString("volunteerName"));
            obj.put("address", selectedTeamMemberVolunteerUser.getString("address"));
            obj.put("isExperienced", selectedTeamMemberVolunteerUser.getString("isExperienced"));
            obj.put("haveFirstAidCert", selectedTeamMemberVolunteerUser.getString("haveFirstAidCert"));
            obj.put("requestedSubpart", selectedTeamMemberVolunteerUser.getString("requestedSubpart"));
            obj.put("responseSubpart", selectedTeamMemberVolunteerUser.getString("responseSubpart"));
            obj.put("assignedTeamID", selectedTeamID); // reset Team id
            obj.put("role", "Normal");
            obj.put("latitude", selectedTeamMemberVolunteerUser.getString("latitude"));
            obj.put("longitude", selectedTeamMemberVolunteerUser.getString("longitude"));
            obj.put("locationTime", selectedTeamMemberVolunteerUser.getString("locationTime"));
            obj.put("tc", selectedTeamMemberVolunteerUser.getString("tc"));
            obj.put("tel", selectedTeamMemberVolunteerUser.getString("tel"));
            obj.put("birthDate", selectedTeamMemberVolunteerUser.getString("birthDate"));

            System.out.println("after putting " + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTeamMemberVolunteerToSpinner();
                setVolunteerAvailableToSpinner();
                //System.out.println("############################################## personel ID : " + personnelID);
                try {
                    notificationSender.sendNotification(selectedTeamID + " takımına eklendiniz.",
                            "Yetkili yöneticilerden birisi sizi " + selectedTeamID + " takımına ekledi." , selectedAvailableVolunteerUser.getString("volunteerID").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        Toast.makeText(this, "Takım elemanı eklendi", Toast.LENGTH_SHORT).show();
        //finish();
    }

    public void removeVolunteerFromTeam(){
        //System.out.println("smpu. to"   + selectedTeamMemberPersonnelUser.toString());
        JSONObject obj = new JSONObject();
        try {

            obj.put("volunteerID", selectedTeamMemberVolunteerUser.getString("volunteerID"));
            obj.put("volunteerName", selectedTeamMemberVolunteerUser.getString("volunteerName"));
            obj.put("address", selectedTeamMemberVolunteerUser.getString("address"));
            obj.put("isExperienced", selectedTeamMemberVolunteerUser.getString("isExperienced"));
            obj.put("haveFirstAidCert", selectedTeamMemberVolunteerUser.getString("haveFirstAidCert"));
            obj.put("requestedSubpart", selectedTeamMemberVolunteerUser.getString("requestedSubpart"));
            obj.put("responseSubpart", selectedTeamMemberVolunteerUser.getString("responseSubpart"));
            obj.put("assignedTeamID", "0"); // reset Team id
            obj.put("role", "Belirlenmedi");
            obj.put("latitude", selectedTeamMemberVolunteerUser.getString("latitude"));
            obj.put("longitude", selectedTeamMemberVolunteerUser.getString("longitude"));
            obj.put("locationTime", selectedTeamMemberVolunteerUser.getString("locationTime"));
            obj.put("tc", selectedTeamMemberVolunteerUser.getString("tc"));
            obj.put("tel", selectedTeamMemberVolunteerUser.getString("tel"));
            obj.put("birthDate", selectedTeamMemberVolunteerUser.getString("birthDate"));

        } catch (Exception e) {
            e.getMessage();
            System.out.println("yeter artık lutfen ");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTeamMemberVolunteerToSpinner();
                setVolunteerAvailableToSpinner();

                try {
                    notificationSender.sendNotification("Takımdan Çıkarıldınız",
                            "Yetkili yöneticilerden birisi sizi bulunduğunuz takımdan çıkardı.",selectedTeamMemberVolunteerUser.getString("volunteerID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        Toast.makeText(this, "Takım elemanı çıkarıldı.", Toast.LENGTH_SHORT).show();
    }
    ////&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    ///////////////////////////////////drawer işlemleri////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void signOut() {
        LogoutHandler lout = new LogoutHandler(getApplicationContext());
        lout.updateUser();
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