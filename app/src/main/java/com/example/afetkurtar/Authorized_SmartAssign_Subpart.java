package com.example.afetkurtar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Authorized_SmartAssign_Subpart extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "Authorized_SmartAssign_Subpart";
    RequestQueue queue;
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;

    Spinner mainDisasterSpinner;

    private GoogleMap mMap;
    private Geocoder geocoder;

    LinearLayout scroll;
    private String howManyNumberText = "";
    private String needManPower = "";
    private String selectedDisasterID = "";

    ArrayAdapter adapter;
    private ArrayList<String> subpartStringList = new ArrayList<String>();
    private ArrayList<JSONObject> jsonObjectListForSubparts = new ArrayList<JSONObject>();

     private ArrayList<JSONObject> jsonObjectListForDisasters = new ArrayList<JSONObject>();
     private ArrayList<String> disasterStringList = new ArrayList<String>();

    ArrayList<String> selectedSubpartIDStringList = new ArrayList<String>();

    private ArrayList<JSONObject> jsonPeopleFromAlgorithmList = new ArrayList<JSONObject>();

    private ArrayList<JSONObject> allPersonnelsFromAlgoritm = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aut_smart_assign_subpart);
        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btn_smart_Assign).setOnClickListener(this::onClick);
        findViewById(R.id.btn_smartAssign_calculateHowManySubpart).setOnClickListener(this::onClick);
        findViewById(R.id.btn_smart_assign_accept).setOnClickListener(this::onClick);
        findViewById(R.id.btn_smart_assign_reject).setOnClickListener(this::onClick);

        setInvisibleEnters();
        setInvisibleAcceptReject();

        mainDisasterSpinner = (Spinner) findViewById(R.id.edit_smart_assign_main_disaster_select_spinner);
        initMap(); // initialize map
        initMainDisasterSpinner(); // init disaster ID
    }// onCreate end
    //////////////////////////////////AFTER ON CREATE//////////////////////////////////////////////////////

    private void calculateHowManyViewAdd(){
        howManyNumberText = ((EditText)findViewById(R.id.et_smartAssign_wantedNumberOfSubpart)).getText().toString();
        int howMany = 0;
        try{
            howMany = Integer.parseInt(howManyNumberText);
        }catch (Exception e){
            e.getMessage();
        }

        for(int i = 0; i< howMany; i++){
            addViewToScroll();
        }
    }
    private void setVisibleEnters(){
        ((EditText)findViewById(R.id.et_smartAssign_wantedNumberOfSubpart)).setText("");
        ((EditText)findViewById(R.id.et_smartAssign_wantedNumberOfManPower)).setText("");
        ((LinearLayout)findViewById(R.id.lin_lay_editTexts_smart_assign)).setVisibility(View.VISIBLE);

    }
    private void setInvisibleEnters(){
        ((EditText)findViewById(R.id.et_smartAssign_wantedNumberOfSubpart)).setText("");
        ((EditText)findViewById(R.id.et_smartAssign_wantedNumberOfManPower)).setText("");
        ((LinearLayout)findViewById(R.id.lin_lay_editTexts_smart_assign)).setVisibility(View.GONE);
    }
    private void setInvisibleAcceptReject(){
        ((LinearLayout)findViewById(R.id.aut_smart_assign_accept_reject_layout)).setVisibility(View.GONE);
    }
    private void setVisibleAcceptReject(){
        ((LinearLayout)findViewById(R.id.aut_smart_assign_accept_reject_layout)).setVisibility(View.VISIBLE);
    }

    private void addViewToScroll(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_aut_smart_assign_subpart_addlayout, null);
        scroll = findViewById(R.id.aut_smart_assign_linearLayoutScroll);
        TextView tv_Linear = findViewById(R.id.tv_smart_assign_selectedTeam_subpartList);
        Spinner linearSpinner = (Spinner) view.findViewById(R.id.smart_assign_subpart_spinner);

        setSubpartToSpinner(linearSpinner);
        scroll.addView(view);
    }//

    //// Set Spinner ////start
    private void setSubpartToSpinner(Spinner subpartSpinner){
        JSONObject obj = new JSONObject();
        try {
            obj.put("disasterID", selectedDisasterID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/subpart/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponseForSubpartSpinner(response,subpartSpinner);
                            //refreshActivity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void handleResponseForSubpartSpinner(JSONObject a,Spinner availableMembersSpinner) {
        try{
            subpartStringList.clear();
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> list = new ArrayList<String>();
        try {
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

                    subpartStringList.add(tmp.getString("subpartID").toString().trim());
                    jsonObjectListForSubparts.add(tmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subpartStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            availableMembersSpinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end

    //// Set Spinner ////end

    private void initMainDisasterSpinner(){

        try{
            setMainDisasterSpinner();

        }catch (Exception e){
            e.printStackTrace();
        }

        mainDisasterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Test Authorized Team onItemSelected");
                if (((Spinner) findViewById(R.id.edit_smart_assign_main_disaster_select_spinner)).getSelectedItem().toString().equalsIgnoreCase("Afet Seçin")) {
                    //System.out.println("Test Authorized Team : seçilmedi");
                    //textAfetId.setText("seçilmedi");
                } else {
                    for (JSONObject x : jsonObjectListForDisasters) {
                        //System.out.println("our X object : " + x.toString());
                        try {
                            String disasterID = ((Spinner) findViewById(R.id.edit_smart_assign_main_disaster_select_spinner)).getSelectedItem().toString();
                            disasterID = disasterID.substring(disasterID.indexOf(":")+ 1,disasterID.indexOf(",")).trim();
                            System.out.println("our disaster ID: *" + disasterID+ "*");
                            if (x.getString("disasterID").trim().equals(disasterID.trim())) {
                                selectedDisasterID = disasterID;
                                //System.out.println("our X object inside if : " + x.toString());
                                //selectedDisasterID = x.getString("disasterID");
                                setVisibleEnters();
                                setInvisibleAcceptReject();
                                resetScrollElements();

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
                System.out.println("Nothing selected ");
            }
        });

    }
    //// Set Spinner ////end

    //////setMainDisaster ID /////// start
    private void setMainDisasterSpinner() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/disasterEvents/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            handleResponseForDisasterTable(response);
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

    private void handleResponseForDisasterTable(JSONObject a) {
        try{
            disasterStringList.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>();
        try {
            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {

                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);
            disasterStringList.add("Afet Seçin");

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);

                    disasterStringList.add("ID : " + tmp.getString("disasterID") + ", İsim: " + tmp.getString("disasterName"));
                    jsonObjectListForDisasters.add(tmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, disasterStringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mainDisasterSpinner.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    //////setMainDisaster ID /////// end
    private boolean isManPowerSelected(){
        try {
            needManPower = ((EditText)findViewById(R.id.et_smartAssign_wantedNumberOfManPower)).getText().toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(needManPower.equals("")){
            return false;
        }
        return true;
    }
    /////ON CLICK/////start
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_smartAssign_calculateHowManySubpart:
                try{
                    if(!selectedDisasterID.equalsIgnoreCase("")){

                        System.out.println("Selected disaster ID : ?" + selectedDisasterID + "?");
                        resetScrollElements();
                        calculateHowManyViewAdd();
                    }
                }catch (Exception e){
                    e.getMessage();
                }
                break;
            case R.id.btn_smart_Assign:
                try{
                    mMap.clear();
                    // if manpower selected and disasterID selected than check is subparts selected
                    if(isManPowerSelected() && (!selectedDisasterID.equalsIgnoreCase(""))){
                        //setVisibleAcceptReject();
                        calculateAlgorithmWithSelectedSubparts();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.btn_smart_assign_accept:
                acceptAlgorithmResult();
                break;
            case R.id.btn_smart_assign_reject:
                rejectAlgorithmResult();
                break;
        }
    }

    ////////calculate with algorithm /////start
    private void calculateAlgorithmWithSelectedSubparts(){

        //reset list
        try {
            selectedSubpartIDStringList.clear();
        }catch (Exception e){
            e.printStackTrace();
        }

        int count = scroll.getChildCount();

        for(int i = 0; i< count; i++){
            Spinner linearSpinner = (Spinner) scroll.getChildAt(i).findViewById(R.id.smart_assign_subpart_spinner);
            String selectedSubpartID = linearSpinner.getSelectedItem().toString();

            if(!(selectedSubpartID.equalsIgnoreCase("") || selectedSubpartID.equalsIgnoreCase("Subpart Seçin"))) {
                selectedSubpartIDStringList.add(selectedSubpartID);
            }

        }
        initSubpartMarkersOnMap();

        callAlgorithm();

    }
    private void initSubpartMarkersOnMap(){
        for(int i = 0; i< selectedSubpartIDStringList.size(); i++){
            findSelectedSubpartLocationAndAddOnMap(selectedSubpartIDStringList.get(i));
        }
    }
    private void findSelectedSubpartLocationAndAddOnMap(String subpartID){
        JSONObject obj = new JSONObject();
        try {
            obj.put("subpartID", subpartID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/subpart/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            response = new JSONObject(cevap);

                            setSubpartMarker(response);

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
    private void callAlgorithm(){

        JSONObject obj = new JSONObject();

        ArrayList<Integer> subpartIDIntegerList = new ArrayList<Integer>();

        for (int i = 0; i < selectedSubpartIDStringList.size(); i++){
            try{
                subpartIDIntegerList.add(Integer.parseInt(selectedSubpartIDStringList.get(i)));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        try {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONArray myJsonArray = new JSONArray(subpartIDIntegerList);
                obj.put("subpartIDs",myJsonArray);
            }
            obj.put("numberOfPeople",needManPower);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("bizim objemiz : " + obj.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/smartAssignment.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("our RESPONSEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE : " + response);
                            handleJsonPeopleFromAlgorithmList(response); // after this method list will initialize
                            initPeopleMarkersOnMap();
                            setVisibleAcceptReject();

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }
    private void initPeopleMarkersOnMap(){
        allPersonnelsFromAlgoritm.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/personnelUser/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ////////////
                        ArrayList<String> list = new ArrayList<String>();

                        try {

                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);

                            while (cevap.indexOf(",{") > -1) {
                                list.add(cevap.substring(0, cevap.indexOf(",{")));
                                cevap = cevap.substring(cevap.indexOf(",{") + 1);
                            }
                            list.add(cevap);

                            for (String x : list) {
                                try {
                                    JSONObject tmp = new JSONObject(x);

                                    for(int i = 0; i< jsonPeopleFromAlgorithmList.size(); i++){
                                        if(tmp.getString("personnelID").equals(jsonPeopleFromAlgorithmList.get(i).getString("personnelID"))){
                                            setPersonnelMarker(tmp,jsonPeopleFromAlgorithmList.get(i).getString("subID"));
                                            allPersonnelsFromAlgoritm.add(tmp);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
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
    private void setSubpartMarker(JSONObject myObject){
        double latitude = 0;
        double longitude = 0;
        try {
            latitude = Double.parseDouble(myObject.getString("latitude"));
            longitude = Double.parseDouble(myObject.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        try {

            Marker peopleMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Subpart ID: " + myObject.getString("subpartID"))
                    .snippet("Subpart İsmi: " + myObject.getString("subpartName")).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_subpartflag)));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPersonnelMarker(JSONObject myObject, String subID){

        double latitude = 0;
        double longitude = 0;
        try {
            latitude = Double.parseDouble(myObject.getString("latitude"));
            longitude = Double.parseDouble(myObject.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        try {

            Marker peopleMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Personnel ID: " + myObject.getString("personnelID"))
                    .snippet("Personnel İsmi: " + myObject.getString("personnelName")
                                    + "\nAtanması planlanan subpart ID: " + subID)
                    .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_personmap)));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void handleJsonPeopleFromAlgorithmList(JSONObject response){
        jsonPeopleFromAlgorithmList.clear();
        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < selectedSubpartIDStringList.size(); i++){
            String cevap = null;
            try {
                cevap = response.getString(selectedSubpartIDStringList.get(i));
                cevap = cevap.substring(1, cevap.length() - 1);


                while (cevap.indexOf(",{") > -1) {
                    list.add(cevap.substring(0, cevap.indexOf(",{")));
                    cevap = cevap.substring(cevap.indexOf(",{") + 1);
                }
                list.add(cevap);

                for (String x : list) {
                    try {
                        // algoritma sonucu belirlenen elemanlar tmp içinde
                        JSONObject tmp = new JSONObject(x);
                        tmp.put("subID", selectedSubpartIDStringList.get(i));
                        jsonPeopleFromAlgorithmList.add(tmp);

                        //tmp.getString("personnelID");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }// for end

            } catch (JSONException e) {
                e.printStackTrace();
            }
            cevap = cevap.substring(1, cevap.length() - 1);
        }



    }
    private void acceptAlgorithmResult(){ // on click accept e ekle

        ////Alttak işlemler takım atamalaarı için ////
        for(int i = 0; i < selectedSubpartIDStringList.size(); i++){
            findTeamFromSubpartID(selectedSubpartIDStringList.get(i));
        }// for end

    }
    private void rejectAlgorithmResult(){ // on click reject e ekle
        refreshActivity();
    }
    private void findTeamFromSubpartID(String subpartID){
        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedSubpartID", subpartID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/team/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                             System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!response : " + response.toString());
                            String cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);
                            response = new JSONObject(cevap);
                            // burdaki response o subPID için team bulursa olan team objesi
                            if(response.getString("assignedSubpartID").toString().equalsIgnoreCase(subpartID)){
                                //takım bulundu takıma ekle
                                updatePersonnelsTeamID(response.getString("teamID"),subpartID);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                        createTeam(subpartID);
                        System.out.println("EEEE222222: " + error.getLocalizedMessage());
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void createTeam(String subpartID){
        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedSubpartID", subpartID);
            obj.put("status", "belirsiz");
            obj.put("needManPower", "0");
            obj.put("needEquipment", "0");
        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/team/create.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String ourTeamID = "0";
                try {
                    ourTeamID = response.getString("id");
                    updatePersonnelsTeamID(ourTeamID,subpartID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
    ////////calculate with algorithm /////end
    private void updatePersonnelsTeamID(String newTeamID, String subpartID){
        for(int i = 0; i < jsonPeopleFromAlgorithmList.size(); i++){
            try {
                if(jsonPeopleFromAlgorithmList.get(i).getString("subID").equals(subpartID)){
                    for(int j = 0 ; j <allPersonnelsFromAlgoritm.size(); j++){
                        if(jsonPeopleFromAlgorithmList.get(i).getString("personnelID").trim().equals(allPersonnelsFromAlgoritm.get(j).getString("personnelID").trim())){
                            addPersonnelToTeam(allPersonnelsFromAlgoritm.get(j), newTeamID);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public void addPersonnelToTeam(JSONObject personnel, String teamID){
        JSONObject obj = new JSONObject();
        try {
            obj.put("personnelID", personnel.getString("personnelID"));
            obj.put("personnelName", personnel.getString("personnelName"));
            obj.put("personnelEmail", personnel.getString("personnelEmail"));
            obj.put("personnelRole", "Normal");
            obj.put("teamID", teamID); // teamID set to 0 to clear assignment on team
            obj.put("latitude", personnel.getString("latitude"));
            obj.put("longitude", personnel.getString("longitude"));
            obj.put("institution", personnel.getString("institution"));
            obj.put("locationTime", personnel.getString("locationTime"));

        } catch (Exception e) {
            e.printStackTrace();
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
    }
    ////////////////////////MAP//////////////////////start
    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Authorized_SmartAssign_Subpart.this));
        System.out.println("test : map is ready");
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
    }//onMapReady end

    public void initMap(){
        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.aut_smart_assign_subpart_map);
        mapFragment.getMapAsync(Authorized_SmartAssign_Subpart.this) ;
    }
    ////////////////////////MAP//////////////////////end
    ///REFRESH ACTIVITY///start
    public void refreshActivity(){
        finish();
        startActivity(getIntent());
    }
    ///REFRESH ACTIVITY///end
    public void resetScrollElements(){
        ((LinearLayout)findViewById(R.id.aut_smart_assign_linearLayoutScroll)).removeAllViews();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0, vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



}//class end
