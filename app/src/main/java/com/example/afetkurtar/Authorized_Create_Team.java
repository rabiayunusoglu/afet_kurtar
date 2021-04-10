package com.example.afetkurtar;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import java.util.ArrayList;

public class Authorized_Create_Team extends AppCompatActivity {
    private static final String TAG = "Authorized_Create_Team";
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    LinearLayout scroll;
    String howManyNumberText = "";
    RequestQueue queue;
    boolean control = false;
    RadioGroup radioGroupRoleOfTeamMembers;
    String createdTeamID = "0";
    NotificationSender notificationSender;
    private RadioButton radioButtonRoleOfTeamMembers;
    private ArrayList<String> selectedPersonnelListWithID = new ArrayList<String>();
    private ArrayList<String> selectedPersonnelListWithRole = new ArrayList<String>();
    private ArrayList<String> afterCreateTeamStringList = new ArrayList<String>();
    private ArrayList<JSONObject> availablePersonnelObjectList = new ArrayList<JSONObject>();

    ArrayAdapter adapter;
    ArrayList<String> personnelAvailableStringList = new ArrayList<String>();
    ArrayList<View> viewList = new ArrayList<View>();
    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_create_team);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn_createTeam_calculateHowManyMember).setOnClickListener(this::onClick);
        findViewById(R.id.btn_createTeam_Create).setOnClickListener(this::onClick);
        findViewById(R.id.btn_returnTo_assignTeam_fromCreateTeam).setOnClickListener(this::onClick);
        notificationSender = new NotificationSender(getApplicationContext());

        drawerLayout = findViewById(R.id.authorized_create_team_drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
                    createTeamWithSelectedPersonnels();
                    /*
                    control = false;
                    selectedPersonnelListWithID.clear();
                    selectedPersonnelListWithRole.clear();
                    afterCreateTeamStringList.clear();
                    availablePersonnelObjectList.clear();
                     */

                }catch (Exception e){
                    e.getMessage();
                }
                break;
            case R.id.btn_returnTo_assignTeam_fromCreateTeam:
                try{
                    //burada team id yi assign sayfasındaki spinner a bir şekilde ekleyelim
                    openTeamAssignPage();
                }catch (Exception e){
                    e.getMessage();
                }
                break;
        }
    }
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
        }//end of for
        if(!controlPersonnelListWithRole()){
            return;
        }
        if(!controlPersonnelListWithID()){
            return;
        }

        createTeamWithGivenInformationToDB();
    }
    public void findCreatedTeamID(){
        readFromTeamTable();
    }
    public void readFromTeamTable() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/team/read.php", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            handleResponseForTeamTable(response);
                            /*
                            if(Authorized_Assign_Team.teamStringListSize == afterCreateTeamStringList.size()){
                                createdTeamID = "0";
                            }else{
                                // db de ıd ne olursa olsun hep son eleman
                                // olarak eklenicek gibi olduğundan okuduğum verilerde en son elemanın team id sini team id olarak belirlerim
                                //createdTeamID = afterCreateTeamStringList.get(afterCreateTeamStringList.size()-1);
                            }

                             */
                            /// team id üretildi şimdi bu team id kullanılarak üyelerin assigned team id lerini burada güncelle
                            updateNewPersonnelsTeamID();
                            refreshActivity(); // dikkat burada refresh atsın diye bu methodu çağırttım
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
                    afterCreateTeamStringList.add(tmp.getString("teamID"));
                } catch (Exception e) {
                    e.printStackTrace();
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

                System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY==" + response.toString() + "==YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                try {
                    createdTeamID = response.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                findCreatedTeamID();

                //finish(); // Dikkat finish i takım oluşturunca yapsın diye buraya koydum
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        queue.add(request);
        Toast.makeText(this, "Takım oluşturuldu.", Toast.LENGTH_LONG).show();
    }
    public void openTeamAssignPage(){
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
         scroll.addView(view);
     }// deneme end
    public void setPersonnelAvailableToSpinner(Spinner availableMembersSpinner) {
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
                            handleResponseForPersonnelAvailableTable(response,availableMembersSpinner);
                            //refreshActivity();
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
    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    public void handleResponseForPersonnelAvailableTable(JSONObject a,Spinner availableMembersSpinner) {

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
                            if(!control){
                                availablePersonnelObjectList.add(tmp);
                            }
                        //}
                        jsonObjectList.add(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                control = true;




            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, personnelAvailableStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        for(int i = 0; i < availablePersonnelObjectList.size(); i++){
            try {

                if(selectedPersonnelListWithID.contains(availablePersonnelObjectList.get(i).getString("personnelID"))){
                    updateTeamIDAndRole(availablePersonnelObjectList.get(i),selectedPersonnelListWithRole
                            .get( selectedPersonnelListWithID.indexOf((availablePersonnelObjectList.get(i).getString("personnelID")))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    public void updateTeamIDAndRole(JSONObject myJsonObject,String role){
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

        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/personnelUser/update.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());

                /*
                try {
                    System.out.println("############################################## personel ID : " + myJsonObject.getString("personnelID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                 */
                try {
                    notificationSender.sendNotification(createdTeamID + " takımına eklendiniz.",
                            "Yetkili yöneticilerden birisi sizi " + createdTeamID + " takımına ekledi." , myJsonObject.getString("personnelID").trim());
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



}//class end
