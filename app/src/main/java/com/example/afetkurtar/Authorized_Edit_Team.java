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

    ArrayAdapter adapter;
    NotificationSender notificationSender;
    ArrayList<String> personnelAvailableStringList = new ArrayList<String>();
    ArrayList<String> personnelTeamMemberStringList = new ArrayList<String>();
    JSONObject selectedAvailablePersonnelUser = new JSONObject();
    JSONObject selectedTeamMemberPersonnelUser = new JSONObject();

    ArrayList<JSONObject> jsonObjectListForAvailableUser = new ArrayList<JSONObject>();
    ArrayList<JSONObject> jsonObjectListForTeamMember = new ArrayList<JSONObject>();

    private String selectedTeamID = "";
    private String ourManPower = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_edit_team);
        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btn_editTeam_addPersonnel).setOnClickListener(this::onClick);
        findViewById(R.id.btn_editTeam_removePersonnel).setOnClickListener(this::onClick);

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

        try{
            this.selectedTeamID = Authorized_Assign_Team.teamID;
            this.ourManPower = Authorized_Assign_Team.needManPower;

        }catch (Exception e){
            e.getMessage();
        }
        initTextViews();

        initPersonnelSpinners();
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
                        //System.out.println("our X object : " + x.toString());
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
    public void initTextViews(){
        ((TextView)(findViewById(R.id.tv_team_manpower))).setText(ourManPower);
        ((TextView)(findViewById(R.id.tv_selected_teamID))).setText(selectedTeamID);

    }
    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    public String findPersonnelID(String line){
         if(line.indexOf(":") != -1){
             line = line.substring(line.indexOf(":")+1,line.indexOf(","));
             line.trim();
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